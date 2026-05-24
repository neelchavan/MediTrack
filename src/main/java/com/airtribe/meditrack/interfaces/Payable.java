package main.java.com.airtribe.meditrack.interfaces;

/**
 * Interface for entities that are payable (bills, invoices, etc.).
 * Defines contract for payment-related operations.
 */
public interface Payable {

    double calculateTotalAmount();

    boolean getPaymentStatus();

    void markAsPaid();

    String getPaymentDetails();
}

