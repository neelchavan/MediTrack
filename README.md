# MediTrack

MediTrack is a small, modular Clinic & Appointment Management System written in Core Java. It models patients, doctors, appointments, and billing to demonstrate practical OOP design, simple services, and basic persistence utilities.

## Features (brief)
- Register and manage Patients and Doctors
- Create, view, confirm and cancel Appointments
- Simple billing and bill summary generation
- Search and filter operations (by ID, name, specialization, date, status)
- Console-based menu UI and a manual test runner

## How OOP is used
- Encapsulation: Entities (`Person`, `Doctor`, `Patient`, `Appointment`) expose behavior through getters/setters; fields are private.
- Inheritance: `Person` is a base class extended by `Doctor` and `Patient`.
- Polymorphism: Services and overloads (search methods) provide different behaviors via the same method names.
- Abstraction: Interfaces like `Searchable` and `Payable` define contracts used by services.
- Immutability: `BillSummary` is an immutable record used to keep billing history safe.

## Design patterns used
- Singleton: `IdGenerator` provides a single ID source.
- Factory: `BillFactory` encapsulates creation of bill objects.
- Strategy: Billing calculations are implemented as switchable strategies (used by billing components).
- Observer (simple): `AppointmentNotificationManager` and `ConsoleNotificationObserver` show event notification for appointments.

## Package structure (key folders)
- `src/main/java/com/airtribe/meditrack/` — main application package
  - `entity/` — domain models (`Person`, `Doctor`, `Patient`, `Appointment`, `Bill`, `BillSummary`)
  - `service/` — business logic and CRUD operations (`DoctorService`, `PatientService`, `AppointmentService`, `BillingService`)
  - `util/` — helpers (`Validator`, `DateUtil`, `IdGenerator`, `DataStore`)
  - `observer/` — notification example
  - `exception/`, `constants/`, `enums/`, `interfaces/` — supporting code

## Run / Setup
Refer to `docs/Setup_Instructions.md` for steps to build and run the project (JDK requirements, IDE tips, and command-line options). Follow that file to compile and run the application locally.

