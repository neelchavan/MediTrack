package main.java.com.airtribe.meditrack.observer;

import main.java.com.airtribe.meditrack.entity.Appointment;
import java.util.ArrayList;
import java.util.List;

/**
 * AppointmentNotificationManager - Observable/Subject in Observer Pattern
 *
 * Manages registered observers and notifies them of appointment events.
 * This is the Subject in the Observer pattern.
 *
 * Benefits:
 * - Loose coupling between Appointment and Notifications
 * - Easy to add new observers without modifying existing code
 * - Multiple observers can react to same event
 */
public class AppointmentNotificationManager {
    private final List<AppointmentObserver> observers = new ArrayList<>();

    public void addObserver(AppointmentObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(AppointmentObserver observer) {
        observers.remove(observer);
    }

    public void clearObservers() {
        observers.clear();
    }

    public void notifyAppointmentBooked(Appointment appointment) {
        for (AppointmentObserver observer : observers) {
            observer.onAppointmentBooked(appointment);
        }
    }

    public void notifyAppointmentCancelled(Appointment appointment) {
        for (AppointmentObserver observer : observers) {
            observer.onAppointmentCancelled(appointment);
        }
    }

    public void notifyAppointmentRescheduled(Appointment oldAppointment, Appointment newAppointment) {
        for (AppointmentObserver observer : observers) {
            observer.onAppointmentRescheduled(oldAppointment, newAppointment);
        }
    }

    public void notifyAppointmentReminder(Appointment appointment, int daysUntil) {
        for (AppointmentObserver observer : observers) {
            observer.onAppointmentReminder(appointment, daysUntil);
        }
    }

    public int getObserverCount() {
        return observers.size();
    }
}

