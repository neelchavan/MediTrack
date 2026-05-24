package main.java.com.airtribe.meditrack;

import main.java.com.airtribe.meditrack.service.DoctorService;
import main.java.com.airtribe.meditrack.service.PatientService;
import main.java.com.airtribe.meditrack.service.AppointmentService;
import main.java.com.airtribe.meditrack.service.BillingService;
import main.java.com.airtribe.meditrack.service.BillFactory;
import main.java.com.airtribe.meditrack.entity.Doctor;
import main.java.com.airtribe.meditrack.entity.Patient;
import main.java.com.airtribe.meditrack.entity.Appointment;
import main.java.com.airtribe.meditrack.entity.Bill;
import main.java.com.airtribe.meditrack.exception.InvalidDataException;
import main.java.com.airtribe.meditrack.exception.AppointmentNotFoundException;
import main.java.com.airtribe.meditrack.util.DateUtil;
import main.java.com.airtribe.meditrack.constants.Constants;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for MediTrack - Clinic & Appointment Management System
 * Provides a menu-driven console interface for managing doctors, patients, and appointments.
 *
 * Features:
 * - Register and manage patients
 * - Register and manage doctors
 * - Book and manage appointments
 * - Search and filter operations
 * - View available doctors and appointment slots
 */
public class Main {
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final BillingService billingService;
    private final Scanner scanner;
    private boolean running;

    /**
     * Initializes the MediTrack application with all services and scanner
     */
    public Main() {
        this.doctorService = new DoctorService();
        this.patientService = new PatientService();
        this.appointmentService = new AppointmentService();
        this.billingService = new BillingService();
        this.scanner = new Scanner(System.in);
        this.running = true;
    }

    /**
     * Main entry point for the application
     * @param args command-line arguments (reserved for future use)
     */
    public static void main(String[] args) {
        Main app = new Main();

        // Start the application
        app.run();
    }

    /**
     * Main application loop - displays menu and processes user input
     */
    public void run() {
        displayWelcome();

        while (running) {
            displayMainMenu();
            processMenuChoice();
        }

        displayGoodbye();
        scanner.close();
    }

