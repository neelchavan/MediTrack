package main.java.com.airtribe.meditrack.entity;

public class Doctor extends Person {
    
    private String specialization;
    private int experienceYears;
    private double consultationFee;

    public Doctor(int id, String name, int age, String gender, String phone, String specialization, int experienceYears, double consultationFee) {
        super(id, name, age, gender, phone);
        this.specialization = specialization;
        this.experienceYears = experienceYears;
        this.consultationFee = consultationFee;
    }

    public String getSpecialization() {
        return specialization;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public double getConsultationFee() {
        return consultationFee;
    }
}
