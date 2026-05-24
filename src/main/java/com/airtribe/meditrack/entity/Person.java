package main.java.com.airtribe.meditrack.entity;

class Person
{
    private int id;
    private String name;
    private int age;
    private String gender;
    private String phone;


    public Person(int id, String name, int age, String gender, String phone) {
        this.id   = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
    }


    public int getId()
    {
        return id;
    }

    
    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getAge() {
        return age;
    }


    public void setAge(int age) {
        this.age = age;
    }


    public String getGender() {
        return gender;
    }


    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    

    
}