    /**
     * Displays welcome message
     */
    private void displayWelcome() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("     🏥 WELCOME TO MEDITRACK - CLINIC MANAGEMENT SYSTEM 🏥");
        System.out.println("=".repeat(70));
        System.out.println("Your trusted appointment and clinic management solution");
        System.out.println("=".repeat(70) + "\n");
    }

    /**
     * Displays the main menu
     */
    private void displayMainMenu() {
        System.out.println("\n" + "-".repeat(70));
        System.out.println("📋 MAIN MENU");
        System.out.println("-".repeat(70));
        System.out.println("1.  👥 Patient Management");
        System.out.println("2.  🩺 Doctor Management");
        System.out.println("3.  📅 Appointment Management");
        System.out.println("4.  💰 Billing Management");
        System.out.println("5.  🔍 Search & Filter");
        System.out.println("6.  📊 View Statistics");
        System.out.println("7.  ❌ Exit");
        System.out.println("-".repeat(70));
        System.out.print("Choose an option (1-7): ");
    }

    /**
     * Processes main menu choice
     */
    private void processMenuChoice() {
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                patientMenu();
                break;
            case "2":
                doctorMenu();
                break;
            case "3":
                appointmentMenu();
                break;
            case "4":
                billingMenu();
                break;
            case "5":
                searchMenu();
                break;
            case "6":
                displayStatistics();
                break;
            case "7":
                running = false;
                break;
            default:
                System.out.println("❌ Invalid choice. Please select 1-7.");
        }
    }

    /**
     * Patient management menu
     */
    private void patientMenu() {
        System.out.println("\n" + "-".repeat(70));
        System.out.println("👥 PATIENT MANAGEMENT");
        System.out.println("-".repeat(70));
        System.out.println("1. Register New Patient");
        System.out.println("2. View All Patients");
        System.out.println("3. View Patient Details");
        System.out.println("4. Update Patient Information");
        System.out.println("5. Delete Patient");
        System.out.println("6. Back to Main Menu");
        System.out.print("Choose an option (1-6): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                registerPatient();
                break;
            case "2":
                viewAllPatients();
                break;
            case "3":
                viewPatientDetails();
                break;
            case "4":
                updatePatient();
                break;
            case "5":
                deletePatient();
                break;
            case "6":
                // Return to main menu
                break;
            default:
                System.out.println("❌ Invalid choice.");
        }
    }

    /**
     * Register a new patient
     */
    private void registerPatient() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📝 REGISTER NEW PATIENT");
        System.out.println("=".repeat(70));

        try {
            System.out.print("Enter patient name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter age: ");
            int age = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter gender (M/F/O): ");
            String gender = scanner.nextLine().trim();

            System.out.print("Enter phone number: ");
            String phone = scanner.nextLine().trim();

            System.out.print("Enter blood group (A+/A-/B+/B-/AB+/AB-/O+/O-): ");
            String bloodGroup = scanner.nextLine().trim();

            System.out.print("Enter medical history: ");
            String medicalHistory = scanner.nextLine().trim();

            Patient patient = patientService.addPatient(name, age, gender, phone, bloodGroup, medicalHistory);

            System.out.println("\n✅ Patient registered successfully!");
            System.out.println("Patient ID: " + patient.getId());
            System.out.println("Name: " + patient.getName());
            System.out.println("Age: " + patient.getAge());
            System.out.println("Blood Group: " + patient.getBloodGroup());
        } catch (InvalidDataException e) {
            System.out.println("❌ Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input format. Please enter valid values.");
        }
    }

    /**
     * View all patients
     */
    private void viewAllPatients() {
        List<Patient> patients = patientService.getAll();

        if (patients.isEmpty()) {
            System.out.println("\n⚠️  No patients registered yet.");
            return;
        }

        System.out.println("\n" + "=".repeat(70));
        System.out.println("👥 ALL PATIENTS (" + patients.size() + " total)");
        System.out.println("=".repeat(70));
        System.out.printf("%-8s | %-20s | %-5s | %-15s | %-6s%n", "ID", "Name", "Age", "Phone", "B.Group");
        System.out.println("-".repeat(70));

        for (Patient patient : patients) {
            System.out.printf("%-8d | %-20s | %-5d | %-15s | %-6s%n",
                    patient.getId(), patient.getName(), patient.getAge(),
                    patient.getPhone(), patient.getBloodGroup());
        }
    }

    /**
     * View patient details by ID
     */
    private void viewPatientDetails() {
        System.out.print("\nEnter patient ID: ");
        try {
            int patientId = Integer.parseInt(scanner.nextLine().trim());
            Patient patient = patientService.getPatientById(patientId);

            if (patient == null) {
                System.out.println("❌ Patient not found.");
                return;
            }

            System.out.println("\n" + "=".repeat(70));
            System.out.println("📋 PATIENT DETAILS");
            System.out.println("=".repeat(70));
            System.out.println("ID: " + patient.getId());
            System.out.println("Name: " + patient.getName());
            System.out.println("Age: " + patient.getAge());
            System.out.println("Gender: " + patient.getGender());
            System.out.println("Phone: " + patient.getPhone());
            System.out.println("Blood Group: " + patient.getBloodGroup());
            System.out.println("Medical History: " + patient.getDisease());
            System.out.println("Total Appointments: " + appointmentService.getPatientAppointmentCount(patientId));
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid ID format.");
        }
    }

    /**
     * Update patient information
     */
    private void updatePatient() {
        System.out.print("\nEnter patient ID to update: ");
        try {
            int patientId = Integer.parseInt(scanner.nextLine().trim());

            if (!patientService.patientExists(patientId)) {
                System.out.println("❌ Patient not found.");
                return;
            }

            System.out.print("Enter new name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter new age: ");
            int age = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter new gender (M/F/O): ");
            String gender = scanner.nextLine().trim();
            System.out.print("Enter new phone: ");
            String phone = scanner.nextLine().trim();
            System.out.print("Enter new blood group: ");
            String bloodGroup = scanner.nextLine().trim();
            System.out.print("Enter new medical history: ");
            String medicalHistory = scanner.nextLine().trim();

            Patient updated = patientService.updatePatient(patientId, name, age, gender, phone, bloodGroup, medicalHistory);
            System.out.println("✅ Patient updated successfully!");
        } catch (InvalidDataException e) {
            System.out.println("❌ Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input format.");
        }
    }

    /**
     * Delete a patient
     */
    private void deletePatient() {
        System.out.print("\nEnter patient ID to delete: ");
        try {
            int patientId = Integer.parseInt(scanner.nextLine().trim());

            if (patientService.deletePatient(patientId)) {
                System.out.println("✅ Patient deleted successfully.");
            } else {
                System.out.println("❌ Patient not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid ID format.");
        }
    }

    /**
     * Doctor management menu
     */
    private void doctorMenu() {
        System.out.println("\n" + "-".repeat(70));
        System.out.println("🩺 DOCTOR MANAGEMENT");
        System.out.println("-".repeat(70));
        System.out.println("1. Register New Doctor");
        System.out.println("2. View All Doctors");
        System.out.println("3. View Doctor Details");
        System.out.println("4. Search Doctors by Specialization");
        System.out.println("5. Update Doctor Information");
        System.out.println("6. Delete Doctor");
        System.out.println("7. Back to Main Menu");
        System.out.print("Choose an option (1-7): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                registerDoctor();
                break;
            case "2":
                viewAllDoctors();
                break;
            case "3":
                viewDoctorDetails();
                break;
            case "4":
                searchDoctorsBySpecialization();
                break;
            case "5":
                updateDoctor();
                break;
            case "6":
                deleteDoctor();
                break;
            case "7":
                // Return to main menu
                break;
            default:
                System.out.println("❌ Invalid choice.");
        }
    }

    /**
     * Register a new doctor
     */
    private void registerDoctor() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📝 REGISTER NEW DOCTOR");
        System.out.println("=".repeat(70));

        try {
            System.out.print("Enter doctor name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter age: ");
            int age = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter gender (M/F/O): ");
            String gender = scanner.nextLine().trim();

            System.out.print("Enter phone number: ");
            String phone = scanner.nextLine().trim();

            System.out.print("Enter specialization (e.g., Cardiology, Neurology): ");
            String specialization = scanner.nextLine().trim();

            System.out.print("Enter years of experience: ");
            int experience = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter consultation fee: ");
            double fee = Double.parseDouble(scanner.nextLine().trim());

            Doctor doctor = doctorService.addDoctor(name, age, gender, phone, specialization, experience, fee);

            System.out.println("\n✅ Doctor registered successfully!");
            System.out.println("Doctor ID: " + doctor.getId());
            System.out.println("Name: " + doctor.getName());
            System.out.println("Specialization: " + doctor.getSpecialization());
            System.out.println("Experience: " + doctor.getExperienceYears() + " years");
            System.out.println("Consultation Fee: ₹" + doctor.getConsultationFee());
        } catch (InvalidDataException e) {
            System.out.println("❌ Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input format.");
        }
    }

    /**
     * View all doctors
     */
    private void viewAllDoctors() {
        List<Doctor> doctors = doctorService.getAll();

        if (doctors.isEmpty()) {
            System.out.println("\n⚠️  No doctors registered yet.");
            return;
        }

        System.out.println("\n" + "=".repeat(90));
        System.out.println("🩺 ALL DOCTORS (" + doctors.size() + " total)");
        System.out.println("=".repeat(90));
        System.out.printf("%-8s | %-20s | %-20s | %-8s | %-8s%n", "ID", "Name", "Specialization", "Exp(Y)", "Fee(₹)");
        System.out.println("-".repeat(90));

        for (Doctor doctor : doctors) {
            System.out.printf("%-8d | %-20s | %-20s | %-8d | %-8.2f%n",
                    doctor.getId(), doctor.getName(), doctor.getSpecialization(),
                    doctor.getExperienceYears(), doctor.getConsultationFee());
        }
    }

    /**
     * View doctor details by ID
     */
    private void viewDoctorDetails() {
        System.out.print("\nEnter doctor ID: ");
        try {
            int doctorId = Integer.parseInt(scanner.nextLine().trim());
            Doctor doctor = doctorService.getDoctorById(doctorId);

            if (doctor == null) {
                System.out.println("❌ Doctor not found.");
                return;
            }

            System.out.println("\n" + "=".repeat(70));
            System.out.println("🩺 DOCTOR DETAILS");
            System.out.println("=".repeat(70));
            System.out.println("ID: " + doctor.getId());
            System.out.println("Name: " + doctor.getName());
            System.out.println("Age: " + doctor.getAge());
            System.out.println("Gender: " + doctor.getGender());
            System.out.println("Phone: " + doctor.getPhone());
            System.out.println("Specialization: " + doctor.getSpecialization());
            System.out.println("Experience: " + doctor.getExperienceYears() + " years");
            System.out.println("Consultation Fee: ₹" + doctor.getConsultationFee());
            System.out.println("Total Appointments: " + appointmentService.getDoctorAppointmentCount(doctorId));
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid ID format.");
        }
    }

    /**
     * Search doctors by specialization
     */
    private void searchDoctorsBySpecialization() {
        System.out.print("\nEnter specialization to search: ");
        String specialization = scanner.nextLine().trim();

        List<Doctor> doctors = doctorService.searchBySpecialization(specialization);

        if (doctors.isEmpty()) {
            System.out.println("⚠️  No doctors found with specialization: " + specialization);
            return;
        }

        System.out.println("\n" + "=".repeat(90));
        System.out.println("🔍 DOCTORS - SPECIALIZATION: " + specialization.toUpperCase());
        System.out.println("=".repeat(90));
        System.out.printf("%-8s | %-20s | %-8s | %-8s%n", "ID", "Name", "Exp(Y)", "Fee(₹)");
        System.out.println("-".repeat(90));

        for (Doctor doctor : doctors) {
            System.out.printf("%-8d | %-20s | %-8d | %-8.2f%n",
                    doctor.getId(), doctor.getName(),
                    doctor.getExperienceYears(), doctor.getConsultationFee());
        }
    }

    /**
     * Update doctor information
     */
    private void updateDoctor() {
        System.out.print("\nEnter doctor ID to update: ");
        try {
            int doctorId = Integer.parseInt(scanner.nextLine().trim());

            if (!doctorService.doctorExists(doctorId)) {
                System.out.println("❌ Doctor not found.");
                return;
            }

            System.out.print("Enter new name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter new age: ");
            int age = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter new gender (M/F/O): ");
            String gender = scanner.nextLine().trim();
            System.out.print("Enter new phone: ");
            String phone = scanner.nextLine().trim();
            System.out.print("Enter new specialization: ");
            String specialization = scanner.nextLine().trim();
            System.out.print("Enter new experience (years): ");
            int experience = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter new consultation fee: ");
            double fee = Double.parseDouble(scanner.nextLine().trim());

            Doctor updated = doctorService.updateDoctor(doctorId, name, age, gender, phone, specialization, experience, fee);
            System.out.println("✅ Doctor updated successfully!");
        } catch (InvalidDataException e) {
            System.out.println("❌ Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input format.");
        }
    }

    /**
     * Delete a doctor
     */
    private void deleteDoctor() {
        System.out.print("\nEnter doctor ID to delete: ");
        try {
            int doctorId = Integer.parseInt(scanner.nextLine().trim());

            if (doctorService.deleteDoctor(doctorId)) {
                System.out.println("✅ Doctor deleted successfully.");
            } else {
                System.out.println("❌ Doctor not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid ID format.");
        }
    }

    /**
     * Appointment management menu
     */
    private void appointmentMenu() {
        System.out.println("\n" + "-".repeat(70));
        System.out.println("📅 APPOINTMENT MANAGEMENT");
        System.out.println("-".repeat(70));
        System.out.println("1. Book New Appointment");
        System.out.println("2. View All Appointments");
        System.out.println("3. View Patient Appointments");
        System.out.println("4. Cancel Appointment");
        System.out.println("5. Reschedule Appointment");
        System.out.println("6. View Available Slots");
        System.out.println("7. Back to Main Menu");
        System.out.print("Choose an option (1-7): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                bookAppointment();
                break;
            case "2":
                viewAllAppointments();
                break;
            case "3":
                viewPatientAppointments();
                break;
            case "4":
                cancelAppointment();
                break;
            case "5":
                rescheduleAppointment();
                break;
            case "6":
                viewAvailableSlots();
                break;
            case "7":
                // Return to main menu
                break;
            default:
                System.out.println("❌ Invalid choice.");
        }
    }

    /**
     * Book a new appointment
     */
    private void bookAppointment() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📝 BOOK NEW APPOINTMENT");
        System.out.println("=".repeat(70));

        try {
            System.out.print("Enter patient ID: ");
            int patientId = Integer.parseInt(scanner.nextLine().trim());
            Patient patient = patientService.getPatientById(patientId);

            if (patient == null) {
                System.out.println("❌ Patient not found.");
                return;
            }

            System.out.print("Enter doctor ID: ");
            int doctorId = Integer.parseInt(scanner.nextLine().trim());
            Doctor doctor = doctorService.getDoctorById(doctorId);

            if (doctor == null) {
                System.out.println("❌ Doctor not found.");
                return;
            }

            System.out.print("Enter appointment date (dd/MM/yyyy): ");
            String dateStr = scanner.nextLine().trim();
            LocalDate appointmentDate = DateUtil.parseDate(dateStr);

            System.out.print("Enter appointment time (HH:mm): ");
            String timeStr = scanner.nextLine().trim();
            LocalTime appointmentTime = DateUtil.parseTime(timeStr);

            Appointment appointment = appointmentService.bookAppointment(patient, doctor, appointmentDate, appointmentTime);

            System.out.println("\n✅ Appointment booked successfully!");
            System.out.println("Appointment ID: " + appointment.getAppointmentId());
            System.out.println("Patient: " + patient.getName());
            System.out.println("Doctor: " + doctor.getName());
            System.out.println("Date: " + DateUtil.formatDate(appointmentDate));
            System.out.println("Time: " + DateUtil.formatTime(appointmentTime));
            System.out.println("Status: " + appointment.getAppointmentStatus());
        } catch (InvalidDataException e) {
            System.out.println("❌ Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input format.");
        }
    }

    /**
     * View all appointments
     */
    private void viewAllAppointments() {
        List<Appointment> appointments = appointmentService.getAll();

        if (appointments.isEmpty()) {
            System.out.println("\n⚠️  No appointments booked yet.");
            return;
        }

        System.out.println("\n" + "=".repeat(100));
        System.out.println("📅 ALL APPOINTMENTS (" + appointments.size() + " total)");
        System.out.println("=".repeat(100));
        System.out.printf("%-8s | %-20s | %-20s | %-12s | %-8s | %-12s%n",
                "ID", "Patient", "Doctor", "Date", "Time", "Status");
        System.out.println("-".repeat(100));

        for (Appointment apt : appointments) {
            System.out.printf("%-8d | %-20s | %-20s | %-12s | %-8s | %-12s%n",
                    apt.getAppointmentId(),
                    apt.getPatient().getName(),
                    apt.getDoctor().getName(),
                    DateUtil.formatDate(apt.getAppointmentDate()),
                    DateUtil.formatTime(apt.getAppointmentTime()),
                    apt.getAppointmentStatus());
        }
    }

    /**
     * View patient's appointments
     */
    private void viewPatientAppointments() {
        System.out.print("\nEnter patient ID: ");
        try {
            int patientId = Integer.parseInt(scanner.nextLine().trim());
            List<Appointment> appointments = appointmentService.searchByPatientId(patientId);

            if (appointments.isEmpty()) {
                System.out.println("⚠️  No appointments found for this patient.");
                return;
            }

            System.out.println("\n" + "=".repeat(100));
            System.out.println("📅 PATIENT APPOINTMENTS (" + appointments.size() + " total)");
            System.out.println("=".repeat(100));
            System.out.printf("%-8s | %-20s | %-12s | %-8s | %-12s%n",
                    "ID", "Doctor", "Date", "Time", "Status");
            System.out.println("-".repeat(100));

            for (Appointment apt : appointments) {
                System.out.printf("%-8d | %-20s | %-12s | %-8s | %-12s%n",
                        apt.getAppointmentId(),
                        apt.getDoctor().getName(),
                        DateUtil.formatDate(apt.getAppointmentDate()),
                        DateUtil.formatTime(apt.getAppointmentTime()),
                        apt.getAppointmentStatus());
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid ID format.");
        }
    }

    /**
     * Cancel an appointment
     */
    private void cancelAppointment() {
        System.out.print("\nEnter appointment ID to cancel: ");
        try {
            int appointmentId = Integer.parseInt(scanner.nextLine().trim());

            Appointment cancelled = appointmentService.cancelAppointment(appointmentId);
            System.out.println("✅ Appointment cancelled successfully.");
            System.out.println("Appointment ID: " + cancelled.getAppointmentId());
            System.out.println("New Status: " + cancelled.getAppointmentStatus());
        } catch (AppointmentNotFoundException e) {
            System.out.println("❌ Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid ID format.");
        }
    }

    /**
     * Reschedule an appointment
     */
    private void rescheduleAppointment() {
        System.out.print("\nEnter appointment ID to reschedule: ");
        try {
            int appointmentId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter new date (dd/MM/yyyy): ");
            LocalDate newDate = DateUtil.parseDate(scanner.nextLine().trim());

            System.out.print("Enter new time (HH:mm): ");
            LocalTime newTime = DateUtil.parseTime(scanner.nextLine().trim());

            Appointment rescheduled = appointmentService.rescheduleAppointment(appointmentId, newDate, newTime);
            System.out.println("✅ Appointment rescheduled successfully.");
            System.out.println("New Date: " + DateUtil.formatDate(newDate));
            System.out.println("New Time: " + DateUtil.formatTime(newTime));
        } catch (InvalidDataException e) {
            System.out.println("❌ Error: " + e.getMessage());
        } catch (AppointmentNotFoundException e) {
            System.out.println("❌ Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input format.");
        }
    }

    /**
     * View available slots for a doctor
     */
    private void viewAvailableSlots() {
        System.out.print("\nEnter doctor ID: ");
        try {
            int doctorId = Integer.parseInt(scanner.nextLine().trim());

            if (!doctorService.doctorExists(doctorId)) {
                System.out.println("❌ Doctor not found.");
                return;
            }

            System.out.print("Enter date (dd/MM/yyyy): ");
            LocalDate date = DateUtil.parseDate(scanner.nextLine().trim());

            List<LocalTime> slots = appointmentService.getAvailableTimeSlots(doctorId, date);

            if (slots.isEmpty()) {
                System.out.println("⚠️  No available slots for this date.");
                return;
            }

            System.out.println("\n" + "=".repeat(70));
            System.out.println("⏰ AVAILABLE SLOTS ON " + DateUtil.formatDate(date));
            System.out.println("=".repeat(70));

            for (int i = 0; i < slots.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + DateUtil.formatTime(slots.get(i)));
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input format.");
        }
    }

    /**
     * Billing management menu
     */
    private void billingMenu() {
        System.out.println("\n" + "-".repeat(70));
        System.out.println("💰 BILLING MANAGEMENT");
        System.out.println("-".repeat(70));
        System.out.println("1. Generate Bill (Standard)");
        System.out.println("2. Generate Bill (With Strategy)");
        System.out.println("3. View All Bills");
        System.out.println("4. View Bill Details");
        System.out.println("5. Update Bill Status");
        System.out.println("6. Delete Bill");
        System.out.println("7. Back to Main Menu");
        System.out.print("Choose an option (1-7): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                generateBill();
                break;
            case "2":
                generateBillWithStrategy();
                break;
            case "3":
                viewAllBills();
                break;
            case "4":
                viewBillDetails();
                break;
            case "5":
                updateBillStatus();
                break;
            case "6":
                deleteBill();
                break;
            case "7":
                // Return to main menu
                break;
            default:
                System.out.println("❌ Invalid choice.");
        }
    }

    /**
     * Generate a new bill (Standard billing)
     */
    private void generateBill() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🧾 GENERATE BILL (Standard)");
        System.out.println("=".repeat(70));

        try {
            System.out.print("Enter appointment ID: ");
            int appointmentId = Integer.parseInt(scanner.nextLine().trim());
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);

            System.out.print("Enter payment status (PAID/PENDING): ");
            String status = scanner.nextLine().trim().toUpperCase();

            Bill bill = billingService.generateBill(appointment, status);

            System.out.println("\n✅ Bill generated successfully!");
            System.out.println("Bill ID: " + bill.getId());
            System.out.println("Appointment ID: " + bill.getAppointment().getAppointmentId());
            System.out.println("Patient: " + bill.getAppointment().getPatient().getName());
            System.out.println("Doctor: " + bill.getAppointment().getDoctor().getName());
            System.out.println("Amount: ₹" + bill.getAmount());
            System.out.println("Status: " + bill.getStatus());
        } catch (AppointmentNotFoundException e) {
            System.out.println("❌ Error: " + e.getMessage());
        } catch (InvalidDataException e) {
            System.out.println("❌ Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input format.");
        }
    }

    /**
     * Generate a bill with billing strategy using Factory Pattern
     */
    private void generateBillWithStrategy() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("🧾 GENERATE BILL (With Strategy)");
        System.out.println("=".repeat(70));
        System.out.println("Available Billing Strategies:");
        System.out.println("1. STANDARD    - Consultation fee + 18% tax");
        System.out.println("2. DISCOUNTED  - 10% discount on consultation + 18% tax");
        System.out.println("3. PREMIUM     - 20% markup on consultation + 18% tax");
        System.out.println("4. FLAT_RATE   - Fixed consultation fee (₹" + Constants.CONSULTATION_FEE_DEFAULT + ") + 18% tax");
        System.out.println("=".repeat(70));

        try {
            System.out.print("Enter appointment ID: ");
            int appointmentId = Integer.parseInt(scanner.nextLine().trim());
            Appointment appointment = appointmentService.getAppointmentById(appointmentId);

            System.out.print("Enter strategy (STANDARD/DISCOUNTED/PREMIUM/FLAT_RATE): ");
            String strategyStr = scanner.nextLine().trim().toUpperCase();
            BillFactory.BillingStrategy strategy = BillFactory.getBillingStrategyFromString(strategyStr);

            System.out.print("Enter payment status (PAID/PENDING): ");
            String status = scanner.nextLine().trim().toUpperCase();

            Bill bill = BillFactory.createBill(appointment, strategy, status);
            // Store the bill in the billing service so it can be retrieved later
            billingService.addBillToStore(bill);

            System.out.println("\n✅ Bill generated successfully!");
            System.out.println("Bill ID: " + bill.getId());
            System.out.println("Appointment ID: " + bill.getAppointment().getAppointmentId());
            System.out.println("Patient: " + bill.getAppointment().getPatient().getName());
            System.out.println("Doctor: " + bill.getAppointment().getDoctor().getName());
            System.out.println("Strategy: " + strategy.getDescription());
            System.out.println("Amount: ₹" + bill.getAmount());
            System.out.println("Status: " + bill.getStatus());
            System.out.println("(✓ Tip: This bill was generated using the Factory Pattern with " + strategy + " strategy)");
        } catch (AppointmentNotFoundException e) {
            System.out.println("❌ Error: " + e.getMessage());
        } catch (InvalidDataException e) {
            System.out.println("❌ Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input format.");
        }
    }

    /**
     * View all bills
     */
    private void viewAllBills() {
        List<Bill> bills = billingService.getAll();

        if (bills.isEmpty()) {
            System.out.println("\n⚠️  No bills generated yet.");
            return;
        }

        System.out.println("\n" + "=".repeat(80));
        System.out.println("🧾 ALL BILLS (" + bills.size() + " total)");
        System.out.println("=".repeat(80));
        System.out.printf("%-8s | %-15s | %-20s | %-8s | %-8s%n", "ID", "Appointment ID", "Patient", "Amount", "Status");
        System.out.println("-".repeat(80));

        for (Bill bill : bills) {
            System.out.printf("%-8d | %-15d | %-20s | %-8.2f | %-8s%n",
                    bill.getId(), bill.getAppointment().getAppointmentId(),
                    bill.getAppointment().getPatient().getName(),
                    bill.getAmount(), bill.getStatus());
        }
    }

    /**
     * View bill details by ID
     */
    private void viewBillDetails() {
        System.out.print("\nEnter bill ID: ");
        try {
            int billId = Integer.parseInt(scanner.nextLine().trim());
            Bill bill = billingService.getBillById(billId);

            if (bill == null) {
                System.out.println("❌ Bill not found.");
                return;
            }

            System.out.println("\n" + "=".repeat(70));
            System.out.println("🧾 BILL DETAILS");
            System.out.println("=".repeat(70));
            System.out.println("ID: " + bill.getId());
            System.out.println("Appointment ID: " + bill.getAppointment().getAppointmentId());
            System.out.println("Patient: " + bill.getAppointment().getPatient().getName());
            System.out.println("Doctor: " + bill.getAppointment().getDoctor().getName());
            System.out.println("Amount: ₹" + bill.getAmount());
            System.out.println("Status: " + bill.getStatus());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid ID format.");
        }
    }

    /**
     * Update bill status
     */
    private void updateBillStatus() {
        System.out.print("\nEnter bill ID to update: ");
        try {
            int billId = Integer.parseInt(scanner.nextLine().trim());

            if (!billingService.billExists(billId)) {
                System.out.println("❌ Bill not found.");
                return;
            }

            System.out.print("Enter new status (PAID/PENDING): ");
            String status = scanner.nextLine().trim().toUpperCase();

            Bill updated = billingService.updateBillStatus(billId, status);
            System.out.println("✅ Bill status updated successfully!");
        } catch (InvalidDataException e) {
            System.out.println("❌ Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input format.");
        }
    }

    /**
     * Delete a bill
     */
    private void deleteBill() {
        System.out.print("\nEnter bill ID to delete: ");
        try {
            int billId = Integer.parseInt(scanner.nextLine().trim());

            if (billingService.deleteBill(billId)) {
                System.out.println("✅ Bill deleted successfully.");
            } else {
                System.out.println("❌ Bill not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid ID format.");
        }
    }

    /**
     * Search menu
     */
    private void searchMenu() {
        System.out.println("\n" + "-".repeat(70));
        System.out.println("🔍 SEARCH & FILTER");
        System.out.println("-".repeat(70));
        System.out.println("1. Search Patient by Name");
        System.out.println("2. Search Patient by Age Range");
        System.out.println("3. Search Doctor by Name");
        System.out.println("4. Search Appointments by Status");
        System.out.println("5. Search Appointments by Date Range");
        System.out.println("6. Back to Main Menu");
        System.out.print("Choose an option (1-6): ");

        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1":
                searchPatientByName();
                break;
            case "2":
                searchPatientByAgeRange();
                break;
            case "3":
                searchDoctorByName();
                break;
            case "4":
                searchAppointmentByStatus();
                break;
            case "5":
                searchAppointmentByDateRange();
                break;
            case "6":
                // Return to main menu
                break;
            default:
                System.out.println("❌ Invalid choice.");
        }
    }

    private void searchPatientByName() {
        System.out.print("\nEnter patient name to search: ");
        String name = scanner.nextLine().trim();
        List<Patient> results = patientService.searchByName(name);

        if (results.isEmpty()) {
            System.out.println("⚠️  No patients found.");
            return;
        }

        System.out.println("\n🔍 Search Results (" + results.size() + " found):");
        for (Patient p : results) {
            System.out.println("  - " + p.getId() + ": " + p.getName() + ", Age: " + p.getAge());
        }
    }

    private void searchPatientByAgeRange() {
        System.out.print("\nEnter minimum age: ");
        int minAge = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter maximum age: ");
        int maxAge = Integer.parseInt(scanner.nextLine().trim());

        List<Patient> results = patientService.searchByAgeRange(minAge, maxAge);

        if (results.isEmpty()) {
            System.out.println("⚠️  No patients found in this age range.");
            return;
        }

        System.out.println("\n🔍 Search Results (" + results.size() + " found):");
        for (Patient p : results) {
            System.out.println("  - " + p.getId() + ": " + p.getName() + ", Age: " + p.getAge());
        }
    }

    private void searchDoctorByName() {
        System.out.print("\nEnter doctor name to search: ");
        String name = scanner.nextLine().trim();
        List<Doctor> results = doctorService.searchByName(name);

        if (results.isEmpty()) {
            System.out.println("⚠️  No doctors found.");
            return;
        }

        System.out.println("\n🔍 Search Results (" + results.size() + " found):");
        for (Doctor d : results) {
            System.out.println("  - " + d.getId() + ": " + d.getName() + ", Specialization: " + d.getSpecialization());
        }
    }

    private void searchAppointmentByStatus() {
        System.out.print("\nEnter status (CONFIRMED/CANCELLED/PENDING/SCHEDULED): ");
        String status = scanner.nextLine().trim().toUpperCase();
        List<Appointment> results = appointmentService.searchByStatus(status);

        if (results.isEmpty()) {
            System.out.println("⚠️  No appointments found with this status.");
            return;
        }

        System.out.println("\n🔍 Search Results (" + results.size() + " found):");
        for (Appointment a : results) {
            System.out.println("  - " + a.getAppointmentId() + ": " + a.getPatient().getName() +
                    " → " + a.getDoctor().getName() + " on " + DateUtil.formatDate(a.getAppointmentDate()));
        }
    }

    private void searchAppointmentByDateRange() {
        System.out.print("\nEnter start date (dd/MM/yyyy): ");
        LocalDate startDate = DateUtil.parseDate(scanner.nextLine().trim());
        System.out.print("Enter end date (dd/MM/yyyy): ");
        LocalDate endDate = DateUtil.parseDate(scanner.nextLine().trim());

        List<Appointment> results = appointmentService.searchByDateRange(startDate, endDate);

        if (results.isEmpty()) {
            System.out.println("⚠️  No appointments found in this date range.");
            return;
        }

        System.out.println("\n🔍 Search Results (" + results.size() + " found):");
        for (Appointment a : results) {
            System.out.println("  - " + a.getAppointmentId() + ": " + a.getPatient().getName() +
                    " on " + DateUtil.formatDate(a.getAppointmentDate()));
        }
    }

    /**
     * Display statistics
     */
    private void displayStatistics() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📊 SYSTEM STATISTICS");
        System.out.println("=".repeat(70));
        System.out.println("👥 PATIENT STATISTICS:");
        System.out.println("   Total Patients: " + patientService.getTotalCount());
        System.out.println("   Average Age: " + String.format("%.1f", patientService.getAveragePatientAge()));
        System.out.println();
        System.out.println("🩺 DOCTOR STATISTICS:");
        System.out.println("   Total Doctors: " + doctorService.getTotalCount());
        System.out.println();
        System.out.println("📅 APPOINTMENT STATISTICS:");
        System.out.println("   Total Appointments: " + appointmentService.getTotalCount());
        System.out.println("   Confirmed: " + appointmentService.getAppointmentCountByStatus("CONFIRMED"));
        System.out.println("   Cancelled: " + appointmentService.getAppointmentCountByStatus("CANCELLED"));
        System.out.println();
        System.out.println("💰 BILLING STATISTICS:");
        System.out.println("   Total Bills: " + billingService.getTotalBillCount());
        System.out.println("   Paid Bills: " + billingService.getPaidBillCount());
        System.out.println("   Pending Bills: " + billingService.getUnpaidBillCount());
        System.out.println("   Total Revenue: ₹" + String.format("%.2f", billingService.getTotalRevenue()));
        System.out.println("   Pending Amount: ₹" + String.format("%.2f", billingService.getTotalPendingAmount()));
        System.out.println("   Average Bill: ₹" + String.format("%.2f", billingService.getAverageBillAmount()));
        System.out.println("=".repeat(70));
    }

    /**
     * Display goodbye message
     */
    private void displayGoodbye() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("👋 THANK YOU FOR USING MEDITRACK!");
        System.out.println("Your data has been kept in memory.");
        System.out.println("=".repeat(70) + "\n");
    }
}

