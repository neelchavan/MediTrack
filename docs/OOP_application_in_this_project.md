# OOP Concepts & Design Patterns Used in MediTrack

This short document lists the main object-oriented concepts and design patterns applied in the MediTrack project. It is kept brief and practical — showing which concept/pattern is used and where you can find it in the codebase.

OOP Concepts

- Encapsulation
  - What: Keep data private and expose behavior via methods.
  - Where: Entities like `Person`, `Doctor`, `Patient`, `Appointment` use private fields with getters/setters (see `src/main/java/com/airtribe/meditrack/entity`).

- Inheritance
  - What: Share common behavior through a base class.
  - Where: `Person` is a base class; `Doctor` and `Patient` extend it to add specific fields/behavior.

- Polymorphism
  - What: Same method name, different implementations; runtime dispatch.
  - Where: Service interfaces and overloaded search methods in services (e.g., `searchById`, `searchByName`) and method overriding in entities.

- Abstraction
  - What: Expose essential behavior while hiding implementation details.
  - Where: Interfaces like `Searchable` and `Payable` define contracts; services implement the business logic.

- Immutability
  - What: Objects that cannot be changed after creation (safer for records/history).
  - Where: `BillSummary` is implemented as an immutable record-class to keep billing records consistent.

Design Patterns

- Singleton
  - Purpose: Single shared instance across the app.
  - Where: `IdGenerator` (single point for IDs/configuration).

- Factory
  - Purpose: Encapsulate object creation logic.
  - Where: `BillFactory` creates different `Bill` objects depending on billing rules.

- Strategy
  - Purpose: Make algorithms (billing calculations) interchangeable.
  - Where: Billing strategies used by `BillingService`/`BillFactory` to compute totals and taxes.

- (Optional) Observer
  - Purpose: Notify interested components on events.
  - Where: `AppointmentNotificationManager` and `ConsoleNotificationObserver` demonstrate a simple observer pattern for appointment notifications.
