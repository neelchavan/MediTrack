package main.java.com.airtribe.meditrack.service;

import main.java.com.airtribe.meditrack.entity.Patient;
import main.java.com.airtribe.meditrack.exception.InvalidDataException;
import main.java.com.airtribe.meditrack.interfaces.Searchable;
import main.java.com.airtribe.meditrack.util.DataStore;
import main.java.com.airtribe.meditrack.util.IdGenerator;
import main.java.com.airtribe.meditrack.util.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing Patient entities.
 * Provides CRUD operations and search functionality for patients.
 * Implements Searchable interface for polymorphic search operations.
 */
public class PatientService implements Searchable<Patient> {
    private final DataStore<Patient> patientStore;
    private final IdGenerator idGenerator;

    /**
     * Initializes PatientService with a new data store and singleton IdGenerator.
     */
    public PatientService() {
        this.patientStore = new DataStore<>();
        this.idGenerator = IdGenerator.getInstance();
    }

    public Patient addPatient(String name, int age, String gender, String phone,
                             String bloodGroup, String disease) throws InvalidDataException {
        // Validate all input data
        Validator.validateName(name);
        Validator.validateAge(age);
        Validator.validateGender(gender);
        Validator.validatePhone(phone);
        Validator.validateBloodGroup(bloodGroup);
        Validator.validateMedicalHistory(disease);

        // Generate unique ID
        int patientId = idGenerator.generatePatientId();

        // Create and store patient
        Patient patient = new Patient(patientId, name, age, gender, phone, bloodGroup, disease);
        patientStore.add(patientId, patient);

        return patient;
    }

    public Patient getPatientById(int patientId) {
        return patientStore.get(patientId);
    }

    @Override
    public Patient searchById(int id) {
        return getPatientById(id);
    }

    @Override
    public List<Patient> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String searchName = name.toLowerCase().trim();
        return patientStore.getAll().stream()
                .filter(patient -> patient.getName().toLowerCase().contains(searchName))
                .collect(Collectors.toList());
    }

    public List<Patient> searchByAge(int age) {
        return patientStore.getAll().stream()
                .filter(patient -> patient.getAge() == age)
                .collect(Collectors.toList());
    }

    public List<Patient> searchByAgeRange(int minAge, int maxAge) {
        return patientStore.getAll().stream()
                .filter(patient -> patient.getAge() >= minAge && patient.getAge() <= maxAge)
                .collect(Collectors.toList());
    }

    public List<Patient> searchByBloodGroup(String bloodGroup) {
        if (bloodGroup == null || bloodGroup.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return patientStore.getAll().stream()
                .filter(patient -> patient.getBloodGroup().equalsIgnoreCase(bloodGroup))
                .collect(Collectors.toList());
    }

    public Patient updatePatient(int patientId, String name, int age, String gender, String phone,
                                String bloodGroup, String disease) throws InvalidDataException {
        // Validate patient exists
        Patient existingPatient = getPatientById(patientId);
        if (existingPatient == null) {
            throw new InvalidDataException("Patient with ID " + patientId + " not found");
        }

        // Validate all input data
        Validator.validateName(name);
        Validator.validateAge(age);
        Validator.validateGender(gender);
        Validator.validatePhone(phone);
        Validator.validateBloodGroup(bloodGroup);
        Validator.validateMedicalHistory(disease);

        // Create updated patient object
        Patient updatedPatient = new Patient(patientId, name, age, gender, phone, bloodGroup, disease);
        patientStore.update(patientId, updatedPatient);

        return updatedPatient;
    }

    public boolean deletePatient(int patientId) {
        Patient deleted = patientStore.delete(patientId);
        return deleted != null;
    }

    @Override
    public List<Patient> getAll() {
        return patientStore.getAll();
    }

    @Override
    public int getTotalCount() {
        return patientStore.size();
    }

    public boolean patientExists(int patientId) {
        return patientStore.exists(patientId);
    }

    public double getAveragePatientAge() {
        List<Patient> allPatients = getAll();
        if (allPatients.isEmpty()) {
            return 0;
        }

        return allPatients.stream()
                .mapToInt(Patient::getAge)
                .average()
                .orElse(0);
    }
}


