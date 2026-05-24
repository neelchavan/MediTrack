package main.java.com.airtribe.meditrack.entity;

class BillSummary
{


    private final int totalBills;
    private final double totalRevenue;
    private final double pendingAmount;

    //  Constrcutors

    public BillSummary(int totalBills, double totalRevenue, double pendingAmount) 
    {
        this.totalBills = totalBills;
        this.totalRevenue = totalRevenue;
        this.pendingAmount = pendingAmount;
    }

    // Getter

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
