package main.java.com.airtribe.meditrack.exception;

/**
 * Custom exception thrown when a requested appointment is not found in the system.
 * Used for appointment lookup failures.
 */
public class AppointmentNotFoundException extends Exception {

    public AppointmentNotFoundException(String message) {
        super(message);
    }

    public AppointmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppointmentNotFoundException(Throwable cause) {
        super(cause);
    }
}

