package main.java.com.airtribe.meditrack.service;

import main.java.com.airtribe.meditrack.entity.Appointment;
import main.java.com.airtribe.meditrack.entity.Patient;
import main.java.com.airtribe.meditrack.entity.Doctor;
import main.java.com.airtribe.meditrack.enums.AppointmentStatus;
import main.java.com.airtribe.meditrack.exception.AppointmentNotFoundException;
import main.java.com.airtribe.meditrack.exception.InvalidDataException;
import main.java.com.airtribe.meditrack.interfaces.Searchable;
import main.java.com.airtribe.meditrack.observer.AppointmentNotificationManager;
import main.java.com.airtribe.meditrack.util.DataStore;
import main.java.com.airtribe.meditrack.util.DateUtil;
import main.java.com.airtribe.meditrack.util.IdGenerator;
import main.java.com.airtribe.meditrack.util.Validator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing Appointment entities.
 * Provides CRUD operations, scheduling, cancellation, and search functionality for appointments.
 * Handles appointment conflicts and ensures valid scheduling.
 * Implements Searchable interface for polymorphic search operations.
 */
public class AppointmentService implements Searchable<Appointment> {
    private final DataStore<Appointment> appointmentStore;
    private final IdGenerator idGenerator;
    private final AppointmentNotificationManager notificationManager;

    /**
     * Initializes AppointmentService with a new data store and singleton IdGenerator.
     */
    public AppointmentService() {
        this.appointmentStore = new DataStore<>();
        this.idGenerator = IdGenerator.getInstance();
        this.notificationManager = new AppointmentNotificationManager();
    }

    public AppointmentNotificationManager getNotificationManager() {
        return notificationManager;
    }

    public Appointment bookAppointment(Patient patient, Doctor doctor,
                                       LocalDate appointmentDate, LocalTime appointmentTime)
            throws InvalidDataException {
        // Validate inputs
        if (patient == null) {
            throw new InvalidDataException("Patient cannot be null");
        }
        if (doctor == null) {
            throw new InvalidDataException("Doctor cannot be null");
        }

        Validator.validateAppointmentDate(appointmentDate);
        Validator.validateAppointmentTime(appointmentTime);

        // Check for double-booking (same doctor, same date and time)
        if (isTimeSlotBooked(doctor.getId(), appointmentDate, appointmentTime)) {
            throw new InvalidDataException("Time slot is already booked for this doctor. " +
                    "Please choose a different time.");
        }

        // Generate unique ID
        int appointmentId = idGenerator.generateAppointmentId();

        // Create appointment with CONFIRMED status
        Appointment appointment = new Appointment(appointmentId, patient, doctor,
                appointmentDate, appointmentTime, AppointmentStatus.CONFIRMED.toString());
        appointmentStore.add(appointmentId, appointment);

        // Notify observers of booking
        notificationManager.notifyAppointmentBooked(appointment);

        return appointment;
    }

    public Appointment getAppointmentById(int appointmentId) throws AppointmentNotFoundException {
        Appointment appointment = appointmentStore.get(appointmentId);
        if (appointment == null) {
            throw new AppointmentNotFoundException("Appointment with ID " + appointmentId + " not found");
        }
        return appointment;
    }

    @Override
    public Appointment searchById(int id) {
        return appointmentStore.get(id);
    }

    @Override
    public List<Appointment> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String searchName = name.toLowerCase().trim();
        return appointmentStore.getAll().stream()
                .filter(apt -> apt.getPatient().getName().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
    }

    public List<Appointment> searchByPatientId(int patientId) {
        return appointmentStore.getAll().stream()
                .filter(apt -> apt.getPatient().getId() == patientId)
                .collect(Collectors.toList());
    }

    public List<Appointment> searchByDoctorId(int doctorId) {
        return appointmentStore.getAll().stream()
                .filter(apt -> apt.getDoctor().getId() == doctorId)
                .collect(Collectors.toList());
    }

