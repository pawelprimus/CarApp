package pl.primrim.carapp;


import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Car extends RepresentationModel<Car> {

    public enum Colour {
        RED, GREEN, BLUE;
    }

    private long id;
    @NotNull
    @Size(min = 2, max = 20)
    private String mark;
    @NotNull
    @Size(min = 2, max = 20)
    private String model;
    private Colour colour;

    public Car(long id, String mark, String model, Colour colour) {
        this.id = id;
        this.mark = mark;
        this.model = model;
        this.colour = colour;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }
}
