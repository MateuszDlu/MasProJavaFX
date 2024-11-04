package com.maspro.masprojavafx;

import com.maspro.masprojavafx.Exceptions.InvalidInputException;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public abstract class Vehicle extends ObjectPlus implements Serializable {
    private String registration; // registration number of the vehicle
    private LocalDate technicalExaminationExpirationDate; // expiration date of vehicle's technical examination
    private Model model; // model of the vehicle
    private Set<Tag> tags = new HashSet<>(); // types that the vehicle belongs to
    private Set<DriveType> driveTypes;//Electric or combustion
    private boolean isHybrid; // if is electric and combustion at the same time
    private Integer batteryCapacity;// in case of electric drive
    private Integer engineCapacity;// in case of combustion drive
    private FuelType fuelType;// in case of combustion drive
    private static List<Vehicle> listOfVehicles = new ArrayList<Vehicle>(); // collection of all vehicles

    public Vehicle(String registration, LocalDate technicalExaminationExpirationDate, Model model, Set<Tag> tags, Set<DriveType> driveTypes, Integer engineCapacity, FuelType fuelType, Integer batteryCapacity) throws InvalidInputException {
        super();
        try {
            setRegistration(registration);
            setTechnicalExaminationExpirationDate(technicalExaminationExpirationDate);
            setModel(model);
            setTags(tags);
            setDriveTypes(driveTypes);
            if (this.driveTypes.contains(DriveType.ELECTRIC) && this.driveTypes.contains(DriveType.COMBUSTION)) {
                isHybrid = true;
            }
            setEngineCapacity(engineCapacity);
            setFuelType(fuelType);
            setBatteryCapacity(batteryCapacity);
            setEngineCapacity(engineCapacity);
            addVehicleToList(this);
            //listOfVehicles.add(this);
        } catch (Exception e) {
            removeThisFromClassExtent();
            throw e;
        }
    }


    protected void setBatteryCapacity(Integer batteryCapacity) throws InvalidInputException {
        if (driveTypes.contains(DriveType.ELECTRIC)) {
            if (batteryCapacity == null) {
                throw new InvalidInputException("invalid battery capacity");
            }
        }
        this.batteryCapacity = batteryCapacity;
    }

    protected void setFuelType(FuelType fuelType) throws InvalidInputException {
        if (driveTypes.contains(DriveType.COMBUSTION)) {
            if (fuelType == null) {
                throw new InvalidInputException("invalid fuel type");
            }
        }
        this.fuelType = fuelType;
    }

    protected void setDriveTypes(Set<DriveType> driveTypes) throws InvalidInputException {
        if (driveTypes.size() > 2 || driveTypes.isEmpty()) {
            throw new InvalidInputException("invalid drive types");
        }
        this.driveTypes = driveTypes;
    }

    public static List<Object> getNearTechnicalExaminationDueList() {
        LocalDate monthFromNow = LocalDate.now().plusMonths(1);
        List<Object> returnList = new ArrayList<Object>();
        for (Car c : ObjectPlus.getExtent(Car.class)) {
            if (c.getTechnicalExaminationExpirationDate().isBefore(monthFromNow)) {
                returnList.add(c);
            }
        }
        for (Motorcycle m : ObjectPlus.getExtent(Motorcycle.class)) {
            if (m.getTechnicalExaminationExpirationDate().isBefore(monthFromNow)) {
                returnList.add(m);
            }
        }
        return returnList;
    }

    public Set<Tag> getTags() {
        if (tags == null) {
            return null;
        }
        return Collections.unmodifiableSet(tags);
    }

    protected void setTags(Set<Tag> tags) throws InvalidInputException {
        if (this.tags == null) {
            this.tags = new HashSet<>();
        }
        if (tags != null && !tags.isEmpty()) {
            for (Tag t : tags) {
                addTag(t);
            }
        } else throw new InvalidInputException("invalid tags");
    }

    public void addTag(Tag tag) throws InvalidInputException {
        if (tag == null || tags.contains(tag)) {
            throw new InvalidInputException("invalid tag");
        } else {
            tags.add(tag);
        }
    }

    public void removeTag(Tag tag) throws InvalidInputException {
        if (tag == null || !tags.contains(tag)) {
            throw new InvalidInputException("invalid tag");
        } else if (tags.size() == 1) {
            throw new InvalidInputException("last element");
        } else {
            tags.remove(tag);
        }
    }

    public Model getModel() {
        return model;
    }

    private void setModel(Model model) {
        this.model = model;
    }

    private void addVehicleToList(Vehicle vehicle) {
        if (listOfVehicles == null) {
            listOfVehicles = new ArrayList<>();
        }
        listOfVehicles.add(vehicle);
    }

    protected void setEngineCapacity(Integer engineCapacity) throws InvalidInputException {
        if (driveTypes.contains(DriveType.COMBUSTION)) {
            if (engineCapacity == null) {
                throw new InvalidInputException("invalid engine capacity");
            }
        }
        this.engineCapacity = engineCapacity;
    }//private because shouldn't be changed

    private void setRegistration(String registration) throws InvalidInputException {
        if (registration == null) {
            throw new InvalidInputException("invalid registration");
        }
        if (registration.isEmpty() || registration.contains(" ")) {
            throw new InvalidInputException("invalid registration");
        }
        for (Vehicle v : listOfVehicles) {
            if (Objects.equals(v.getRegistration(), registration)) {
                throw new InvalidInputException("duplicate registration");
            }
        }
        this.registration = registration;
    }//private because shouldn't be changed

    protected void setTechnicalExaminationExpirationDate(LocalDate technicalExaminationExpirationDate) {
        this.technicalExaminationExpirationDate = technicalExaminationExpirationDate;
    }

    private static void setListOfVehicles(List<Vehicle> listOfVehicles) {
        Vehicle.listOfVehicles = listOfVehicles;
    }//private because shouldn't be changed

    public Integer getEngineCapacity() {
        return engineCapacity;
    }

    public String getRegistration() {
        return registration;
    }

    public LocalDate getTechnicalExaminationExpirationDate() {
        return technicalExaminationExpirationDate;
    }

    public static List<Object> getListOfVehicles() {
        List<Object> returnList = new ArrayList<>();
        returnList.addAll(ObjectPlus.getExtent(Car.class));
        returnList.addAll(ObjectPlus.getExtent(Motorcycle.class));
        return returnList;
    }

    public Set<DriveType> getDriveTypes() {
        if (driveTypes == null) {
            return null;
        }
        return Collections.unmodifiableSet(driveTypes);
    }

    public boolean isHybrid() {
        return isHybrid;
    }

    public Integer getBatteryCapacity() {
        return batteryCapacity;
    }

    public FuelType getFuelType() {
        return fuelType;
    }
}
