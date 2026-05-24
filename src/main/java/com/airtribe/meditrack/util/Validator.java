package main.java.com.airtribe.meditrack.util;

import main.java.com.airtribe.meditrack.constants.Constants;
import main.java.com.airtribe.meditrack.exception.InvalidDataException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Validator {
    private Validator() {
        throw new AssertionError("Cannot instantiate Validator class");
    }

    public static void validateName(String name) throws InvalidDataException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDataException("Name cannot be null or empty");
        }
        if (name.length() < Constants.MIN_NAME_LENGTH) {
            throw new InvalidDataException("Name must be at least " + Constants.MIN_NAME_LENGTH + " characters long");
        }
        if (name.length() > Constants.MAX_NAME_LENGTH) {
            throw new InvalidDataException("Name cannot exceed " + Constants.MAX_NAME_LENGTH + " characters");
        }
        if (!name.matches("[a-zA-Z\\s.'-]+")) {
            throw new InvalidDataException("Name can only contain letters, spaces, dots, hyphens, and apostrophes");
        }
    }

    public static void validateAge(int age) throws InvalidDataException {
        if (age < Constants.MIN_PATIENT_AGE) {
            throw new InvalidDataException("Age cannot be less than " + Constants.MIN_PATIENT_AGE);
        }
        if (age > Constants.MAX_PATIENT_AGE) {
            throw new InvalidDataException("Age cannot exceed " + Constants.MAX_PATIENT_AGE);
        }
    }

    public static void validatePhone(String phone) throws InvalidDataException {
        if (phone == null || phone.trim().isEmpty()) {
            throw new InvalidDataException("Phone number cannot be null or empty");
        }

        String cleanedPhone = phone.replaceAll("[\\s\\-\\+\\(\\)]", "");

        if (!cleanedPhone.matches(Constants.PHONE_REGEX)) {
            throw new InvalidDataException("Phone number must contain 10-15 digits");
        }
    }

    public static void validateEmail(String email) throws InvalidDataException {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidDataException("Email cannot be null or empty");
        }
        if (!email.matches(Constants.EMAIL_REGEX)) {
            throw new InvalidDataException("Invalid email format");
        }
    }

    public static void validateGender(String gender) throws InvalidDataException {
        if (gender == null || gender.trim().isEmpty()) {
            throw new InvalidDataException("Gender cannot be null or empty");
        }
        if (!gender.matches("^[MFOmfo]$")) {
            throw new InvalidDataException("Gender must be M (Male), F (Female), or O (Other)");
        }
    }

    public static void validateBloodGroup(String bloodGroup) throws InvalidDataException {
        if (bloodGroup == null || bloodGroup.trim().isEmpty()) {
            throw new InvalidDataException("Blood group cannot be null or empty");
        }
        if (!bloodGroup.matches("^(A|B|AB|O)[+-]$|^(A|B|AB|O)[+-]$")) {
            throw new InvalidDataException("Invalid blood group format. Use: A+, A-, B+, B-, AB+, AB-, O+, O-");
        }
    }

    public static void validateMedicalHistory(String disease) throws InvalidDataException {
        if (disease == null || disease.trim().isEmpty()) {
            throw new InvalidDataException("Medical history cannot be null or empty");
        }
        if (disease.length() > 500) {
            throw new InvalidDataException("Medical history cannot exceed 500 characters");
        }
    }

    public static void validateSpecialization(String specialization) throws InvalidDataException {
        if (specialization == null || specialization.trim().isEmpty()) {
            throw new InvalidDataException("Specialization cannot be null or empty");
        }
        if (specialization.length() < 3 || specialization.length() > 50) {
            throw new InvalidDataException("Specialization must be between 3 and 50 characters");
        }
    }

    public static void validateExperience(int experienceYears) throws InvalidDataException {
        if (experienceYears < 0) {
            throw new InvalidDataException("Years of experience cannot be negative");
        }
        if (experienceYears > 70) {
            throw new InvalidDataException("Years of experience cannot exceed 70");
        }
    }

    public static void validateConsultationFee(double consultationFee) throws InvalidDataException {
        if (consultationFee < 0) {
            throw new InvalidDataException("Consultation fee cannot be negative");
        }
        if (consultationFee == 0) {
            throw new InvalidDataException("Consultation fee must be greater than 0");
        }
        if (consultationFee > 100000) {
            throw new InvalidDataException("Consultation fee seems unreasonably high (max 100,000)");
        }
    }

    public static void validateAppointmentDate(LocalDate appointmentDate) throws InvalidDataException {
        if (appointmentDate == null) {
            throw new InvalidDataException("Appointment date cannot be null");
        }
        if (!DateUtil.isValidDate(appointmentDate)) {
            throw new InvalidDataException("Appointment date is not valid");
        }
        if (!DateUtil.isFutureDate(appointmentDate)) {
            throw new InvalidDataException("Appointment date cannot be in the past");
        }
    }

    public static void validateAppointmentTime(LocalTime appointmentTime) throws InvalidDataException {
        if (appointmentTime == null) {
            throw new InvalidDataException("Appointment time cannot be null");
        }
        if (!DateUtil.isValidAppointmentTime(appointmentTime)) {
            throw new InvalidDataException("Appointment time must be between 9:00 AM and 6:00 PM");
        }
    }

    public static void validateAppointmentStatus(String status) throws InvalidDataException {
        if (status == null || status.trim().isEmpty()) {
            throw new InvalidDataException("Appointment status cannot be null or empty");
        }
        if (!status.matches("CONFIRMED|CANCELLED|PENDING|SCHEDULED")) {
            throw new InvalidDataException("Invalid appointment status: " + status);
        }
    }
}

