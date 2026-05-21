package main.java.com.airtribe.meditrack.entity;

public class Patient extends Person {

    private String bloodGroup;
    private String disease;

    public Patient(int id, String name, int age, String gender, String phone, String bloodGroup, String disease) {
        super(id, name, age, gender, phone);
        this.bloodGroup = bloodGroup;
        this.disease = disease;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getDisease() {
        return disease;
    }

     public void setDisease(String disease) {
        this.disease = disease;
    }

}