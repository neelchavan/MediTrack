package main.java.com.airtribe.meditrack.entity;

import main.java.com.airtribe.meditrack.interfaces.Payable;

/**
 * Bill entity representing a billing record for an appointment.
 * Implements Payable interface to support payment tracking.
 * Links billing directly to appointments for accurate tracking.
 */
public class Bill implements Payable {
    private int id;
    private Appointment appointment;
    private double amount;
    private String status; // PAID or PENDING

    public Bill(int id, Appointment appointment, double amount, String status) {
        this.id = id;
        this.appointment = appointment;
        this.amount = amount;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public double calculateTotalAmount() {
        return amount;
    }

    @Override
    public boolean getPaymentStatus() {
        return "PAID".equalsIgnoreCase(status);
    }

    @Override
    public void markAsPaid() {
        this.status = "PAID";
    }

    @Override
    public String getPaymentDetails() {
        return String.format("Bill #%d | Appointment: %d | Amount: ₹%.2f | Status: %s",
                id, appointment.getAppointmentId(), amount, status);
    }

    @Override
    public String toString() {
        return getPaymentDetails();
    }
}