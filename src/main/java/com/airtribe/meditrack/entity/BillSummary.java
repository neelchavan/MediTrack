package main.java.com.airtribe.meditrack.entity;

public final class BillSummary {

    private final int totalBills;
    private final double totalRevenue;
    private final double pendingAmount;

    public BillSummary(int totalBills, double totalRevenue, double pendingAmount) {
        this.totalBills = totalBills;
        this.totalRevenue = totalRevenue;
        this.pendingAmount = pendingAmount;
    }

    public int getTotalBills() {
        return totalBills;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public double getPendingAmount() {
        return pendingAmount;
    }
}
