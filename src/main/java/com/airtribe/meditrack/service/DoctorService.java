package main.java.com.airtribe.meditrack.service;

import main.java.com.airtribe.meditrack.entity.Doctor;
import main.java.com.airtribe.meditrack.exception.InvalidDataException;
import main.java.com.airtribe.meditrack.interfaces.Searchable;
import main.java.com.airtribe.meditrack.util.DataStore;
import main.java.com.airtribe.meditrack.util.IdGenerator;
import main.java.com.airtribe.meditrack.util.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing Doctor entities.
 * Provides CRUD operations and search functionality for doctors.
 * Implements Searchable interface for polymorphic search operations.
 */
public class DoctorService implements Searchable<Doctor> {
    private final DataStore<Doctor> doctorStore;
    private final IdGenerator idGenerator;

    public DoctorService() {
        this.doctorStore = new DataStore<>();
        this.idGenerator = IdGenerator.getInstance();
    }

    public Doctor addDoctor(String name, int age, String gender, String phone,
                            String specialization, int experienceYears, double consultationFee)
            throws InvalidDataException {
        // Validate all input data
        Validator.validateName(name);
        Validator.validateAge(age);
        Validator.validateGender(gender);
        Validator.validatePhone(phone);
        Validator.validateSpecialization(specialization);
        Validator.validateExperience(experienceYears);
        Validator.validateConsultationFee(consultationFee);

        // Generate unique ID
        int doctorId = idGenerator.generateDoctorId();

        // Create and store doctor
        Doctor doctor = new Doctor(doctorId, name, age, gender, phone, specialization,
                                  experienceYears, consultationFee);
        doctorStore.add(doctorId, doctor);

        return doctor;
    }

    public Doctor getDoctorById(int doctorId) {
        return doctorStore.get(doctorId);
    }

    @Override
    public Doctor searchById(int id) {
        return getDoctorById(id);
    }

    @Override
    public List<Doctor> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String searchName = name.toLowerCase().trim();
        return doctorStore.getAll().stream()
                .filter(doctor -> doctor.getName().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
    }

    public List<Doctor> searchBySpecialization(String specialization) {
        if (specialization == null || specialization.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String searchSpec = specialization.toLowerCase().trim();
        return doctorStore.getAll().stream()
                .filter(doctor -> doctor.getSpecialization().toLowerCase().contains(searchSpec))
                .collect(Collectors.toList());
    }

    public Doctor updateDoctor(int doctorId, String name, int age, String gender, String phone,
                               String specialization, int experienceYears, double consultationFee)
            throws InvalidDataException {
        // Validate doctor exists
        Doctor existingDoctor = getDoctorById(doctorId);
        if (existingDoctor == null) {
            throw new InvalidDataException("Doctor with ID " + doctorId + " not found");
        }

        // Validate all input data
        Validator.validateName(name);
        Validator.validateAge(age);
        Validator.validateGender(gender);
        Validator.validatePhone(phone);
        Validator.validateSpecialization(specialization);
        Validator.validateExperience(experienceYears);
        Validator.validateConsultationFee(consultationFee);

        // Create updated doctor object
        Doctor updatedDoctor = new Doctor(doctorId, name, age, gender, phone, specialization,
                                         experienceYears, consultationFee);
        doctorStore.update(doctorId, updatedDoctor);

        return updatedDoctor;
    }

    public boolean deleteDoctor(int doctorId) {
        Doctor deleted = doctorStore.delete(doctorId);
        return deleted != null;
    }

    @Override
    public List<Doctor> getAll() {
        return doctorStore.getAll();
    }

    @Override
    public int getTotalCount() {
        return doctorStore.size();
    }

    public boolean doctorExists(int doctorId) {
        return doctorStore.exists(doctorId);
    }

    public long getDoctorCountBySpecialization(String specialization) {
        return searchBySpecialization(specialization).size();
    }

    public void clearAllDoctors() {
        doctorStore.clear();
    }
}


