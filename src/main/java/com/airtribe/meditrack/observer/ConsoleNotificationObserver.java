package main.java.com.airtribe.meditrack.observer;

import main.java.com.airtribe.meditrack.entity.Appointment;

/**
 * ConsoleNotificationObserver - Concrete Observer Implementation
 *
 * Displays appointment notifications on the console.
 * This is a concrete implementation of the Observer pattern.
 */
public class ConsoleNotificationObserver implements AppointmentObserver {

    @Override
    public void onAppointmentBooked(Appointment appointment) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📬 APPOINTMENT BOOKED NOTIFICATION");
        System.out.println("=".repeat(70));
        System.out.println("Patient: " + appointment.getPatient().getName());
        System.out.println("Doctor: " + appointment.getDoctor().getName());
        System.out.println("Date: " + appointment.getAppointmentDate());
        System.out.println("Time: " + appointment.getAppointmentTime());
        System.out.println("Status: " + appointment.getAppointmentStatus());
        System.out.println("✅ Confirmation email sent to patient");
        System.out.println("=".repeat(70) + "\n");
    }

    @Override
    public void onAppointmentCancelled(Appointment appointment) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("❌ APPOINTMENT CANCELLED NOTIFICATION");
        System.out.println("=".repeat(70));
        System.out.println("Patient: " + appointment.getPatient().getName());
        System.out.println("Doctor: " + appointment.getDoctor().getName());
        System.out.println("Appointment Date: " + appointment.getAppointmentDate());
        System.out.println("Cancellation notification sent to both parties");
        System.out.println("=".repeat(70) + "\n");
    }

    @Override
    public void onAppointmentRescheduled(Appointment oldAppointment, Appointment newAppointment) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🔄 APPOINTMENT RESCHEDULED NOTIFICATION");
        System.out.println("=".repeat(70));
        System.out.println("Patient: " + newAppointment.getPatient().getName());
        System.out.println("Doctor: " + newAppointment.getDoctor().getName());
        System.out.println("Old Date/Time: " + oldAppointment.getAppointmentDate() + " " + oldAppointment.getAppointmentTime());
        System.out.println("New Date/Time: " + newAppointment.getAppointmentDate() + " " + newAppointment.getAppointmentTime());
        System.out.println("✅ Rescheduling confirmation sent to patient and doctor");
        System.out.println("=".repeat(70) + "\n");
    }

    @Override
    public void onAppointmentReminder(Appointment appointment, int daysUntil) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("⏰ APPOINTMENT REMINDER NOTIFICATION");
        System.out.println("=".repeat(70));
        System.out.println("Patient: " + appointment.getPatient().getName());
        System.out.println("Doctor: " + appointment.getDoctor().getName());
        System.out.println("Appointment Date: " + appointment.getAppointmentDate());
        System.out.println("Time: " + appointment.getAppointmentTime());
        System.out.println("⏳ Reminder: Your appointment is " + daysUntil + " days away");
        System.out.println("✅ Reminder notification sent to patient");
        System.out.println("=".repeat(70) + "\n");
    }
}

