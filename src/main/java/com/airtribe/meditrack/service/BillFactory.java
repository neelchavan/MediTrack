package main.java.com.airtribe.meditrack.service;

import main.java.com.airtribe.meditrack.entity.Bill;
import main.java.com.airtribe.meditrack.entity.Appointment;
import main.java.com.airtribe.meditrack.constants.Constants;
import main.java.com.airtribe.meditrack.util.IdGenerator;
import main.java.com.airtribe.meditrack.exception.InvalidDataException;

/**
 * BillFactory - Factory Pattern Implementation
 *
 * Creates different types of bills based on billing strategy.
 * This provides a centralized way to create bills with appointment integration.
 * Bills are generated based on different calculation strategies.
 *
 * Design Pattern: Factory Method Pattern
 * Benefits:
 * - Encapsulates bill creation logic
 * - Easy to add new billing strategies
 * - Consistent bill creation process
 * - Supports appointment-based billing
 * - Extensible for future billing methods
 */
public class BillFactory {
    private static final IdGenerator idGenerator = IdGenerator.getInstance();

    public enum BillingStrategy {
        STANDARD("Standard Consultation Billing"),
        DISCOUNTED("Discounted Rate (10% off)"),
        PREMIUM("Premium Service (20% markup)"),
        FLAT_RATE("Flat Rate Billing");

        private final String description;

        BillingStrategy(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    // Creates a bill for an appointment using the specified billing strategy
    // Applies tax calculation based on Constants.DEFAULT_TAX_RATE
    public static Bill createBill(Appointment appointment, BillingStrategy strategy, String paymentStatus)
            throws InvalidDataException {

        if (appointment == null) {
            throw new InvalidDataException("Appointment cannot be null");
        }

        if (!isValidPaymentStatus(paymentStatus)) {
            throw new InvalidDataException("Invalid payment status. Use PAID or PENDING.");
        }

        // Get base consultation fee from doctor
        double baseConsultationFee = appointment.getDoctor().getConsultationFee();
        double billAmount = 0;

        // Apply strategy-based calculation
        switch (strategy) {
            case STANDARD:
                // Standard: Consultation fee + tax
                billAmount = applyTax(baseConsultationFee);
                break;

            case DISCOUNTED:
                // Discounted: 10% off consultation fee, then apply tax
                double discountedFee = baseConsultationFee * 0.9;  // 10% discount
                billAmount = applyTax(discountedFee);
                break;

            case PREMIUM:
                // Premium: 20% markup on consultation fee, then apply tax
                double premiumFee = baseConsultationFee * 1.2;  // 20% markup
                billAmount = applyTax(premiumFee);
                break;

            case FLAT_RATE:
                // Flat rate: Fixed amount from constants, apply tax
                billAmount = applyTax(Constants.CONSULTATION_FEE_DEFAULT);
                break;

            default:
                throw new InvalidDataException("Unknown billing strategy: " + strategy);
        }

        // Generate bill ID
        int billId = idGenerator.generateBillId();

        // Create and return bill linked to appointment
        return new Bill(billId, appointment, billAmount, paymentStatus);
    }

    // Creates a bill using STANDARD strategy (consultation + tax)
    public static Bill createStandardBill(Appointment appointment, String paymentStatus)
            throws InvalidDataException {
        return createBill(appointment, BillingStrategy.STANDARD, paymentStatus);
    }

    // Gets billing strategy from string
    public static BillingStrategy getBillingStrategyFromString(String strategyStr) throws InvalidDataException {
        try {
            return BillingStrategy.valueOf(strategyStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidDataException("Invalid billing strategy: " + strategyStr);
        }
    }

    private static double applyTax(double baseAmount) {
        return baseAmount * (1 + Constants.DEFAULT_TAX_RATE);
    }

    private static boolean isValidPaymentStatus(String status) {
        return status != null &&
               (status.equalsIgnoreCase("PAID") || status.equalsIgnoreCase("PENDING"));
    }
}

