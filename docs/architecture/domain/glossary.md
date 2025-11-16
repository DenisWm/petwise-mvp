# Domain Glossary

This glossary defines the core domain terms used in the PetWise MVP and explains how
they are represented in the codebase using the project's DDD building blocks
(`AggregateRoot`, `Entity`, `ValueObject`, `DomainEvent`).

---

## Core Business Concepts

### **Tutor**
Person responsible for one or more pets (the legal owner or guardian).

- **DDD Role:** `Tutor` is an **Aggregate Root**.
- **Reason:** It is the entry point to a cluster of domain objects and maintains the
  relationship with its pets.
- **Identity:** Usually generated (`Identifier`).

---

### **Pet**
Animal registered in the system and linked to a single tutor.

- **DDD Role:** `Pet` is an **Entity**.
- **Reason:** It has identity and lifecycle but depends on the Tutor aggregate.
- **Identity:** Usually generated (`Identifier`).
- **Rules (MVP):**
    - Must belong to exactly one tutor.
    - Can only be deleted when no active appointments exist.

---

### **Appointment**
A scheduled period during which a pet receives daycare (creche) or hotel service.

- **DDD Role:** `Appointment` is an **Aggregate Root**.
- **Reason:** It owns its lifecycle, status transitions, validation rules, and
  invariants independently of other aggregates.
- **Key Invariants:**
    - Valid start/end time.
    - No overlapping `PENDING` or `ACTIVE` appointments for the same pet.
    - Status transitions must follow the allowed lifecycle.

---

### **Service Type**
Enumerates the type of service offered:

- `CRECHE`
- `HOTEL`

Used when creating appointments.

---

### **Appointment Status**
Represents the state of an appointment in the MVP:

- `PENDING` – created but not started.
- `ACTIVE` – the pet is currently in service.
- `COMPLETED` – service finished.
- `CANCELED` – appointment was canceled before completion.

- **DDD Note:** Status is a **Value Object**, part of the appointment aggregate.

---

## Domain Building Blocks (Project-Level DDD Kernel)

The project includes a small **Domain Layer Kernel** to enforce consistency and
modeling discipline across aggregates.

### **Aggregate Root**
`AggregateRoot<ID>`  
Base class for domain types that represent consistency boundaries.

- Ensures business invariants.
- Manages entities/value objects inside the aggregate.
- Owns domain-specific behavior.

### **Entity**
`Entity<ID>`  
Represents a mutable domain object with identity.

- Lives inside an aggregate.
- Cannot be referenced independently outside its root.

### **Value Object**
`ValueObject`  
Immutable object defined solely by its values.

- Examples in future versions: `Email`, `PhoneNumber`, `TimeRange`.
- Must have no identity.

### **Identifier**
`Identifier`  
Strong-typed ID used by entities and aggregates.

- Prevents incorrect mixing of IDs.
- Makes domain model safer and more expressive.

### **Domain Event**
`DomainEvent`  
Represents something meaningful that happened inside an aggregate.

- Currently used as infrastructure for future behavior.
- No concrete events defined in the MVP.

### **Domain Event Publisher**
`DomainEventPublisher`  
Dispatches domain events to subscribers or integration mechanisms.

- Stubbed for now (MVP has no external integration).
- Future-ready for outbox pattern or async messaging.

### **Domain Exception**
`DomainException` / `NoStacktraceRuntimeException`  
Exception used to signal domain rule violations.

- Used for invariants and validation failures.
- Not tied to HTTP; infrastructure handles translation (RFC 7807).

---

## Validation & Error Handling Terms

### **Validator**
`Validator`  
Performs domain-level validations inside aggregates or value objects.

### **Validation Handler / Notification**
`ValidationHandler`, `Notification`  
Collects validation errors in a non-exceptional workflow.

- Often used to gather multiple errors before failing a use case.

---

## Persistence Concepts

### **Gateway (Port) and Repository (Adapter)**
Abstract access layer used by the application to read/write aggregates.

- **Gateway interfaces** (e.g. TutorGateway, PetGateway, AppointmentGateway) live in the **domain module**.
- **Default gateway implementations** (e.g. TutorDefaultGateway) live in the infrastructure module.
- They internally use `XxxJpaRepository` and `XxxJpaEntity` to talk to the database.
- This ensures domain isolation from persistence technologies.

---
