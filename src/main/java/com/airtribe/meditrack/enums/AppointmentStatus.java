package main.java.com.airtribe.meditrack.enums;

/**
 * Enum representing the status of an appointment.
 *
 * Status Definitions:
 * - CONFIRMED: Appointment is confirmed and scheduled
 * - CANCELLED: Appointment has been cancelled (soft delete)
 * - PENDING: Appointment is waiting for confirmation
 * - SCHEDULED: Appointment is scheduled and upcoming
 */
public enum AppointmentStatus {
    CONFIRMED("Appointment confirmed"),
    CANCELLED("Appointment cancelled"),
    PENDING("Appointment pending confirmation"),
    SCHEDULED("Appointment scheduled");

    private final String description;

    AppointmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return this == CONFIRMED || this == SCHEDULED;
    }

    public boolean isCancelled() {
        return this == CANCELLED;
    }
}
