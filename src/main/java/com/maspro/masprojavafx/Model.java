package com.maspro.masprojavafx;

import com.maspro.masprojavafx.Exceptions.InvalidInputException;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class Model extends ObjectPlus implements Serializable {
    private String make; // manufacturer of this model
    private String model; // name of the model

    public Model(String make, String model) throws InvalidInputException {
        super();
        setMake(make);
        setModel(model);
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) throws InvalidInputException {
        if (make == null) {
            throw new InvalidInputException("invalid make");
        }
        if (make.isEmpty()) {
            throw new InvalidInputException("invalid make");
        }
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) throws InvalidInputException {
        if (model == null) {
            throw new InvalidInputException("invalid model");
        }
        if (model.isEmpty()) {
            throw new InvalidInputException("invalid model");
        }
        this.model = model;
    }
}
