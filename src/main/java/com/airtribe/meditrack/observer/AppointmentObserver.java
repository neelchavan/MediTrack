package main.java.com.airtribe.meditrack.observer;

import main.java.com.airtribe.meditrack.entity.Appointment;

/**
 * AppointmentObserver Interface
 *
 * Defines the contract for all observers interested in appointment events.
 * Uses the Observer Pattern for loose coupling between appointments and notifications.
 */
public interface AppointmentObserver {

    void onAppointmentBooked(Appointment appointment);

    void onAppointmentCancelled(Appointment appointment);

    void onAppointmentRescheduled(Appointment oldAppointment, Appointment newAppointment);

    void onAppointmentReminder(Appointment appointment, int daysUntil);
}

