package main.java.com.airtribe.meditrack.test;

import main.java.com.airtribe.meditrack.service.DoctorService;
import main.java.com.airtribe.meditrack.service.PatientService;
import main.java.com.airtribe.meditrack.service.AppointmentService;
import main.java.com.airtribe.meditrack.service.BillFactory;
import main.java.com.airtribe.meditrack.service.BillingService;
import main.java.com.airtribe.meditrack.entity.Doctor;
import main.java.com.airtribe.meditrack.entity.Patient;
import main.java.com.airtribe.meditrack.entity.Appointment;
import main.java.com.airtribe.meditrack.entity.Bill;
import main.java.com.airtribe.meditrack.exception.InvalidDataException;
import main.java.com.airtribe.meditrack.util.DateUtil;
import main.java.com.airtribe.meditrack.util.Validator;
import main.java.com.airtribe.meditrack.util.IdGenerator;
import main.java.com.airtribe.meditrack.constants.Constants;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Comprehensive test runner for MediTrack application.
 * Tests all services, utilities, and business logic.
 * No external testing framework (JUnit) required - manual testing.
 */
public class UtilitiesTestRunner {
    private static int testsRun = 0;
    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        displayHeader();

        testConstants();
        testDateUtil();
        testValidator();
        testIdGenerator();
        testDoctorService();
        testPatientService();
        testAppointmentService();
        testBillFactory();
        testBillingService();
        testBillingIntegration();
        testSearchFunctionality();
        testValidationErrors();
        testConflictDetection();

