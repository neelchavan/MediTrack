package main.java.com.airtribe.meditrack.entity;

class Doctor extends Person
{

    
    private String specialization;
    private int experienceYears;
    private double consultationFee;

    // Constructor

    public Doctor(int id, String name, int age, String gender, String phone, String specialization, int experienceYears,
            double consultationFee) {
        super(id, name, age, gender, phone);
        this.specialization = specialization;
        this.experienceYears = experienceYears;
        this.consultationFee = consultationFee;
    }

    // Getters

    public String getSpecialization() {
        return specialization;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public double getConsultationFee() {
        return consultationFee;
    }


      // Setters

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setConsultationFee(double consultationFee) {
        this.consultationFee = consultationFee;
    }

   

     


   

    

    
}