    public List<Appointment> searchByStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            return new ArrayList<>();
        }

        try {
            Validator.validateAppointmentStatus(status);
        } catch (InvalidDataException e) {
            return new ArrayList<>();
        }

        return appointmentStore.getAll().stream()
                .filter(apt -> apt.getAppointmentStatus().equals(status))
                .collect(Collectors.toList());
    }

    public List<Appointment> searchByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return new ArrayList<>();
        }

        return appointmentStore.getAll().stream()
                .filter(apt -> {
                    LocalDate aptDate = apt.getAppointmentDate();
                    return !aptDate.isBefore(startDate) && !aptDate.isAfter(endDate);
                })
                .collect(Collectors.toList());
    }

    public Appointment cancelAppointment(int appointmentId) throws AppointmentNotFoundException {
        Appointment appointment = getAppointmentById(appointmentId);

        if (AppointmentStatus.CANCELLED.toString().equals(appointment.getAppointmentStatus())) {
            throw new AppointmentNotFoundException("Appointment already cancelled");
        }

        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED.toString());
        appointmentStore.update(appointmentId, appointment);

        // Notify observers of cancellation
        notificationManager.notifyAppointmentCancelled(appointment);

        return appointment;
    }

    public Appointment rescheduleAppointment(int appointmentId, LocalDate newDate, LocalTime newTime)
            throws InvalidDataException, AppointmentNotFoundException {
        Appointment appointment = getAppointmentById(appointmentId);

        Validator.validateAppointmentDate(newDate);
        Validator.validateAppointmentTime(newTime);

        // Check for double-booking with new date/time
        if (isTimeSlotBooked(appointment.getDoctor().getId(), newDate, newTime)) {
            throw new InvalidDataException("New time slot is already booked for this doctor");
        }

        // Store old appointment for notification
        Appointment oldAppointment = new Appointment(
            appointment.getAppointmentId(),
            appointment.getPatient(),
            appointment.getDoctor(),
            appointment.getAppointmentDate(),
            appointment.getAppointmentTime(),
            appointment.getAppointmentStatus()
        );

        appointment.setAppointmentDate(newDate);
        appointment.setAppointmentTime(newTime);
        appointmentStore.update(appointmentId, appointment);

        // Notify observers of rescheduling
        notificationManager.notifyAppointmentRescheduled(oldAppointment, appointment);

        return appointment;
    }

    private boolean isTimeSlotBooked(int doctorId, LocalDate date, LocalTime time) {
        return appointmentStore.getAll().stream()
                .anyMatch(apt -> apt.getDoctor().getId() == doctorId &&
                               apt.getAppointmentDate().equals(date) &&
                               apt.getAppointmentTime().equals(time) &&
                               !AppointmentStatus.CANCELLED.toString().equals(apt.getAppointmentStatus()));
    }

    public List<LocalTime> getAvailableTimeSlots(int doctorId, LocalDate date) {
        List<LocalTime> allSlots = DateUtil.generateAppointmentSlots(date);
        List<LocalTime> bookedSlots = appointmentStore.getAll().stream()
                .filter(apt -> apt.getDoctor().getId() == doctorId &&
                              apt.getAppointmentDate().equals(date) &&
                              !AppointmentStatus.CANCELLED.toString().equals(apt.getAppointmentStatus()))
                .map(Appointment::getAppointmentTime)
                .collect(Collectors.toList());

        return allSlots.stream()
                .filter(slot -> !bookedSlots.contains(slot))
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> getAll() {
        return appointmentStore.getAll();
    }

    @Override
    public int getTotalCount() {
        return appointmentStore.size();
    }

    public long getAppointmentCountByStatus(String status) {
        return searchByStatus(status).size();
    }

    public long getDoctorAppointmentCount(int doctorId) {
        return searchByDoctorId(doctorId).size();
    }

    public long getPatientAppointmentCount(int patientId) {
        return searchByPatientId(patientId).size();
    }

}


