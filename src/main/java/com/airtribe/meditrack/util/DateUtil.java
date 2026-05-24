package main.java.com.airtribe.meditrack.util;

import main.java.com.airtribe.meditrack.constants.Constants;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for date and time operations in MediTrack.
 * Handles date formatting, parsing, validation, and appointment slot generation.
 * Uses dd/MM/yyyy format as per requirements.
 */
public class DateUtil {
    private static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_PATTERN);
    private static final DateTimeFormatter TIME_FORMATTER =
        DateTimeFormatter.ofPattern(Constants.TIME_FORMAT_PATTERN);
    private static final DateTimeFormatter DATETIME_FORMATTER =
        DateTimeFormatter.ofPattern(Constants.DATETIME_FORMAT_PATTERN);

    /**
     * Private constructor to prevent instantiation
     */
    private DateUtil() {
        throw new AssertionError("Cannot instantiate DateUtil class");
    }

    /**
     * Parses a date string in dd/MM/yyyy format to LocalDate.
     *
     * @param dateString the date string to parse (dd/MM/yyyy format)
     * @return LocalDate object if parsing is successful
     * @throws IllegalArgumentException if date string format is invalid
     */
    public static LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                "Invalid date format. Expected dd/MM/yyyy, got: " + dateString, e);
        }
    }

    /**
     * Parses a time string in HH:mm format to LocalTime.
     *
     * @param timeString the time string to parse (HH:mm format)
     * @return LocalTime object if parsing is successful
     * @throws IllegalArgumentException if time string format is invalid
     */
    public static LocalTime parseTime(String timeString) {
        try {
            return LocalTime.parse(timeString, TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                "Invalid time format. Expected HH:mm, got: " + timeString, e);
        }
    }

    /**
     * Formats a LocalDate to dd/MM/yyyy string format.
     *
     * @param date the LocalDate to format
     * @return formatted date string in dd/MM/yyyy format
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DATE_FORMATTER);
    }

    /**
     * Formats a LocalTime to HH:mm string format.
     *
     * @param time the LocalTime to format
     * @return formatted time string in HH:mm format
     */
    public static String formatTime(LocalTime time) {
        if (time == null) {
            return "";
        }
        return time.format(TIME_FORMATTER);
    }

    /**
     * Checks if a given date is a valid future date (not in the past).
     *
     * @param date the date to validate
     * @return true if date is today or in the future, false if in the past
     */
    public static boolean isFutureDate(LocalDate date) {
        return !date.isBefore(LocalDate.now());
    }

    /**
     * Checks if a given date is valid (not null and within reasonable bounds).
     *
     * @param date the date to validate
     * @return true if date is valid, false otherwise
     */
    public static boolean isValidDate(LocalDate date) {
        if (date == null) {
            return false;
        }
        LocalDate minDate = LocalDate.of(1900, 1, 1);
        LocalDate maxDate = LocalDate.of(2100, 12, 31);
        return !date.isBefore(minDate) && !date.isAfter(maxDate);
    }

    /**
     * Checks if a given time is valid (within business hours).
     *
     * @param time the time to validate
     * @return true if time is within business hours (9:00 AM - 6:00 PM), false otherwise
     */
    public static boolean isValidAppointmentTime(LocalTime time) {
        if (time == null) {
            return false;
        }
        LocalTime startTime = parseTime(Constants.APPOINTMENT_START_TIME);
        LocalTime endTime = parseTime(Constants.APPOINTMENT_END_TIME);
        return !time.isBefore(startTime) && time.isBefore(endTime);
    }

    /**
     * Generates available appointment slots for a given date.
     * Each slot is 30 minutes long, starting from 9:00 AM to 6:00 PM.
     *
     * @param date the date to generate slots for
     * @return List of available appointment times as LocalTime objects
     */
    public static List<LocalTime> generateAppointmentSlots(LocalDate date) {
        List<LocalTime> slots = new ArrayList<>();

        if (!isValidDate(date)) {
            return slots;
        }

        LocalTime startTime = parseTime(Constants.APPOINTMENT_START_TIME);
        LocalTime endTime = parseTime(Constants.APPOINTMENT_END_TIME);
        int slotDuration = Constants.APPOINTMENT_SLOT_DURATION_MINUTES;

        LocalTime currentSlot = startTime;
        while (currentSlot.isBefore(endTime)) {
            slots.add(currentSlot);
            currentSlot = currentSlot.plusMinutes(slotDuration);
        }

        return slots;
    }

    /**
     * Generates available appointment dates for the next N days.
     *
     * @param daysAhead number of days ahead to generate slots for
     * @return List of LocalDate objects for the next N days (excluding past dates)
     */
    public static List<LocalDate> generateAvailableDates(int daysAhead) {
        List<LocalDate> availableDates = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (int i = 0; i < daysAhead; i++) {
            LocalDate date = today.plusDays(i);
            if (isValidDate(date)) {
                availableDates.add(date);
            }
        }

        return availableDates;
    }

    /**
     * Checks if a date is within a given date range (inclusive).
     *
     * @param date the date to check
     * @param startDate the start of the range (inclusive)
     * @param endDate the end of the range (inclusive)
     * @return true if date is within range, false otherwise
     */
    public static boolean isDateInRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        if (date == null || startDate == null || endDate == null) {
            return false;
        }
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    /**
     * Gets the number of days between two dates.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return number of days between dates (can be negative if endDate is before startDate)
     */
    public static long getDaysBetween(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * Gets today's date.
     *
     * @return LocalDate representing today
     */
    public static LocalDate getTodayDate() {
        return LocalDate.now();
    }

    /**
     * Gets current time.
     *
     * @return LocalTime representing current time
     */
    public static LocalTime getCurrentTime() {
        return LocalTime.now();
    }
}

