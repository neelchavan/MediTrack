package main.java.com.airtribe.meditrack.constants;

/**
 * Application-wide constants for MediTrack system.
 * Contains all configuration values, tax rates, fees, and file paths.
 */
public class Constants {
    // Private constructor to prevent instantiation
    private Constants() {
        throw new AssertionError("Cannot instantiate Constants class");
    }

    // ===== Tax & Billing Constants =====
    /** GST/Tax rate applied to bills (18%) */
    public static final double DEFAULT_TAX_RATE = 0.18;

    /** Default consultation fee for doctors */
    public static final double CONSULTATION_FEE_DEFAULT = 500.0;

    // ===== Patient Constants =====
    /** Maximum valid age for a patient */
    public static final int MAX_PATIENT_AGE = 120;

    /** Minimum valid age for a patient */
    public static final int MIN_PATIENT_AGE = 0;

    // ===== Appointment Constants =====
    /** Duration of each appointment slot in minutes */
    public static final int APPOINTMENT_SLOT_DURATION_MINUTES = 30;

    /** Number of days ahead to generate appointment slots (default: 7 days) */
    public static final int DEFAULT_APPOINTMENT_DAYS_AHEAD = 7;

    /** Appointment start time (9:00 AM) in HH:mm format */
    public static final String APPOINTMENT_START_TIME = "09:00";

    /** Appointment end time (6:00 PM) in HH:mm format */
    public static final String APPOINTMENT_END_TIME = "18:00";

    /** DateTime format pattern: dd/MM/yyyy HH:mm */
    public static final String DATETIME_FORMAT_PATTERN = "dd/MM/yyyy HH:mm";

    // ===== Validation Constants =====
    /** Date format pattern: dd/MM/yyyy */
    public static final String DATE_FORMAT_PATTERN = "dd/MM/yyyy";

    /** Time format pattern: HH:mm */
    public static final String TIME_FORMAT_PATTERN = "HH:mm";

    // ===== Validation Constants =====
    /** Minimum length for a person's name */
    public static final int MIN_NAME_LENGTH = 2;

    /** Maximum length for a person's name */
    public static final int MAX_NAME_LENGTH = 100;

    /** Minimum length for phone number */
    public static final int MIN_PHONE_LENGTH = 10;

    /** Maximum length for phone number */
    public static final int MAX_PHONE_LENGTH = 15;

    /** Regular expression for valid email */
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    /** Regular expression for valid phone number (digits only, 10-15 chars) */
    public static final String PHONE_REGEX = "^\\d{10,15}$";

    // ===== Message Constants =====
    /** Success message prefix */
    public static final String SUCCESS_MESSAGE = "✓ ";

    /** Error message prefix */
    public static final String ERROR_MESSAGE = "✗ ";

    /** Warning message prefix */
    public static final String WARNING_MESSAGE = "⚠ ";

    // ===== ID Generation Constants =====
    /** Starting ID for patients */
    public static final int PATIENT_ID_START = 1001;

    /** Starting ID for doctors */
    public static final int DOCTOR_ID_START = 2001;

    /** Starting ID for appointments */
    public static final int APPOINTMENT_ID_START = 3001;

    /** Starting ID for bills */
    public static final int BILL_ID_START = 4001;
}

