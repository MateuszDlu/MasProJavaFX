package com.maspro.masprojavafx;

import com.maspro.masprojavafx.Exceptions.InvalidInputException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class Customer extends Person {

    private String email; // email to customer [optional]
    private String phone; // phone to customer [unique]
    private Address address; // address of the customer for important information in form of a letter
    private List<Motorcycle> motorcycles; // owned motorcycles
    private List<Car> cars; // owned cars
    private List<Appointment> appointments; // history of appointments and planned appointments
    private final String phoneRegex = "^\\+?[1-9][0-9]{7,14}$";
    private final String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    public Customer(String name, String surname, String phone, Address address, String email) throws InvalidInputException {
        super(name, surname);
        setPhone(phone);
        setAddress(address);
        setEmail(email);
    }

    public Customer(String name, String surname, String phone, Address address) throws InvalidInputException {
        super(name, surname);
        setPhone(phone);
        setAddress(address);
        this.email = null;
    }


    protected void addMotorcycle(Motorcycle motorcycle) throws InvalidInputException {
        if (motorcycles == null) {
            motorcycles = new ArrayList<>();
        }
        if (motorcycle != null) {
            if (!motorcycles.contains(motorcycle) && motorcycle.getOwner() != this) {
                motorcycles.add(motorcycle);
            } else throw new InvalidInputException("this connection already exists");
        } else throw new InvalidInputException("invalid motorcycle");
    }

    public void createNewMotorcycleRelation(Motorcycle motorcycle) throws InvalidInputException {
        if (motorcycles == null) {
            motorcycles = new ArrayList<>();
        }
        if (motorcycle != null) {
            if (!motorcycles.contains(motorcycle) && motorcycle.getOwner() == null) {
                try {
                    motorcycle.setOwner(this);
                    motorcycles.add(motorcycle);
                } catch (InvalidInputException e) {
                    if (motorcycles.contains(motorcycle)) {
                        motorcycles.remove(motorcycle);
                    }
                    throw e;
                }
            } else throw new InvalidInputException("could not set relation customer-motorcycle");
        } else throw new InvalidInputException("invalid motorcycle");
    }

    public void createOwnedMotorcycleRelation(Motorcycle motorcycle) throws InvalidInputException {
        if (motorcycles == null) {
            motorcycles = new ArrayList<>();
        }
        if (motorcycle != null) {
            if (!motorcycles.contains(motorcycle) && motorcycle.getOwner() != null) {
                try {
                    motorcycle.changeOwnerRelation(this);
                } catch (InvalidInputException e) {
                    if (motorcycles.contains(motorcycle)) {
                        motorcycles.remove(motorcycle);
                    }
                    throw e;
                }
            } else throw new InvalidInputException("could not set relation customer-motorcycle");
        } else throw new InvalidInputException("invalid motorcycle");
    }

    public void removeMotorcycleRelation(Motorcycle motorcycle) throws InvalidInputException {
        if (motorcycle != null) {
            if (motorcycles.contains(motorcycle) && motorcycle.getOwner() == this) {
                motorcycle.removeOwner();
                removeMotorcycle(motorcycle);
            } else throw new InvalidInputException("could not remove relation customer-motorcycle");
        } else throw new InvalidInputException("invalid motorcycle");
    }

    protected void removeMotorcycle(Motorcycle motorcycle) throws InvalidInputException {
        if (motorcycle != null) {
            if (motorcycles.contains(motorcycle)) {
                motorcycles.remove(motorcycle);
            } else throw new InvalidInputException("could not find motorcycle");
        } else throw new InvalidInputException("invalid motorcycle");
    }


    public void addCar(Car car) throws InvalidInputException {
        if (cars == null) {
            cars = new ArrayList<>();
        }
        if (car != null) {
            if (!cars.contains(car) && car.getOwner() != this) {
                cars.add(car);
            } else throw new InvalidInputException("this connection already exists");
        } else throw new InvalidInputException("invalid car");
    }

    public void createNewCarRelation(Car car) throws InvalidInputException {
        if (cars == null) {
            cars = new ArrayList<>();
        }
        if (car != null) {
            if (!cars.contains(car) && car.getOwner() == null) {
                try {
                    car.setOwner(this);
                    cars.add(car);
                } catch (InvalidInputException e) {
                    if (cars.contains(car)) {
                        cars.remove(car);
                    }
                    throw e;
                }
            } else throw new InvalidInputException("could not set relation customer-car");
        } else throw new InvalidInputException("invalid car");
    }

    public void createOwnedCarRelation(Car car) throws InvalidInputException {
        if (cars == null) {
            cars = new ArrayList<>();
        }
        if (car != null) {
            if (!cars.contains(car) && car.getOwner() != null) {
                try {
                    car.changeOwnerRelation(this);
                } catch (InvalidInputException e) {
                    if (cars.contains(car)) {
                        cars.remove(car);
                    }
                }
            } else throw new InvalidInputException("could not set relation customer-car");
        } else throw new InvalidInputException("invalid car");
    }

    public void removeCarRelation(Car car) throws InvalidInputException {
        if (car != null) {
            if (cars.contains(car) && car.getOwner() == this) {
                car.removeOwner();
                removeCar(car);
            } else throw new InvalidInputException("could not remove relation customer-car");
        } else throw new InvalidInputException("invalid car");
    }

    public void removeCar(Car car) throws InvalidInputException {
        if (car != null) {
            if (cars.contains(car)) {
                cars.remove(car);
            } else throw new InvalidInputException("could not find car");
        } else throw new InvalidInputException("invalid car");
    }

    public List<Appointment> getAppointments() {
        if (appointments == null) {
            return null;
        }
        List<Appointment> tempA = new ArrayList<Appointment>(List.copyOf(appointments));
        tempA.sort((a1, a2) -> {
            ZonedDateTime zdt1 = ZonedDateTime.of(a1.getAppointmentDate(), ZoneId.systemDefault());
            ZonedDateTime zdt2 = ZonedDateTime.of(a2.getAppointmentDate(), ZoneId.systemDefault());
            return (int) (zdt2.toInstant().toEpochMilli() - zdt1.toInstant().toEpochMilli());
        });
        return Collections.unmodifiableList(tempA);
    }

    protected void addAppointment(Appointment appointment) throws InvalidInputException {
        if (appointments == null) {
            appointments = new ArrayList<>();
        }
        if (appointment != null && !appointments.contains(appointment) && appointment.getCustomer() == null) {
            if (appointments == null) {
                appointments = new ArrayList<Appointment>();
            }
            appointments.add(appointment);
        } else {
            throw new InvalidInputException("invalid appointment");
        }
    }

    protected void removeAppointment(Appointment appointment) {
        if (appointment != null && appointments.contains(appointment)) {
            appointments.remove(appointment);
        }
    }

    private void removeAppointmentRelation(Appointment appointment) throws InvalidInputException {
        if (appointment == null) {
            throw new InvalidInputException("invalid appointment");
        }
        appointment.removeCustomerRelation();
    }

    public List<Motorcycle> getMotorcycles() {
        if (motorcycles == null){
            return null;
        }
        return Collections.unmodifiableList(motorcycles);
    }

    private void setMotorcycles(List<Motorcycle> motorcycles) {
        this.motorcycles = motorcycles;
    }//shouldn't be overwritten

    public List<Car> getCars() {
        if (cars == null){
            return null;
        }
        return Collections.unmodifiableList(cars);
    }

    private void setCars(List<Car> cars) {
        this.cars = cars;
    }//shouldn't be overwritten

    @Override
    public double appointmentEstimation(Appointment appointment) throws InvalidInputException {
        if (appointment.getCustomer() != this) {
            throw new InvalidInputException("appointment access denied");
        } else {
            return appointment.getTotalCost();
        }
    }

    public String getEmail() {
        return email;
    }

    protected void setEmail(String email) throws InvalidInputException {
        if (email != null) {
            if (!email.equals("")) {
                if (!email.matches(emailRegex)) {
                    throw new InvalidInputException("invalid email");
                } else {
                    this.email = email;
                }
            } else {
                this.email = null;
            }
        } else {
            this.email = null;
        }
    }

    public String getPhone() {
        return phone;
    }

    protected void setPhone(String phone) throws InvalidInputException {
        if (phone == null) {
            throw new InvalidInputException("invalid phone");
        }
        for (Customer c : getExtent(Customer.class)) {
            if (Objects.equals(c.getPhone(), phone)) {
                throw new InvalidInputException("duplicate phone");
            }
        }
        if (!phone.matches(phoneRegex) || phone.equals("")) {
            throw new InvalidInputException("invalid phone");
        } else {
            this.phone = phone;
        }
    }

    public Address getAddress() {
        return address;
    }

    protected void setAddress(Address address) throws InvalidInputException {
        if (address == null) {
            throw new InvalidInputException("invalid address");
        } else {
            this.address = address;
        }
    }

    protected void deleteCustomer() throws InvalidInputException {
        if (appointments != null){
            for (Appointment a : appointments) {
                a.removeThisFromClassExtent();
            }
        }
        if (cars != null){
            for (Car c : cars) {
                removeCarRelation(c);
            }
        }
        if (motorcycles != null){
            for (Motorcycle m : motorcycles) {
                removeMotorcycleRelation(m);
            }
        }
        removeThisFromClassExtent();
    }
}
