package com.maspro.masprojavafx;

import com.maspro.masprojavafx.Exceptions.InvalidInputException;

import java.io.Serializable;

public class Address implements Serializable { // a complex attribute for class Customer
    private String street, city, postal;

    public Address(String street, String city, String postal) throws InvalidInputException {
        setStreet(street);
        setCity(city);
        setPostal(postal);
    }

    public String getStreet() {
        return street;
    }

    private void setStreet(String street) throws InvalidInputException {
        if (street.equals("")) {
            throw new InvalidInputException("invalid street");
        } else {
            this.street = street;
        }
    }

    public String getCity() {
        return city;
    }

    private void setCity(String city) throws InvalidInputException {
        if (city.equals("")) {
            throw new InvalidInputException("invalid city");
        } else {
            this.city = city;
        }
    }

    public String getPostal() {
        return postal;
    }

    private void setPostal(String postal) throws InvalidInputException {
        if (postal.equals("")) {
            throw new InvalidInputException("invalid postal");
        } else {
            this.postal = postal;
        }
    }
}
