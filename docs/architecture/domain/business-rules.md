# Business Rules – PetWise MVP

This document defines the core domain rules, invariants, and constraints that govern the PetWise MVP.  
These rules complement the glossary, ERD, ADRs, and use cases, forming the formal domain model.

In the PetWise domain model:

- **Tutor** is an **Aggregate Root**
- **Pet** is an **Entity** inside the Tutor aggregate
- **Appointment** is an **Aggregate Root**
- **ServiceType** and **AppointmentStatus** behave as **Value Objects**
- All invariants are enforced inside the **Domain Layer** (aggregates, entities, value objects),  
  orchestrated by **UseCases** in the Application Layer.

---

## 1. Tutor Rules (Aggregate Root)

### BR-T01 – A tutor must have at least a name
A valid tutor requires a non-empty name.

### BR-T02 – A tutor may have zero or more pets
The Tutor aggregate owns the pet collection.

### BR-T03 – A pet must belong to exactly one tutor
Pet → Tutor is a mandatory relation.  
Pet is an **Entity** inside the Tutor aggregate.

### BR-T04 – A tutor cannot be deleted if they still have pets
Deletion is permitted only when no pets reference the tutor.

---

## 2. Pet Rules (Entity inside Tutor Aggregate)

### BR-P01 – A pet must have a non-empty name
Required for identification and display.

### BR-P02 – A pet may have optional attributes
Species, breed, birth date, and notes are optional.

### BR-P03 – A pet cannot be deleted if it has ACTIVE appointments
The domain prevents inconsistency by blocking deletion.

### BR-P04 – A pet may be deleted if it has only completed or canceled appointments
Historical data does not block deletion.

### BR-P05 – Birth date cannot be in the future
If provided, must represent a valid past date.

---

## 3. Appointment Rules (Aggregate Root)

### BR-A01 – An appointment must belong to exactly one pet
Appointment belongs to a pet but remains its **own aggregate root**.

### BR-A02 – Required fields
Appointments require:
- service type (`CRECHE`, `HOTEL`)
- start date/time
- end date/time

### BR-A03 – Appointment time window must be valid
`start_at` must be strictly earlier than `end_at`.

### BR-A04 – No overlapping appointments per pet
A pet cannot have two appointments whose time windows overlap  
during `PENDING` or `ACTIVE` states.

### BR-A05 – Appointment lifecycle is forward-only
Defined by ADR-0002:

- `PENDING → ACTIVE`
- `ACTIVE → COMPLETED`
- `PENDING → CANCELED`

No backward transitions.

### BR-A06 – Completed appointments cannot be modified
Aggregate is immutable after completion.

### BR-A07 – Canceled appointments cannot transition to another status
Cancellation is a terminal state in the MVP.

### BR-A08 – Appointments must be visible in the daily agenda
If their time window overlaps a given day, they appear in that day’s agenda.

---

## 4. Agenda (Daily View) Rules

### BR-G01 – Agenda must include all appointments that start on the selected day
Regardless of status.

### BR-G02 – Agenda may filter by status and service type
Helps operational clarity.

### BR-G03 – Agenda order is chronological
Primary: `start_at`  
Secondary (optional UI rule): `pet name`

---

## 5. Validation Rules

### BR-V01 – Input validation must follow API guidelines
Errors use RFC 7807 Problem Details.

### BR-V02 – Referential integrity must be enforced
Relationships:
- Pet → Tutor
- Appointment → Pet

### BR-V03 – Domain invariants must be enforced **inside the domain layer**
UseCases orchestrate, but all rules are enforced inside:

- Tutor aggregate
- Pet entity
- Appointment aggregate
- Value objects (e.g., Status, ServiceType)

This replaces older “service layer validations” with  
**true domain validation** enforced by aggregates.

---

## 6. Data Integrity Rules

### BR-DI01 – Identifiers must be unique per entity type
Use either UUIDs or numeric IDs.

### BR-DI02 – Timestamps must follow a consistent format
Recommended: ISO-8601 (UTC or local-with-offset).

### BR-DI03 – Deletion is hard delete in MVP
Soft delete may be added later.

---

## 7. MVP Scope Clarifications

### BR-MVP01 – No capacity constraints
Daycare/hotel space limits are out of scope.

### BR-MVP02 – No multi-pet appointments
Each appointment is for one pet.

### BR-MVP03 – No recurring appointments
Only explicit reservations.

### BR-MVP04 – No audit history for status changes
`COMPLETED` is immutable, but transitions are not logged.

---

# Summary

These business rules define the PetWise domain as implemented using a light tactical DDD approach:

- **Aggregate Roots** protect consistency (Tutor, Appointment)
- **Entities** model dependent objects (Pet)
- **Value Objects** capture behavior (AppointmentStatus, ServiceType)
- **Domain invariants** are enforced inside aggregates
- **UseCases** orchestrate operations without containing business rules

This foundation prepares the system for future expansions such as capacity planning, audit logs, pricing, payment flows, and multi-site operations.
