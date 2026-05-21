package main.java.com.airtribe.meditrack.util;

import main.java.com.airtribe.meditrack.constants.Constants;

/**
 * Singleton class for generating unique IDs for entities.
 * Provides ID generation for Patients, Doctors, Appointments, and Bills.
 *
 * Design Pattern: Singleton (eager initialization)
 * Thread-safety: Uses eager initialization to ensure thread-safe singleton
 */
public class IdGenerator {
    // Singleton instance created eagerly
    private static final IdGenerator instance = new IdGenerator();

    // ID counters for each entity type
    private int patientIdCounter;
    private int doctorIdCounter;
    private int appointmentIdCounter;
    private int billIdCounter;

    /**
     * Private constructor - prevents external instantiation
     * Initializes ID counters with starting values from Constants
     */
    private IdGenerator() {
        this.patientIdCounter = Constants.PATIENT_ID_START;
        this.doctorIdCounter = Constants.DOCTOR_ID_START;
        this.appointmentIdCounter = Constants.APPOINTMENT_ID_START;
        this.billIdCounter = Constants.BILL_ID_START;
    }

    /**
     * Gets the singleton instance of IdGenerator.
     *
     * @return the singleton IdGenerator instance
     */
    public static IdGenerator getInstance() {
        return instance;
    }

    /**
     * Generates the next unique Patient ID.
     * IDs start from Constants.PATIENT_ID_START (1001) and increment.
     *
     * @return the next unique patient ID
     */
    public synchronized int generatePatientId() {
        return patientIdCounter++;
    }

    /**
     * Generates the next unique Doctor ID.
     * IDs start from Constants.DOCTOR_ID_START (2001) and increment.
     *
     * @return the next unique doctor ID
     */
    public synchronized int generateDoctorId() {
        return doctorIdCounter++;
    }

    /**
     * Generates the next unique Appointment ID.
     * IDs start from Constants.APPOINTMENT_ID_START (3001) and increment.
     *
     * @return the next unique appointment ID
     */
    public synchronized int generateAppointmentId() {
        return appointmentIdCounter++;
    }

    /**
     * Generates the next unique Bill ID.
     * IDs start from Constants.BILL_ID_START (4001) and increment.
     *
     * @return the next unique bill ID
     */
    public synchronized int generateBillId() {
        return billIdCounter++;
    }

    /**
     * Resets all ID counters to their starting values.
     * Useful for testing purposes only.
     */
    public synchronized void reset() {
        this.patientIdCounter = Constants.PATIENT_ID_START;
        this.doctorIdCounter = Constants.DOCTOR_ID_START;
        this.appointmentIdCounter = Constants.APPOINTMENT_ID_START;
        this.billIdCounter = Constants.BILL_ID_START;
    }

    /**
     * Gets the current patient ID counter value without incrementing.
     *
     * @return the current patient ID counter
     */
    public synchronized int getCurrentPatientIdCounter() {
        return patientIdCounter;
    }

    /**
     * Gets the current doctor ID counter value without incrementing.
     *
     * @return the current doctor ID counter
     */
    public synchronized int getCurrentDoctorIdCounter() {
        return doctorIdCounter;
    }

    /**
     * Gets the current appointment ID counter value without incrementing.
     *
     * @return the current appointment ID counter
     */
    public synchronized int getCurrentAppointmentIdCounter() {
        return appointmentIdCounter;
    }

    /**
     * Gets the current bill ID counter value without incrementing.
     *
     * @return the current bill ID counter
     */
    public synchronized int getCurrentBillIdCounter() {
        return billIdCounter;
    }

    /**
     * Manually sets the patient ID counter (useful when loading from persistent storage).
     *
     * @param id the ID to set the counter to
     */
    public synchronized void setPatientIdCounter(int id) {
        if (id >= Constants.PATIENT_ID_START) {
            this.patientIdCounter = id + 1;
        }
    }

    /**
     * Manually sets the doctor ID counter (useful when loading from persistent storage).
     *
     * @param id the ID to set the counter to
     */
    public synchronized void setDoctorIdCounter(int id) {
        if (id >= Constants.DOCTOR_ID_START) {
            this.doctorIdCounter = id + 1;
        }
    }

    /**
     * Manually sets the appointment ID counter (useful when loading from persistent storage).
     *
     * @param id the ID to set the counter to
     */
    public synchronized void setAppointmentIdCounter(int id) {
        if (id >= Constants.APPOINTMENT_ID_START) {
            this.appointmentIdCounter = id + 1;
        }
    }

    /**
     * Manually sets the bill ID counter (useful when loading from persistent storage).
     *
     * @param id the ID to set the counter to
     */
    public synchronized void setBillIdCounter(int id) {
        if (id >= Constants.BILL_ID_START) {
            this.billIdCounter = id + 1;
        }
    }

    @Override
    public String toString() {
        return "IdGenerator{" +
                "patientIdCounter=" + patientIdCounter +
                ", doctorIdCounter=" + doctorIdCounter +
                ", appointmentIdCounter=" + appointmentIdCounter +
                ", billIdCounter=" + billIdCounter +
                '}';
    }
}

