package com.maspro.masprojavafx;

import com.maspro.masprojavafx.Exceptions.InvalidInputException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Car extends Vehicle {
    private Customer owner; // customer who owns the car
    private int storageCapacity; // with back seats lowered in m3
    private static List<Car> carsList;

    public Car(Customer owner, String registration, LocalDate technicalExaminationExpirationDate, Model model, Set<Tag> tags, Set<DriveType> driveTypes, Integer engineCapacity, FuelType fuelType, Integer batteryCapacity, int storageCapacity) throws InvalidInputException {
        super(registration, technicalExaminationExpirationDate, model, tags, driveTypes, engineCapacity, fuelType, batteryCapacity);
        setOwner(owner);
        setStorageCapacity(storageCapacity);
        addCarToList(this);
    }

    public Customer getOwner() {
        return owner;
    }

    protected void setOwner(Customer owner) throws InvalidInputException {
        if (owner != null) {
            this.owner = owner;
        } else throw new InvalidInputException("invalid customer");
    }

    public int getStorageCapacity() {
        return storageCapacity;
    }

    public void setStorageCapacity(int storageCapacity) throws InvalidInputException {
        if (storageCapacity < 0) {
            throw new InvalidInputException("invalid storageCapacity");
        }
        this.storageCapacity = storageCapacity;
    }

    public void setNewOwnerRelation(Customer customer) throws InvalidInputException {
        if (customer != null) {
            customer.createNewCarRelation(this);
        } else throw new InvalidInputException("invalid customer");
    }

    public void changeOwnerRelation(Customer customer) throws InvalidInputException {
        if (owner != null) {
            if (customer != null) {
                if (!customer.getCars().contains(this)) {
                    removeOwnerRelation();
                    customer.addCar(this);
                    setOwner(customer);
                } else throw new InvalidInputException("this customer already owns this car");
            } else throw new InvalidInputException("invalid customer");
        } else throw new InvalidInputException("there is no owner");
    }

    public void removeOwnerRelation() throws InvalidInputException {
        if (owner != null) {
            owner.removeCarRelation(this);
        } else throw new InvalidInputException("no owner");
    }

    protected void removeOwner() throws InvalidInputException {
        if (owner != null) {
            owner = null;
        } else {
            throw new InvalidInputException("there is no owner");
        }
    }

    public static Car getCarByRegistration(String registration) {
        if (getExtent(Car.class) == null) {
            return null;
        }
        for (Car c : getExtent(Car.class)) {
            if (Objects.equals(c.getRegistration(), registration)) {
                return c;
            }
        }
        return null;
    }

    private void addCarToList(Car car) {
        if (carsList == null) {
            carsList = new ArrayList<>();
        }
        carsList.add(car);
    }

    public void deleteCar() throws InvalidInputException {
        removeOwnerRelation();
        for (Appointment a : getExtent(Appointment.class)) {
            if (a.getCar() == this) {
                a.deleteAppointment();
            }
        }
    }
}
