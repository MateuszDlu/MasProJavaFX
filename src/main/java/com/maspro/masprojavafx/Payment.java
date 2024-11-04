package com.maspro.masprojavafx;

import com.maspro.masprojavafx.Exceptions.InvalidInputException;

import java.io.Serializable;

public class Payment extends ObjectPlus implements Serializable {
    private PaymentType type;
    private final double total;
    private Appointment appointment;

    public Payment(PaymentType type, Appointment appointment) throws InvalidInputException {
        super();
        try {
            setType(type);
            createAppointmentRelation(appointment);
            total = getAppointmentsTotalCost();
        } catch (Exception e) {
            removeThisFromClassExtent();
            throw e;
        }

    }

    public double getTotal() {
        return total;
    }

    private double getAppointmentsTotalCost() throws InvalidInputException {
        if (this.appointment != null) {
            return this.appointment.getTotalCost();
        } else {
            throw new InvalidInputException("appointment does not exist");
        }
    }

    public PaymentType getType() {
        return type;
    }

    private void setType(PaymentType type) throws InvalidInputException {
        if (type != null) {
            this.type = type;
        } else throw new InvalidInputException("invalid type");
    }

    public Appointment getAppointment() {
        return appointment;
    }

    protected void setAppointment(Appointment appointment) throws InvalidInputException {
        if (appointment != null) {
            if (appointment.getPayment() == null) {
                this.appointment = appointment;
            } else throw new InvalidInputException("appointment already has payment");
        } else throw new InvalidInputException("invalid appointment");
    }

    protected void removeAppointment() throws InvalidInputException {
        appointment.removePayment();
        this.appointment = null;
        this.removeThisFromClassExtent();
    }

    private void createAppointmentRelation(Appointment appointment) throws InvalidInputException { // private used only by constructor
        if (appointment != null) {
            if (appointment.getPayment() == null && this.appointment == null) {
                setAppointment(appointment);
                appointment.setPayment(this);
            } else throw new InvalidInputException("could not create relation payment-appointment");
        } else throw new InvalidInputException("invalid appointment");
    }

    public void removeAppointmentRelation() throws InvalidInputException {
        removeAppointment();
    }

}
