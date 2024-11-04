package com.maspro.masprojavafx;

import com.maspro.masprojavafx.Exceptions.InvalidInputException;

import java.io.Serializable;

public abstract class Person extends ObjectPlus implements Serializable {
    private String name;
    private String surname;

    public double appointmentEstimation(Appointment appointment) throws InvalidInputException {
        return 0;
    }//method overriding - inherited by Customer Technician and Manager (different logic in each)

    public Person(String name, String surname) throws InvalidInputException {
        super();
        try {
            setName(name);
            setSurname(surname);
        } catch (Exception e) {
            removeThisFromClassExtent();
            throw e;
        }

    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    protected void setName(String name) throws InvalidInputException {
        if (name == null) {
            throw new InvalidInputException("invalid name");
        }
        if (name.contains(" ") || name.isEmpty()) {
            throw new InvalidInputException("invalid name");
        }
        this.name = name;
    }

    protected void setSurname(String surname) throws InvalidInputException {
        if (surname == null) {
            throw new InvalidInputException("invalid surname");
        }
        if (surname.contains(" ") || surname.isEmpty()) {
            throw new InvalidInputException("invalid surname");
        }
        this.surname = surname;
    }
}
