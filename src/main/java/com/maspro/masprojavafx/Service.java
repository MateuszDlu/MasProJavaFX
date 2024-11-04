package com.maspro.masprojavafx;

import com.maspro.masprojavafx.Exceptions.InvalidInputException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Service extends ObjectPlus implements Serializable {
    private List<Appointment> appointments;
    private String name; // name of service
    private double price; // price of service
    private int[] time; // time to finish the service. First element is optimistic, second is pessimistic
    private static List<Service> allServices; // collection of all services

    public Service(String name, double price, int[] time) throws InvalidInputException {
        super();
        try {
            setName(name);
            setPrice(price);
            setTime(time);
            addService(this);
        } catch (Exception e) {
            removeThisFromClassExtent();
            throw e;
        }
    }

    public List<Appointment> getAppointments() {
        if (appointments == null) {
            return new ArrayList<>();
        }
        return Collections.unmodifiableList(appointments);
    }

    protected void addAppointment(Appointment appointment) throws InvalidInputException {
        if (appointments == null) {
            appointments = new ArrayList<>();
        }
        if (appointment == null) {
            throw new InvalidInputException("invalid appointment");
        }
        appointments.add(appointment);
    }

    protected void removeAppointment(Appointment appointment) throws InvalidInputException {
        if (appointments == null) {
            throw new InvalidInputException("no appointments");
        }
        if (appointment == null) {
            throw new InvalidInputException("invalid appointment");
        }
        if (!appointments.contains(appointment)) {
            throw new InvalidInputException("could not find appointment");
        }
        appointments.remove(appointment);
    }

    public void createAppointmentRelation(Appointment appointment) throws InvalidInputException {
        if (appointments == null) {
            appointments = new ArrayList<>();
        }
        if (appointment != null) {
            throw new InvalidInputException("invalid appointment");
        }
        appointment.createServiceRelation(this);
    }

    public void removeAppointmentRelation(Appointment appointment) throws InvalidInputException {
        if (appointments == null) {
            throw new InvalidInputException("no appointments");
        }
        if (appointment == null) {
            throw new InvalidInputException("invalid appointment");
        }
        appointment.removeServiceRelation(this);
    }


    private void addService(Service service) throws InvalidInputException {
        if (service != null) {
            boolean flag = false;
            if (allServices == null) {
                allServices = new ArrayList<>();
            }
            for (Service s : allServices) {
                if (Objects.equals(s.getName(), service.getName())) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                allServices.add(service);
            } else throw new InvalidInputException("this service already exists");
        } else throw new InvalidInputException("invalid service");
    }

    public static List<Service> getAllServices() {
        if (allServices == null) {
            return null;
        }
        return Collections.unmodifiableList(allServices);
    }

    private static void setAllServices(List<Service> allServices) throws InvalidInputException {
        if (allServices != null) {
            Service.allServices = allServices;
        } else throw new InvalidInputException("invalid services");

    }

    public int[] getTime() {
        return time;
    }

    protected void setTime(int[] time) throws InvalidInputException {
        if (time.length == 2 && time[0] != 0 && time[1] != 0) {
            if (time[0] < time[1]) {
                this.time = time;
            }
        } else {
            throw new InvalidInputException("invalid time");
        }
    }

    protected void setName(String name) throws InvalidInputException {
        if (name != null) {
            if (!name.equals("")) {
                this.name = name;
            } else {
                throw new InvalidInputException("invalid name");
            }
        } else {
            throw new InvalidInputException("invalid name");
        }
    }

    public String getName() {
        return name;
    }

    protected void setPrice(double price) throws InvalidInputException {
        if (price != 0) {
            this.price = price;
        } else {
            throw new InvalidInputException("invalid price");
        }

    }

    public double getPrice() {
        return price;
    }

    public void deleteService() throws InvalidInputException {
        if (appointments != null){
            for (Appointment a : appointments) {
                removeAppointmentRelation(a);
            }
        }
        removeThisFromClassExtent();
    }
}
