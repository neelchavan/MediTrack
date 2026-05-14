package main.java.com.airtribe.meditrack.entity;

class Bill 
{
    private int billId;
    private Patient patient;
    private double consultationCharge;
    private double medicineCharge;
    private double testCharge;
    private double totalAmount;
    private boolean paymentStatus;


    public Bill(int billId, Patient patient, double consultationCharge, double medicineCharge, double testCharge,   double totalAmount, boolean paymentStatus) 
    {
        this.billId = billId;
        this.patient = patient;
        this.consultationCharge = consultationCharge;
        this.medicineCharge = medicineCharge;
        this.testCharge = testCharge;
        this.totalAmount = totalAmount;
        this.paymentStatus = paymentStatus;
    }

// Getter 

    public int getBillId() {
        return billId;
    }


    public Patient getPatient() {
        return patient;
    }


    public double getConsultationCharge() {
        return consultationCharge;
    }


    public double getMedicineCharge() {
        return medicineCharge;
    }


    public double getTestCharge() {
        return testCharge;
    }


    public double getTotalAmount() {
        return totalAmount;
    }


    public boolean isPaymentStatus() {
        return paymentStatus;
    }


    // Setter

    public void setConsultationCharge(double consultationCharge) {
        this.consultationCharge = consultationCharge;
    }

    public void setMedicineCharge(double medicineCharge) {
        this.medicineCharge = medicineCharge;
    }

    public void setTestCharge(double testCharge) {
        this.testCharge = testCharge;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    
    
}