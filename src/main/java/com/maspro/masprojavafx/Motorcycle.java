package com.maspro.masprojavafx;

import com.maspro.masprojavafx.Exceptions.InvalidInputException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Motorcycle extends Vehicle {
    private Customer owner; // customer who owns the motorcycle
    private boolean hasStorage; // if the motorcycle has trunks
    private static List<Motorcycle> motorcyclesList;

    public Motorcycle(Customer owner, String registration, LocalDate technicalExaminationExpirationDate, Model model, Set<Tag> tags, Set<DriveType> driveTypes, Integer engineCapacity, FuelType fuelType, Integer batteryCapacity, boolean hasStorage) throws InvalidInputException {
        super(registration, technicalExaminationExpirationDate, model, tags, driveTypes, engineCapacity, fuelType, batteryCapacity);
        setOwner(owner);
        setHasStorage(hasStorage);
        addMotorcycleToList(this);
    }

    public Customer getOwner() {
        return owner;
    }

    protected void setOwner(Customer owner) throws InvalidInputException {
        if (owner != null) {
            this.owner = owner;
        } else throw new InvalidInputException("invalid customer");
    }

    public boolean HasStorage() {
        return hasStorage;
    }

    protected void setHasStorage(boolean hasStorage) {
        this.hasStorage = hasStorage;
    }

    public void setNewOwnerRelation(Customer customer) throws InvalidInputException {
        if (customer != null) {
            customer.createNewMotorcycleRelation(this);
        } else throw new InvalidInputException("invalid customer");
    }

    public void changeOwnerRelation(Customer customer) throws InvalidInputException {
        if (owner != null) {
            if (customer != null) {
                if (!customer.getMotorcycles().contains(this)) {
                    removeOwnerRelation();
                    customer.addMotorcycle(this);
                    setOwner(customer);
                } else throw new InvalidInputException("this customer already owns this motorcycle");
            } else throw new InvalidInputException("invalid customer");
        } else throw new InvalidInputException("there is no owner");
    }

    public void removeOwnerRelation() throws InvalidInputException {
        if (owner != null) {
            owner.removeMotorcycleRelation(this);
        } else throw new InvalidInputException("no owner");
    }

    protected void removeOwner() throws InvalidInputException {
        if (owner != null) {
            owner = null;
        } else {
            throw new InvalidInputException("there is no owner");
        }
    }

    public static Motorcycle getMotorcycleByRegistration(String registration) {
        if (motorcyclesList == null) {
            return null;
        }
        for (Motorcycle m : motorcyclesList) {
            if (Objects.equals(m.getRegistration(), registration)) {
                return m;
            }
        }
        return null;
    }

    private void addMotorcycleToList(Motorcycle motorcycle) {
        if (motorcyclesList == null) {
            motorcyclesList = new ArrayList<>();
        }
        motorcyclesList.add(motorcycle);
    }

    public void deleteMotorcycle() throws InvalidInputException {
        removeOwnerRelation();
        for (Appointment a : getExtent(Appointment.class)) {
            if (a.getMotorcycle() == this) {
                a.deleteAppointment();
            }
        }
    }
}
