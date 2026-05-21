package main.java.com.airtribe.meditrack.exception;

/**
 * Custom exception thrown when invalid data is provided to the system.
 * Used for validation errors, invalid input, and data constraint violations.
 */
public class InvalidDataException extends Exception {

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDataException(Throwable cause) {
        super(cause);
    }
}

