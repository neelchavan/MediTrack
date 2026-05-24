package main.java.com.airtribe.meditrack.service;

import main.java.com.airtribe.meditrack.entity.Bill;
import main.java.com.airtribe.meditrack.entity.Appointment;
import main.java.com.airtribe.meditrack.entity.Doctor;
import main.java.com.airtribe.meditrack.constants.Constants;
import main.java.com.airtribe.meditrack.util.DataStore;
import main.java.com.airtribe.meditrack.util.IdGenerator;
import main.java.com.airtribe.meditrack.exception.InvalidDataException;
import java.util.List;

/**
 * Service class for managing Bill entities and billing operations.
 * Handles bill creation, payment tracking, and billing analytics.
 * Integrates with appointments for automatic billing as per project requirements.
 */
public class BillingService {
    private final DataStore<Bill> billStore;
    private final IdGenerator idGenerator;

    public BillingService() {
        this.billStore = new DataStore<>();
        this.idGenerator = IdGenerator.getInstance();
    }

    public Bill generateBill(Appointment appointment, String status) throws InvalidDataException {
        if (appointment == null) {
            throw new InvalidDataException("Appointment cannot be null");
        }

        if (!isValidStatus(status)) {
            throw new InvalidDataException("Invalid payment status. Use PAID or PENDING.");
        }

        Doctor doctor = appointment.getDoctor();

        // Get consultation fee from doctor
        double consultationFee = doctor.getConsultationFee();

        // Calculate tax
        double tax = consultationFee * Constants.DEFAULT_TAX_RATE;

        // Calculate total amount
        double totalAmount = consultationFee + tax;

        // Generate bill ID
        int billId = idGenerator.generateBillId();

        // Create and store bill
        Bill bill = new Bill(billId, appointment, totalAmount, status);
        billStore.add(billId, bill);

        return bill;
    }

    // Creates a simple bill with only consultation charge (default status: PENDING)
     public Bill createSimpleBill(Appointment appointment) throws InvalidDataException {
         return generateBill(appointment, "PENDING");
     }

     // Adds an already-created bill to the store (used with Factory Pattern)
     // This allows bills created by BillFactory to be stored and retrieved
     public void addBillToStore(Bill bill) {
         if (bill != null) {
             billStore.add(bill.getId(), bill);
         }
     }

    public Bill getBillById(int billId) {
        return billStore.get(billId);
    }

    public List<Bill> getAll() {
        return billStore.getAll();
    }

//    public List<Bill> getUnpaidBills() {
//        return billStore.getAll().stream()
//                .filter(bill -> "PENDING".equalsIgnoreCase(bill.getStatus()))
//                .collect(Collectors.toList());
//    }


//    public List<Bill> getPaidBills() {
//        return billStore.getAll().stream()
//                .filter(bill -> "PAID".equalsIgnoreCase(bill.getStatus()))
//                .collect(Collectors.toList());
//    }

//    public List<Bill> getBillsForPatient(int patientId) {
//        return billStore.getAll().stream()
//                .filter(bill -> bill.getAppointment().getPatient().getId() == patientId)
//                .collect(Collectors.toList());
//    }

    public Bill updateBillStatus(int billId, String status) throws InvalidDataException {
        Bill bill = getBillById(billId);
        if (bill == null) {
            throw new InvalidDataException("Bill with ID " + billId + " not found");
        }

        if (!isValidStatus(status)) {
            throw new InvalidDataException("Invalid payment status. Use PAID or PENDING.");
        }

        bill.setStatus(status);
        billStore.update(billId, bill);
        return bill;
    }

//    public Bill markAsPaid(int billId) throws InvalidDataException {
//        return updateBillStatus(billId, "PAID");
//    }

    public boolean billExists(int billId) {
        return getBillById(billId) != null;
    }

    public boolean deleteBill(int billId) {
        if (billExists(billId)) {
            billStore.delete(billId);
            return true;
        }
        return false;
    }

    public double getTotalRevenue() {
        return getAll().stream()
                .filter(bill -> "PAID".equalsIgnoreCase(bill.getStatus()))
                .mapToDouble(Bill::getAmount)
                .sum();
    }

    public double getTotalPendingAmount() {
        return getAll().stream()
                .filter(bill -> "PENDING".equalsIgnoreCase(bill.getStatus()))
                .mapToDouble(Bill::getAmount)
                .sum();
    }

    public double getAverageBillAmount() {
        List<Bill> allBills = getAll();
        if (allBills.isEmpty()) {
            return 0;
        }

        return allBills.stream()
                .mapToDouble(Bill::getAmount)
                .average()
                .orElse(0);
    }

    public int getTotalBillCount() {
        return billStore.size();
    }

    public int getPaidBillCount() {
        return (int) getAll().stream()
                .filter(bill -> "PAID".equalsIgnoreCase(bill.getStatus()))
                .count();
    }

    public int getUnpaidBillCount() {
        return (int) getAll().stream()
                .filter(bill -> "PENDING".equalsIgnoreCase(bill.getStatus()))
                .count();
    }

    private boolean isValidStatus(String status) {
        return status != null &&
               (status.equalsIgnoreCase("PAID") || status.equalsIgnoreCase("PENDING"));
    }
}

