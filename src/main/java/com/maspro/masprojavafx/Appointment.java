package com.maspro.masprojavafx;

import com.maspro.masprojavafx.Exceptions.InvalidInputException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Appointment extends ObjectPlus implements Serializable {

    private Car car; // xor motorcycle
    private Motorcycle motorcycle; // xor car
    private Customer customer;
    private List<ShopWorker> technicians = new ArrayList<>(); // technicians assigned to the appointment
    private Map<String, Service> services = new HashMap<>(); // services to be done during the appointment
    private LocalDateTime appointmentDate; // date of the appointment
    private Payment payment; // payment for appointment added when customer wishes to pay

    public Appointment(List<Service> services, String registration, List<ShopWorker> technicians, LocalDateTime appointmentDate) throws InvalidInputException {
        super();
        try {
            if (services == null) {
                throw new InvalidInputException("invalid services");
            }
            for (Service s : services) {
                if (s == null) {
                    throw new InvalidInputException("invalid services");
                }
                createServiceRelation(s);
            }
            setVehicleByRegistration(registration);
            setCustomerRelation(car.getOwner());
            setAppointmentDate(appointmentDate);
            setTechnicians(technicians);
        } catch (Exception e) {
            removeThisFromClassExtent();
            throw e;
        }
    }

    private void setVehicleByRegistration(String registration) throws InvalidInputException {
        Motorcycle tempM = Motorcycle.getMotorcycleByRegistration(registration);
        Car tempC = Car.getCarByRegistration(registration);
        if (tempM == null && tempC == null) {
            throw new InvalidInputException("vehicle not found");
        }
        setMotorcycle(tempM);
        setCar(tempC);
    }

    public LocalDateTime getAppointmentDate() {
        return appointmentDate;
    }

    public LocalDateTime getAppointmentPessimisticEnd() {
        return appointmentDate.plusMinutes(getTotalTime()[1]);
    }

    private void setAppointmentDate(LocalDateTime appointmentDate) throws InvalidInputException {
        if (appointmentDate == null) {
            throw new InvalidInputException("invalid appointmentDate");
        }
        if (appointmentDate.isBefore(LocalDateTime.now())) {
            throw new InvalidInputException("date before today");
        }
        this.appointmentDate = appointmentDate;
    }

    public double getTotalCost() {
        double temp = 0;
        for (Service s : services.values()) {
            temp += s.getPrice();
        }
        return temp;
    } // sums up all costs of services in the appointment

    public int[] getTotalTime() {
        int tempLow = 0;
        int tempHigh = 0;
        for (Service s : services.values()) {
            tempLow += s.getTime()[0];
            tempHigh += s.getTime()[1];
        }
        return new int[]{tempLow, tempHigh};
    } // sums up low-end and high-end time individually

    public double getTotalTimeEstimate() {
        return (getTotalTime()[0] + getTotalTime()[1]) / 2;
    } // average time it will take to complete this appointment

    public AppointmentState getAppointmentState() {
        if (appointmentDate.isBefore(LocalDateTime.now())) {
            return AppointmentState.PLANNED;
        } else if (appointmentDate.isAfter(LocalDateTime.now()) && LocalDateTime.now().isBefore(appointmentDate.plusMinutes(getTotalTime()[1]))) {
            return AppointmentState.IN_PROGRESS;
        } else {
            return AppointmentState.COMPLETED;
        }
    } // derives appointment's status based on its date

    public Collection<Service> getServices() {
        if (services == null) {
            return null;
        }
        return Collections.unmodifiableCollection(services.values());
    }

    public Service getServiceByName(String name) {
        if (services == null) {
            return null;
        }
        return services.get(name);
    } // get a service from the assigned ones by its name

    protected void addService(Service service) throws InvalidInputException {
        if (payment != null) {
            throw new InvalidInputException("appointment closed");
        }
        if (service == null) {
            throw new InvalidInputException("invalid service");
        }
        if (services == null) {
            throw new InvalidInputException("services are null");
        }
        if (!services.containsValue(service)) {
            services.put(service.getName(), service);
        } else throw new InvalidInputException("service already exists");
    } // helper method for adding services

    private void removeService(Service service) throws InvalidInputException {
        if (payment != null) {
            throw new InvalidInputException("appointment closed");
        }
        if (service == null) {
            throw new InvalidInputException("invalid service");
        }
        if (services == null) {
            throw new InvalidInputException("services are null");
        }
        if (services.containsValue(service)) {
            services.remove(service.getName());
        } else throw new InvalidInputException("could not find service");
    } // helper method for removing services

    public void createServiceRelation(Service service) throws InvalidInputException {
        if (service == null) {
            throw new InvalidInputException("invalid service");
        }
        if (services.containsValue(service)) {
            throw new InvalidInputException("this relation already exists");
        }
        if (service.getAppointments().contains(this)) {
            throw new InvalidInputException("this relation already exists");
        }
        service.addAppointment(this);
        addService(service);
    } // creates connection between service and this appointment

    public void removeServiceRelation(Service service) throws InvalidInputException {
        if (service == null) {
            throw new InvalidInputException("invalid service");
        }
        if (services.containsValue(service)) {
            try {
                service.removeAppointment(this);
                removeService(service);
            } catch (InvalidInputException e) {
                if (!service.getAppointments().contains(this)) {
                    service.addAppointment(this);
                }
                if (!services.containsValue(service)) {
                    services.put(service.getName(), service);
                }
                throw e;
            }
        } else throw new InvalidInputException("no relation");
    }// removes connection between service and this appointment

    public Payment getPayment() {
        return payment;
    } //returns this payment object

    public Customer getCustomer() {
        return customer;
    } //returns this customer object

    private void setCustomer(Customer customer) {
        this.customer = customer;
    }

    private void setCustomerRelation(Customer customer) throws InvalidInputException {
        if (customer != null) {
            customer.addAppointment(this);
            this.customer = customer;
        } else {
            throw new InvalidInputException("invalid customer");
        }
    } // helper method for setting customer

    public Motorcycle getMotorcycle() {
        return motorcycle;
    } //returns this motorcycle object

    private void setMotorcycle(Motorcycle motorcycle) {
        this.motorcycle = motorcycle;
    } // sets the motorcycle

    public Car getCar() {
        return car;
    } //returns this car object

    private void setCar(Car car) {
        this.car = car;
    } // sets the car

    public List<ShopWorker> getTechnicians() {
        if (technicians == null) {
            return null;
        }
        return Collections.unmodifiableList(technicians);
    } // returns list of technicians assigned to this appointment

    private void setTechnicians(List<ShopWorker> technicians) throws InvalidInputException {
        if (technicians == null) {
            throw new InvalidInputException("invalid technicians");
        }
        if (technicians.size() > 4) {
            throw new InvalidInputException("only 4 technicians per assignment");
        }
        for (ShopWorker t : technicians) {
            if (t == null) {
                throw new InvalidInputException("invalid technicians");
            }
            if (t.getRole() != Role.TECHNICIAN) {
                throw new InvalidInputException("invalid role");
            }
            for (Appointment a : t.getAppointments()) {
                if (a.getAppointmentState() == AppointmentState.COMPLETED) {
                    continue;
                }
                if (a.getAppointmentPessimisticEnd().isBefore(getAppointmentDate())) {
                    continue;
                }
                if (a.getAppointmentDate().isAfter(getAppointmentPessimisticEnd())) {
                    continue;
                }
                if (a.getAppointmentDate().isBefore(getAppointmentDate()) && a.getAppointmentPessimisticEnd().isAfter(getAppointmentDate())) {
                    throw new InvalidInputException("technician is busy");
                }
                if (a.getAppointmentDate().isBefore(getAppointmentPessimisticEnd()) && a.getAppointmentPessimisticEnd().isAfter(getAppointmentPessimisticEnd())) {
                    throw new InvalidInputException("technician is busy");
                }
                if (getAppointmentDate().isBefore(a.getAppointmentDate()) && getAppointmentPessimisticEnd().isAfter(a.getAppointmentDate())) {
                    throw new InvalidInputException("technician is busy");
                }
            }
        }
        for (ShopWorker t : technicians) {
            createTechnicianRelation(t);
        }
    } // assigns technicians to the appointment from a list

    public void createTechnicianRelation(ShopWorker technician) throws InvalidInputException {
        if (technician == null) {
            throw new InvalidInputException("invalid technician");
        }
        if (technician.getRole() != Role.TECHNICIAN) {
            throw new InvalidInputException("invalid role");
        }
        if (technicians.contains(technician) || technician.getAppointments().contains(this)) {
            throw new InvalidInputException("invalid technician");
        }
        if (technicians == null) {
            technicians = new ArrayList<>();
        }
        technicians.add(technician);
        technician.addAppointment(this);
    } // creates connection to technician

    protected void setPayment(Payment payment) throws InvalidInputException {//no validation as it's protected and can be set to null
        this.payment = payment;
    } // sets the payment

    protected void removePayment() throws InvalidInputException {
        if (payment == null) {
            throw new InvalidInputException("payment does not exist");
        }
        setPayment(null);
    } // removes the payment

    public void createPaymentRelation(PaymentType type) throws InvalidInputException {
        if (type != null) {
            new Payment(type, this);
        } else throw new InvalidInputException("invalid payment type");
    } // creates connection between payment and this appointment

    public void removePaymentRelation() throws InvalidInputException {
        if (payment != null){
            if (payment.getAppointment() == this) {
                payment.removeAppointmentRelation();
            } else throw new InvalidInputException("could not remove relation appointment-payment");
        }
    } // removes connection between payment and this appointment

    protected void removeCustomerRelation() {
        customer.removeAppointment(this);
        setCustomer(null);
    }

    protected void removeTechnicianRelation(ShopWorker technician) throws InvalidInputException {
        if (technician == null) {
            throw new InvalidInputException("invalid technician");
        }
        if (technician.getRole() != Role.TECHNICIAN) {
            throw new InvalidInputException("invalid role");
        }
        technician.removeAppointment(this);
        technicians.remove(technician);
    }

    public void deleteAppointment() throws InvalidInputException {
        for (Service s : services.values()) {
            removeServiceRelation(s);
        }
        List<ShopWorker> tempTechnicians = List.copyOf(technicians);
        for (ShopWorker t : tempTechnicians) {
            removeTechnicianRelation(t);
        }
        removeCustomerRelation();
        removePaymentRelation();
        removeThisFromClassExtent();
    } // removes this object from extent
}
