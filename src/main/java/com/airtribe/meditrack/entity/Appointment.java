package main.java.com.airtribe.meditrack.entity;

import java.time.LocalDate;
import java.time.LocalTime;


class Appointment
{


    private int appointmentId;
    private Patient patient;
    private Doctor doctor;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String appointmentStatus;


    // Constructor

    public Appointment(int appointmentId, Patient patient, Doctor doctor, LocalDate appointmentDate,    LocalTime appointmentTime, String appointmentStatus) {
        this.appointmentId = appointmentId;
        this.patient = patient;
        this.doctor = doctor;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.appointmentStatus = appointmentStatus;
    }

    // Getter

    public int getAppointmentId() {
        return appointmentId;
    }


    public Patient getPatient() {
        return patient;
    }


    public Doctor getDoctor() {
        return doctor;
    }


    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }


    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }


    public String getAppointmentStatus() {
        return appointmentStatus;
    }


    // Setter

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    
}

