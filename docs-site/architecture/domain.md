---
layout: default
title: Domain Model
parent: Architecture
nav_order: 3
---

# Domain Model
{: .no_toc }

The core business domain of PetWise: tutors, pets, and appointments.
{: .fs-6 .fw-300 }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Overview

PetWise domain model follows **Domain-Driven Design** principles with three main aggregates:

1. **Tutor** - Pet owner/guardian
2. **Pet** - Animal under care
3. **Appointment** - Scheduled veterinary visit

---

## Entities

### Tutor

**Purpose:** Represents a pet owner or guardian.

**Attributes:**
- `id: TutorId` - Unique identifier
- `name: String` - Required, non-empty
- `email: Email` - Optional, must be valid format
- `phone: Phone` - Optional, must be valid format

**Business Rules:**
- Name cannot be empty
- Email must be valid format (if provided)
- Phone must be valid format (if provided)
- A tutor can have multiple pets

**Invariants:**
- ID is immutable once created
- Name is always present

---

### Pet

**Purpose:** Represents an animal under veterinary care.

**Attributes:**
- `id: PetId` - Unique identifier
- `name: String` - Required, non-empty
- `species: String` - Required (e.g., "Dog", "Cat")
- `breed: String` - Optional
- `tutorId: TutorId` - Required, reference to owner

**Business Rules:**
- Name cannot be empty
- Species cannot be empty
- Must be associated with a valid tutor
- Tutor must exist when creating/updating pet

**Invariants:**
- ID is immutable once created
- Must always have a valid tutor reference

---

### Appointment

**Purpose:** Represents a scheduled veterinary appointment.

**Attributes:**
- `id: AppointmentId` - Unique identifier
- `petId: PetId` - Required, reference to pet
- `scheduledAt: LocalDateTime` - Required, future date/time
- `status: AppointmentStatus` - Required, follows lifecycle
- `reason: String` - Optional, appointment purpose
- `notes: String` - Optional, additional notes

**Business Rules:**
- Pet must exist
- Scheduled time must be in the future (when creating)
- Status transitions must follow valid lifecycle
- Cannot schedule multiple appointments for same pet at same time

**Status Lifecycle:**
```
SCHEDULED → CONFIRMED → IN_PROGRESS → COMPLETED
         ↘ CANCELLED
```

**Valid Transitions:**
- `SCHEDULED` → `CONFIRMED`
- `SCHEDULED` → `CANCELLED`
- `CONFIRMED` → `IN_PROGRESS`
- `CONFIRMED` → `CANCELLED`
- `IN_PROGRESS` → `COMPLETED`

**Invariants:**
- ID is immutable once created
- Status transitions are validated
- Scheduled time cannot be changed once confirmed

---

## Value Objects

### Email

**Purpose:** Encapsulate email validation logic.

**Validation:**
- Must match email format regex
- Cannot be null when provided

### Phone

**Purpose:** Encapsulate phone number validation.

**Validation:**
- Must match phone format regex
- Cannot be null when provided

### AppointmentStatus

**Purpose:** Represent appointment lifecycle state.

**Values:**
- `SCHEDULED` - Initial state, awaiting confirmation
- `CONFIRMED` - Confirmed by tutor/clinic
- `IN_PROGRESS` - Currently happening
- `COMPLETED` - Finished successfully
- `CANCELLED` - Cancelled before completion

---

## Aggregates

### Appointment Aggregate

**Aggregate Root:** `Appointment`

**Responsibilities:**
- Enforce status transition rules
- Validate business invariants
- Maintain consistency within appointment lifecycle

**Why it's an Aggregate:**
- Status is part of appointment lifecycle
- Transitions must be atomic
- External access only through appointment root

---

## Entity Relationships

```
Tutor (1) ----< (many) Pet
  |
  | (via Pet)
  |
  +----< (many) Appointment
```

**Relationships:**
- A **Tutor** can have many **Pets**
- A **Pet** belongs to one **Tutor**
- An **Appointment** is for one **Pet** (and thus one **Tutor**)
- A **Pet** can have many **Appointments**

---

## Business Rules Summary

### Cross-Aggregate Rules

1. **Cannot delete a tutor with existing pets**
   - Must delete/reassign pets first

2. **Cannot delete a pet with scheduled appointments**
   - Must complete/cancel appointments first

3. **Appointments inherit tutor from pet**
   - Pet must exist when creating appointment

### Invariant Protection

All business rules are enforced at the **domain layer**:
- Entities validate their own state
- Use cases orchestrate multi-entity operations
- Infrastructure layer is dumb - just persistence

---

## Glossary

| Term | Definition |
|:-----|:-----------|
| **Tutor** | Pet owner or guardian |
| **Pet** | Animal under veterinary care |
| **Appointment** | Scheduled veterinary visit |
| **Aggregate** | Consistency boundary in DDD |
| **Entity** | Domain object with unique identity |
| **Value Object** | Immutable object without identity |
| **Invariant** | Business rule that must always be true |

---

## Full Documentation

Detailed domain documentation available in repository:

- 📄 [Business Rules](https://github.com/deniswm/petwise-mvp/blob/master/docs/architecture/domain/business-rules.md)
- 📄 [Glossary](https://github.com/deniswm/petwise-mvp/blob/master/docs/architecture/domain/glossary.md)
- 📄 [ERD Diagram](https://github.com/deniswm/petwise-mvp/blob/master/docs/architecture/domain/erd.puml)

---

## Related

- [Architecture Decision Records](decisions) - Why we made these design choices
- [Use Cases](../use-cases) - How the domain is used
- [API Reference](../api-reference) - REST API for domain operations