        displaySummary();
    }

    private static void displayHeader() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("  🧪 MEDITRACK COMPREHENSIVE TEST RUNNER");
        System.out.println("  Date: " + DateUtil.formatDate(DateUtil.getTodayDate()));
        System.out.println("=".repeat(80) + "\n");
    }

    private static void testConstants() {
        printTest("Testing Constants.java");

        try {
            assert Constants.DEFAULT_TAX_RATE == 0.18;
            assert Constants.MAX_PATIENT_AGE == 120;
            assert Constants.MIN_PATIENT_AGE == 0;
            assert Constants.APPOINTMENT_SLOT_DURATION_MINUTES == 30;
            assert Constants.DATE_FORMAT_PATTERN.equals("dd/MM/yyyy");

            printPass("All constants loaded correctly");
            printPass("Date format pattern verified: " + Constants.DATE_FORMAT_PATTERN);
        } catch (AssertionError e) {
            printFail("Constants assertion failed: " + e.getMessage());
        }
    }

    private static void testDateUtil() {
        printTest("Testing DateUtil.java");

        try {
            // Test date parsing (dd/MM/yyyy format)
            LocalDate parsed = DateUtil.parseDate("17/05/2026");
            assert parsed.getDayOfMonth() == 17;
            assert parsed.getMonthValue() == 5;
            printPass("✓ Date parsing (dd/MM/yyyy): 17/05/2026 → " + parsed);

            // Test date formatting
            LocalDate date = LocalDate.of(2026, 5, 20);
            String formatted = DateUtil.formatDate(date);
            assert formatted.equals("20/05/2026");
            printPass("✓ Date formatting: " + date + " → " + formatted);

            // Test future date validation
            LocalDate futureDate = DateUtil.getTodayDate().plusDays(5);
            assert DateUtil.isFutureDate(futureDate);
            printPass("✓ Future date validation: " + futureDate + " is future");

            // Test appointment slot generation
            List<LocalTime> slots = DateUtil.generateAppointmentSlots(futureDate);
            assert slots.size() > 0;
            assert slots.size() == 18; // 9 AM to 6 PM in 30-min slots
            printPass("✓ Appointment slots generated: " + slots.size() + " slots for " + DateUtil.formatDate(futureDate));

            // Test available dates generation
            List<LocalDate> dates = DateUtil.generateAvailableDates(7);
            assert dates.size() == 7;
            printPass("✓ Available dates generated: " + dates.size() + " dates for next 7 days");
        } catch (Exception e) {
            printFail("DateUtil test failed: " + e.getMessage());
        }
    }

    private static void testValidator() {
        printTest("Testing Validator.java");

        try {
            Validator.validateName("Dr. John Smith");
            printPass("✓ Valid name accepted");

            Validator.validateAge(30);
            printPass("✓ Valid age accepted");

            Validator.validatePhone("9876543210");
            printPass("✓ Valid phone accepted");

            Validator.validateEmail("test@example.com");
            printPass("✓ Valid email accepted");

            Validator.validateBloodGroup("O+");
            printPass("✓ Valid blood group accepted");

            Validator.validateConsultationFee(500.0);
            printPass("✓ Valid consultation fee accepted");
        } catch (InvalidDataException e) {
            printFail("Validator test failed: " + e.getMessage());
        }
    }

    private static void testIdGenerator() {
        printTest("Testing IdGenerator.java (Singleton)");

        try {
            IdGenerator gen1 = IdGenerator.getInstance();
            IdGenerator gen2 = IdGenerator.getInstance();

            assert gen1 == gen2;
            printPass("✓ Singleton pattern verified: same instance");

            gen1.reset();

            int patientId = gen1.generatePatientId();
            assert patientId == 1001;
            printPass("✓ Patient ID generated: " + patientId);

            int doctorId = gen1.generateDoctorId();
            assert doctorId == 2001;
            printPass("✓ Doctor ID generated: " + doctorId);

            int appointmentId = gen1.generateAppointmentId();
            assert appointmentId == 3001;
            printPass("✓ Appointment ID generated: " + appointmentId);

            int nextPatientId = gen1.generatePatientId();
            assert nextPatientId == 1002;
            printPass("✓ Sequential IDs working: next patient ID = " + nextPatientId);
        } catch (Exception e) {
            printFail("IdGenerator test failed: " + e.getMessage());
        }
    }

    private static void testDoctorService() {
        printTest("Testing DoctorService.java");

        try {
            DoctorService service = new DoctorService();

            // Test add doctor
            Doctor doctor = service.addDoctor("Dr. Rajesh Sharma", 45, "M", "9876543210", "Cardiology", 20, 500.0);
            assert doctor.getId() > 0;
            printPass("✓ Doctor created with ID: " + doctor.getId());

            // Test get doctor
            Doctor retrieved = service.getDoctorById(doctor.getId());
            assert retrieved != null;
            assert retrieved.getName().equals("Dr. Rajesh Sharma");
            printPass("✓ Doctor retrieved by ID");

            // Test search by name
            List<Doctor> results = service.searchByName("Rajesh");
            assert results.size() > 0;
            printPass("✓ Doctor found by name search: " + results.size() + " result(s)");

            // Test search by specialization
            List<Doctor> cardiology = service.searchBySpecialization("Cardiology");
            assert cardiology.size() > 0;
            printPass("✓ Doctors found by specialization: " + cardiology.size());

            // Test total count
            assert service.getTotalCount() > 0;
            printPass("✓ Doctor count: " + service.getTotalCount());
        } catch (InvalidDataException e) {
            printFail("DoctorService test failed: " + e.getMessage());
        }
    }

    private static void testPatientService() {
        printTest("Testing PatientService.java");

        try {
            PatientService service = new PatientService();

            // Test add patient
            Patient patient = service.addPatient("Priya Patel", 28, "F", "9988776655", "A+", "No allergies");
            assert patient.getId() > 0;
            printPass("✓ Patient created with ID: " + patient.getId());

            // Test get patient
            Patient retrieved = service.getPatientById(patient.getId());
            assert retrieved != null;
            assert retrieved.getName().equals("Priya Patel");
            printPass("✓ Patient retrieved by ID");

            // Test search by name
            List<Patient> results = service.searchByName("Priya");
            assert results.size() > 0;
            printPass("✓ Patient found by name search");

            // Test search by age
            List<Patient> byAge = service.searchByAge(28);
            assert byAge.size() > 0;
            printPass("✓ Patients found by age");

            // Test blood group search
            List<Patient> byBloodGroup = service.searchByBloodGroup("A+");
            assert byBloodGroup.size() > 0;
            printPass("✓ Patients found by blood group");

            // Test average age calculation
            double avgAge = service.getAveragePatientAge();
            assert avgAge > 0;
            printPass("✓ Average patient age calculated: " + String.format("%.1f", avgAge));
        } catch (InvalidDataException e) {
            printFail("PatientService test failed: " + e.getMessage());
        }
    }

    private static void testAppointmentService() {
        printTest("Testing AppointmentService.java");

        try {
            // Create services and data
            PatientService patientService = new PatientService();
            DoctorService doctorService = new DoctorService();
            AppointmentService appointmentService = new AppointmentService();

            Patient patient = patientService.addPatient("Amit Kumar", 35, "M", "9123456789", "B+", "Diabetes");
            Doctor doctor = doctorService.addDoctor("Dr. Neha Singh", 40, "F", "9876543221", "Neurology", 15, 600.0);

            // Test book appointment
            LocalDate futureDate = DateUtil.getTodayDate().plusDays(3);
            LocalTime appointmentTime = LocalTime.of(10, 0);

            Appointment apt = appointmentService.bookAppointment(patient, doctor, futureDate, appointmentTime);
            assert apt.getAppointmentId() > 0;
            assert apt.getAppointmentStatus().equals("CONFIRMED");
            printPass("✓ Appointment booked: ID " + apt.getAppointmentId() + " Status: CONFIRMED");

            // Test get appointment
            Appointment retrieved = appointmentService.searchById(apt.getAppointmentId());
            assert retrieved != null;
            printPass("✓ Appointment retrieved by ID");

            // Test search by patient ID
            List<Appointment> patientApts = appointmentService.searchByPatientId(patient.getId());
            assert patientApts.size() > 0;
            printPass("✓ Appointments found for patient: " + patientApts.size());

            // Test search by doctor ID
            List<Appointment> doctorApts = appointmentService.searchByDoctorId(doctor.getId());
            assert doctorApts.size() > 0;
            printPass("✓ Appointments found for doctor: " + doctorApts.size());

            // Test available slots
            List<LocalTime> slots = appointmentService.getAvailableTimeSlots(doctor.getId(), futureDate);
            int bookedCount = 18 - slots.size();
            printPass("✓ Available slots: " + slots.size() + "/18 (booked: " + bookedCount + ")");

            // Test cancel appointment
            Appointment cancelled = appointmentService.cancelAppointment(apt.getAppointmentId());
            assert cancelled.getAppointmentStatus().equals("CANCELLED");
            printPass("✓ Appointment cancelled: Status changed to CANCELLED");
        } catch (Exception e) {
            printFail("AppointmentService test failed: " + e.getMessage());
        }
    }

    private static void testSearchFunctionality() {
        printTest("Testing Search Functionality");

        try {
            PatientService patientService = new PatientService();
            DoctorService doctorService = new DoctorService();

            // Add multiple records
            patientService.addPatient("John Doe", 30, "M", "9111111111", "O+", "None");
            patientService.addPatient("Jane Doe", 32, "F", "9222222222", "A+", "Asthma");
            patientService.addPatient("Bob Smith", 45, "M", "9333333333", "B+", "Hypertension");

            doctorService.addDoctor("Dr. Arun", 50, "M", "9444444444", "Cardiology", 25, 500.0);
            doctorService.addDoctor("Dr. Bhavna", 40, "F", "9555555555", "Cardiology", 15, 500.0);
            doctorService.addDoctor("Dr. Charu", 35, "F", "9666666666", "Orthopedics", 10, 400.0);

            // Test search by age range
            List<Patient> ageRange = patientService.searchByAgeRange(30, 40);
            assert ageRange.size() == 2;
            printPass("✓ Age range search: 2 patients between 30-40");

            // Test search by specialization count
            long cardiologyCount = doctorService.getDoctorCountBySpecialization("Cardiology");
            assert cardiologyCount == 2;
            printPass("✓ Specialization count: 2 Cardiologists");

            // Test getAll
            assert patientService.getTotalCount() == 3;
            assert doctorService.getTotalCount() == 3;
            printPass("✓ Total counts: 3 patients, 3 doctors");
        } catch (Exception e) {
            printFail("Search functionality test failed: " + e.getMessage());
        }
    }

    private static void testValidationErrors() {
        printTest("Testing Validation Error Handling");

        int errorCount = 0;

        try {
            PatientService service = new PatientService();
            try {
                service.addPatient("A", 30, "M", "9876543210", "O+", "None");
                printFail("Should reject short name");
            } catch (InvalidDataException e) {
                printPass("✓ Short name rejected: " + e.getMessage());
                errorCount++;
            }

            try {
                service.addPatient("John", 150, "M", "9876543210", "O+", "None");
                printFail("Should reject invalid age");
            } catch (InvalidDataException e) {
                printPass("✓ Invalid age rejected");
                errorCount++;
            }

            try {
                service.addPatient("John Doe", 30, "M", "123", "O+", "None");
                printFail("Should reject short phone");
            } catch (InvalidDataException e) {
                printPass("✓ Invalid phone rejected");
                errorCount++;
            }

            try {
                service.addPatient("John Doe", 30, "M", "9876543210", "XX", "None");
                printFail("Should reject invalid blood group");
            } catch (InvalidDataException e) {
                printPass("✓ Invalid blood group rejected");
                errorCount++;
            }

            assert errorCount == 4;
            printPass("✓ All validation errors handled correctly (" + errorCount + "/4)");
        } catch (Exception e) {
            printFail("Validation error test failed: " + e.getMessage());
        }
    }

    private static void testConflictDetection() {
        printTest("Testing Appointment Conflict Detection");

        try {
            PatientService patientService = new PatientService();
            DoctorService doctorService = new DoctorService();
            AppointmentService appointmentService = new AppointmentService();

            Patient patient1 = patientService.addPatient("Conflict Test one", 30, "M", "9111111111", "O+", "None");
            Patient patient2 = patientService.addPatient("Conflict Test two", 35, "F", "9222222222", "A+", "None");
            Doctor doctor = doctorService.addDoctor("Dr. Test", 45, "M", "9333333333", "Surgery", 20, 700.0);

            LocalDate testDate = DateUtil.getTodayDate().plusDays(5);
            LocalTime testTime = LocalTime.of(14, 0);

            // First appointment should succeed
            Appointment apt1 = appointmentService.bookAppointment(patient1, doctor, testDate, testTime);
            printPass("✓ First appointment booked successfully");

            // Second appointment at same time should fail
            try {
                Appointment apt2 = appointmentService.bookAppointment(patient2, doctor, testDate, testTime);
                printFail("Should detect double-booking conflict");
            } catch (InvalidDataException e) {
                printPass("✓ Double-booking conflict detected: " + e.getMessage());
            }

            // Different time should succeed
            Appointment apt3 = appointmentService.bookAppointment(patient2, doctor, testDate, LocalTime.of(14, 30));
            printPass("✓ Different time slot booked successfully");
        } catch (Exception e) {
            printFail("Conflict detection test failed: " + e.getMessage());
        }
    }

    private static void testBillFactory() {
        printTest("Testing BillFactory.java (Factory Pattern)");

        try {
            PatientService patientService = new PatientService();
            DoctorService doctorService = new DoctorService();
            AppointmentService appointmentService = new AppointmentService();

            Patient patient = patientService.addPatient("Bill Test Patient", 40, "M", "9111111111", "B+", "None");
            Doctor doctor = doctorService.addDoctor("Dr. Factory Test", 50, "M", "9222222222", "Surgery", 25, 1000.0);

            LocalDate billDate = DateUtil.getTodayDate().plusDays(2);
            LocalTime billTime = LocalTime.of(11, 0);

            Appointment appointment = appointmentService.bookAppointment(patient, doctor, billDate, billTime);
            printPass("✓ Appointment created for bill factory testing");

            // Test STANDARD billing strategy
            Bill standardBill = BillFactory.createBill(appointment, BillFactory.BillingStrategy.STANDARD, "PENDING");
            assert standardBill.getId() > 0;
            double expectedStandard = 1000.0 * 1.18; // 1000 + 18% tax
            assert Math.abs(standardBill.getAmount() - expectedStandard) < 0.01;
            printPass("✓ STANDARD bill created: Amount ₹" + String.format("%.2f", standardBill.getAmount()) + " (1000 + 18% tax)");

            // Test DISCOUNTED billing strategy (10% off)
            Bill discountedBill = BillFactory.createBill(appointment, BillFactory.BillingStrategy.DISCOUNTED, "PENDING");
            double expectedDiscounted = (1000.0 * 0.9) * 1.18; // 900 + 18% tax
            assert Math.abs(discountedBill.getAmount() - expectedDiscounted) < 0.01;
            printPass("✓ DISCOUNTED bill created: Amount ₹" + String.format("%.2f", discountedBill.getAmount()) + " (10% off + tax)");

            // Test PREMIUM billing strategy (20% markup)
            Bill premiumBill = BillFactory.createBill(appointment, BillFactory.BillingStrategy.PREMIUM, "PAID");
            double expectedPremium = (1000.0 * 1.2) * 1.18; // 1200 + 18% tax
            assert Math.abs(premiumBill.getAmount() - expectedPremium) < 0.01;
            printPass("✓ PREMIUM bill created: Amount ₹" + String.format("%.2f", premiumBill.getAmount()) + " (20% markup + tax)");

            // Test FLAT_RATE billing strategy
            Bill flatRateBill = BillFactory.createBill(appointment, BillFactory.BillingStrategy.FLAT_RATE, "PENDING");
            double expectedFlatRate = Constants.CONSULTATION_FEE_DEFAULT * 1.18;
            assert Math.abs(flatRateBill.getAmount() - expectedFlatRate) < 0.01;
            printPass("✓ FLAT_RATE bill created: Amount ₹" + String.format("%.2f", flatRateBill.getAmount()));

            // Test createStandardBill convenience method
            Bill convenienceBill = BillFactory.createStandardBill(appointment, "PAID");
            assert convenienceBill.getStatus().equals("PAID");
            assert Math.abs(convenienceBill.getAmount() - expectedStandard) < 0.01;
            printPass("✓ createStandardBill convenience method works: Status PAID");

            // Test billing strategy from string
            BillFactory.BillingStrategy strategy = BillFactory.getBillingStrategyFromString("DISCOUNTED");
            assert strategy == BillFactory.BillingStrategy.DISCOUNTED;
            printPass("✓ getBillingStrategyFromString works: 'DISCOUNTED' parsed");

            // Test invalid payment status
            try {
                BillFactory.createBill(appointment, BillFactory.BillingStrategy.STANDARD, "INVALID");
                printFail("Should reject invalid payment status");
            } catch (InvalidDataException e) {
                printPass("✓ Invalid payment status rejected");
            }

            // Test null appointment
            try {
                BillFactory.createBill(null, BillFactory.BillingStrategy.STANDARD, "PENDING");
                printFail("Should reject null appointment");
            } catch (InvalidDataException e) {
                printPass("✓ Null appointment rejected");
            }

            // Test invalid strategy string
            try {
                BillFactory.getBillingStrategyFromString("UNKNOWN_STRATEGY");
                printFail("Should reject invalid strategy string");
            } catch (InvalidDataException e) {
                printPass("✓ Invalid strategy string rejected");
            }

        } catch (Exception e) {
            printFail("BillFactory test failed: " + e.getMessage());
        }
    }

    private static void testBillingService() {
        printTest("Testing BillingService.java");

        try {
            PatientService patientService = new PatientService();
            DoctorService doctorService = new DoctorService();
            AppointmentService appointmentService = new AppointmentService();
            BillingService billingService = new BillingService();

            Patient patient1 = patientService.addPatient("Priya Sharma", 35, "F", "9333333333", "O+", "None");
            Patient patient2 = patientService.addPatient("Ramesh Gupta", 50, "M", "9444444444", "AB+", "Asthma");
            Doctor doctor = doctorService.addDoctor("Dr. Billing", 55, "M", "9555555555", "Dermatology", 30, 800.0);

            LocalDate billDate = DateUtil.getTodayDate().plusDays(1);

            // Create appointments for billing
            Appointment apt1 = appointmentService.bookAppointment(patient1, doctor, billDate, LocalTime.of(9, 0));
            Appointment apt2 = appointmentService.bookAppointment(patient2, doctor, billDate, LocalTime.of(9, 30));

            // Test generateBill
            Bill bill1 = billingService.generateBill(apt1, "PENDING");
            assert bill1.getId() > 0;
            double expectedAmount = 800.0 * 1.18;
            assert Math.abs(bill1.getAmount() - expectedAmount) < 0.01;
            printPass("✓ Bill generated: ID " + bill1.getId() + ", Amount ₹" + String.format("%.2f", bill1.getAmount()));

            // Test second bill
            Bill bill2 = billingService.generateBill(apt2, "PAID");
            assert bill2.getId() > bill1.getId();
            assert bill2.getStatus().equals("PAID");
            printPass("✓ Second bill generated with PAID status");

            // Test createSimpleBill
            Appointment apt3 = appointmentService.bookAppointment(patient1, doctor, billDate, LocalTime.of(10, 0));
            Bill simpleBill = billingService.createSimpleBill(apt3);
            assert simpleBill.getStatus().equals("PENDING");
            printPass("✓ Simple bill created with default PENDING status");

            // Test getBillById
            Bill retrieved = billingService.getBillById(bill1.getId());
            assert retrieved != null;
            assert retrieved.getId() == bill1.getId();
            printPass("✓ Bill retrieved by ID");

            // Test getTotalBillCount
            assert billingService.getTotalBillCount() == 3;
            printPass("✓ Total bill count: " + billingService.getTotalBillCount());

            // Test getPaidBillCount
            assert billingService.getPaidBillCount() == 1;
            printPass("✓ Paid bills count: " + billingService.getPaidBillCount());

            // Test getUnpaidBillCount
            assert billingService.getUnpaidBillCount() == 2;
            printPass("✓ Unpaid bills count: " + billingService.getUnpaidBillCount());

            // Test getTotalRevenue
            double totalRevenue = billingService.getTotalRevenue();
            assert totalRevenue > 0;
            printPass("✓ Total revenue (PAID bills): ₹" + String.format("%.2f", totalRevenue));

            // Test getTotalPendingAmount
            double pendingAmount = billingService.getTotalPendingAmount();
            assert pendingAmount > 0;
            printPass("✓ Total pending amount: ₹" + String.format("%.2f", pendingAmount));

            // Test getAverageBillAmount
            double avgBill = billingService.getAverageBillAmount();
            assert avgBill > 0;
            printPass("✓ Average bill amount: ₹" + String.format("%.2f", avgBill));

            // Test updateBillStatus
            Bill updatedBill = billingService.updateBillStatus(bill1.getId(), "PAID");
            assert updatedBill.getStatus().equals("PAID");
            printPass("✓ Bill status updated to PAID");

            // Verify update affected service state
            assert billingService.getPaidBillCount() == 2;
            assert billingService.getUnpaidBillCount() == 1;
            printPass("✓ Bill counts updated after status change");

            // Test billExists
            assert billingService.billExists(bill1.getId());
            assert !billingService.billExists(99999);
            printPass("✓ billExists method works correctly");

            // Test getAll
            List<Bill> allBills = billingService.getAll();
            assert allBills.size() == 3;
            printPass("✓ Retrieved all bills: " + allBills.size());

            // Test deleteBill
            boolean deleted = billingService.deleteBill(simpleBill.getId());
            assert deleted;
            assert billingService.getTotalBillCount() == 2;
            printPass("✓ Bill deleted successfully");

            // Test invalid bill deletion
            boolean notDeleted = billingService.deleteBill(99999);
            assert !notDeleted;
            printPass("✓ Non-existent bill deletion returns false");

            // Test null appointment error
            try {
                billingService.generateBill(null, "PENDING");
                printFail("Should reject null appointment");
            } catch (InvalidDataException e) {
                printPass("✓ Null appointment rejected");
            }

            // Test invalid payment status
            try {
                billingService.generateBill(apt1, "INVALID_STATUS");
                printFail("Should reject invalid payment status");
            } catch (InvalidDataException e) {
                printPass("✓ Invalid payment status rejected");
            }

            // Test updateBillStatus with invalid bill ID
            try {
                billingService.updateBillStatus(99999, "PAID");
                printFail("Should reject non-existent bill ID");
            } catch (InvalidDataException e) {
                printPass("✓ Non-existent bill ID rejected in update");
            }

        } catch (Exception e) {
            printFail("BillingService test failed: " + e.getMessage());
        }
    }

    private static void testBillingIntegration() {
        printTest("Testing BillFactory & BillingService Integration");

        try {
            PatientService patientService = new PatientService();
            DoctorService doctorService = new DoctorService();
            AppointmentService appointmentService = new AppointmentService();
            BillingService billingService = new BillingService();

            Patient patient = patientService.addPatient("Integration Test Patient", 45, "M", "9666666666", "A+", "None");
            Doctor doctor = doctorService.addDoctor("Dr. Integration", 60, "M", "9777777777", "Oncology", 35, 1500.0);

            LocalDate integrationDate = DateUtil.getTodayDate().plusDays(4);

            // Create multiple appointments with different billing strategies
            Appointment apt1 = appointmentService.bookAppointment(patient, doctor, integrationDate, LocalTime.of(10, 0));
            Appointment apt2 = appointmentService.bookAppointment(patient, doctor, integrationDate, LocalTime.of(10, 30));
            Appointment apt3 = appointmentService.bookAppointment(patient, doctor, integrationDate, LocalTime.of(11, 0));
            Appointment apt4 = appointmentService.bookAppointment(patient, doctor, integrationDate, LocalTime.of(11, 30));

            // Create bills using factory with different strategies
            Bill bill1 = BillFactory.createBill(apt1, BillFactory.BillingStrategy.STANDARD, "PENDING");
            Bill bill2 = BillFactory.createBill(apt2, BillFactory.BillingStrategy.DISCOUNTED, "PENDING");
            Bill bill3 = BillFactory.createBill(apt3, BillFactory.BillingStrategy.PREMIUM, "PAID");
            Bill bill4 = BillFactory.createBill(apt4, BillFactory.BillingStrategy.FLAT_RATE, "PAID");

            // Add factory-created bills to billing service
            billingService.addBillToStore(bill1);
            billingService.addBillToStore(bill2);
            billingService.addBillToStore(bill3);
            billingService.addBillToStore(bill4);

            printPass("✓ Created 4 bills with different strategies and added to service");

            // Verify all bills are stored
            assert billingService.getTotalBillCount() == 4;
            printPass("✓ All 4 bills stored in billing service");

            // Verify revenue calculations
            double totalRevenue = billingService.getTotalRevenue();
            double expectedRevenue = bill3.getAmount() + bill4.getAmount();
            assert Math.abs(totalRevenue - expectedRevenue) < 0.01;
            printPass("✓ Total revenue: ₹" + String.format("%.2f", totalRevenue));

            // Verify pending amount
            double pendingAmount = billingService.getTotalPendingAmount();
            double expectedPending = bill1.getAmount() + bill2.getAmount();
            assert Math.abs(pendingAmount - expectedPending) < 0.01;
            printPass("✓ Total pending: ₹" + String.format("%.2f", pendingAmount));

            // Verify bill counts
            assert billingService.getPaidBillCount() == 2;
            assert billingService.getUnpaidBillCount() == 2;
            printPass("✓ Paid: " + billingService.getPaidBillCount() + ", Unpaid: " + billingService.getUnpaidBillCount());

            // Verify average calculation
            double avgAmount = billingService.getAverageBillAmount();
            double expectedAvg = (bill1.getAmount() + bill2.getAmount() + bill3.getAmount() + bill4.getAmount()) / 4;
            assert Math.abs(avgAmount - expectedAvg) < 0.01;
            printPass("✓ Average bill amount: ₹" + String.format("%.2f", avgAmount));

            // Verify different billing amounts
            assert bill1.getAmount() > bill2.getAmount(); // Standard > Discounted
            assert bill3.getAmount() > bill1.getAmount(); // Premium > Standard
            printPass("✓ Billing strategies produced correct relative amounts");

            // Verify bill details are accessible through service
            Bill retrieved1 = billingService.getBillById(bill1.getId());
            assert retrieved1.getAppointment().getAppointmentId() == apt1.getAppointmentId();
            printPass("✓ Bill appointment link verified through service");

            // Update a factory-created bill status through service
            Bill updated = billingService.updateBillStatus(bill1.getId(), "PAID");
            assert updated.getStatus().equals("PAID");
            assert billingService.getPaidBillCount() == 3;
            printPass("✓ Factory-created bill status updated through service");

        } catch (Exception e) {
            printFail("Billing integration test failed: " + e.getMessage());
        }
    }

    private static void displaySummary() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("📊 TEST SUMMARY");
        System.out.println("=".repeat(80));
        System.out.println("Total Tests Run: " + testsRun);
        System.out.println("Passed: " + testsPassed);
        System.out.println("Failed: " + testsFailed);
        System.out.println("Success Rate: " + (testsRun > 0 ? String.format("%.1f%%", (100.0 * testsPassed / testsRun)) : "0%"));
        System.out.println("=".repeat(80));

        if (testsFailed == 0) {
            System.out.println("✅ ALL TESTS PASSED!");
        } else {
            System.out.println("❌ SOME TESTS FAILED");
        }
        System.out.println("=".repeat(80) + "\n");
    }

    private static void printTest(String message) {
        System.out.println("\n🧪 " + message);
        System.out.println("-".repeat(80));
    }

    private static void printPass(String message) {
        System.out.println("   ✅ " + message);
        testsPassed++;
        testsRun++;
    }

    private static void printFail(String message) {
        System.out.println("   ❌ " + message);
        testsFailed++;
        testsRun++;
    }
}


