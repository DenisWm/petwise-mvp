---
layout: default
title: UC-03 Create Appointment
parent: Use Cases
nav_order: 3
---

# UC-03 – Create Appointment
{: .no_toc }

Allow an attendant to create a daycare (creche) or hotel appointment for a pet.
{: .fs-6 .fw-300 }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Goal

Allow an attendant to create a daycare (creche) or hotel appointment for a pet.

---

## Primary Actor

- Attendant

---

## Preconditions

- The pet exists
- The pet belongs to a tutor registered in the system
- The user has access to the appointment creation UI

---

## Main Flow (Happy Path)

1. The attendant selects a pet
2. The system displays the appointment creation form
3. The attendant enters:
   - Service type (CRECHE or HOTEL)
   - Start date/time
   - End date/time
   - Notes (optional)
4. The attendant submits the form
5. The system validates:
   - Pet exists
   - start_at is earlier than end_at
   - Type is a valid enum
6. The system checks for overlapping pending or active appointments for that pet
7. No conflict is found
8. The system creates a new appointment with initial status PENDING
9. The system returns the created appointment
10. The UI shows a success message

---

## Alternative Flows

### A1 – Pet Not Found

1. At step 5, the system confirms the pet does not exist
2. Returns 404 Not Found
3. UI displays an error

### A2 – Invalid Date Range

1. At step 5, the system detects start_at greater than or equal to end_at
2. Returns 400 Bad Request (ProblemDetails)
3. UI asks the user to correct the values

### A3 – Overlapping Appointment

1. At step 6, the validator finds conflicting PENDING or ACTIVE appointments
2. The system returns 409 Conflict with code `OVERLAPPING_APPOINTMENT`
3. UI informs the user the pet is already booked for that time

---

## Postconditions

- An appointment with status PENDING is stored in the database
- It is linked to the selected pet
- It is visible in the daily agenda view

---

## Application Layer Mapping

**Use case:** `CreateAppointmentUseCase` (application module)  
**Domain rule helper:** `AppointmentValidator` (domain module)  
**Ports used:** `AppointmentGateway`, `PetGateway`  
**Infrastructure entrypoint:** `AppointmentController` (REST)

---

## Related Artifacts

- **API Endpoint:** `POST /api/v1/appointments`
- **API Reference:** [Create Appointment](../api-reference#create-appointment)
- **Sequence Diagram:** See repository `/docs/architecture/sequences/uc03-create-appointment.puml`

<div style="text-align: center;">
  <img src="{{ site.baseurl }}/architecture/sequences/uc03-create-appointment.png" alt="UC-03 Sequence Diagram" />
</div>

## Business Rules

- **BR-A02:** Appointment requires service type, start time, and end time
- **BR-A03:** start_at must be before end_at
- **BR-A04:** No overlapping PENDING/ACTIVE appointments for the same pet

{: .warning }
> **Important:** The system prevents double-booking by checking for overlapping appointments in PENDING or ACTIVE status.
