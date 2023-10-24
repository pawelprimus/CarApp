package pl.primrim.carapp;


import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/cars")
public class CarApi {

    private List<Car> carList;

    public CarApi() {
        this.carList = new ArrayList<>();

        carList.add(new Car(1, "BMW", "model1", Car.Colour.RED));
        carList.add(new Car(2, "BMW", "model2", Car.Colour.RED));
        carList.add(new Car(3, "Mercedes", "model3",Car.Colour.GREEN));
        carList.add(new Car(4, "Fiat", "model4", Car.Colour.BLUE));
    }

    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Car>> getCars() {

        return new ResponseEntity<>(carListToCarListWithLinks(carList), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Car> getCar(@PathVariable long id) {

        Link link = linkTo(CarApi.class).slash(id).withSelfRel();

        Optional<Car> firstCar = carList.stream().filter(car -> car.getId() == id).findFirst();

        return firstCar.map(car -> new ResponseEntity<>(
                        car.add(link), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @GetMapping(path = "/color?colour={colour}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Car>> getCarByColor(@PathVariable Car.Colour colour) {
        List<Car> filteredByColorCars = carList.stream().filter(car -> car.getColour() == colour).collect(Collectors.toList());
        if (!filteredByColorCars.isEmpty()) {
            return new ResponseEntity<>(carListToCarListWithLinks(filteredByColorCars), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity addCar(@RequestBody Car car) {
        boolean add = carList.add(car);
        if (add) {
            return new ResponseEntity(HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity modVideo(@RequestBody Car newCar) {

        Optional<Car> first = carList.stream().filter(car -> car.getId() == newCar.getId()).findFirst();
        if (first.isPresent()) {
            carList.remove(first.get());
            carList.add(newCar);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity removeVideo(@RequestBody Long id) {

        Optional<Car> first = carList.stream().filter(car -> car.getId() == id).findFirst();
        if (first.isPresent()) {
            carList.remove(first.get());
            return new ResponseEntity<>(first.get(), HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private List<Car> carListToCarListWithLinks(List<Car> carList) {
        List<Car> carsWithLinks = new ArrayList<>();
        for (Car car : carList) {
            Link link = linkTo(CarApi.class).slash(car.getId()).withSelfRel();
            car.add(link);
            carsWithLinks.add(car);
        }
        return carsWithLinks;
    }
}
