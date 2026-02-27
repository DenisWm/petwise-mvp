# UC-03 – Create Appointment

> Allow an attendant to create a daycare (creche) or hotel appointment for a pet.

## Goal

Allow an attendant to create a daycare (creche) or hotel appointment for a pet.

## Primary Actor

- Attendant

## Preconditions

- The pet exists and belongs to a registered tutor.

## Main Flow (Happy Path)

1. The attendant submits: petId, serviceType (CRECHE or HOTEL), startAt, endAt, notes (optional)
2. The system validates: pet exists, startAt is before endAt, serviceType is valid
3. The system checks for overlapping PENDING or ACTIVE appointments for that pet
4. No conflict found — the system creates a new appointment with status PENDING
5. The system returns the created appointment

## Alternative Flows

### A1 – Pet Not Found

1. At step 2, the pet does not exist
2. The system returns 404 Not Found

### A2 – Invalid Date Range

1. At step 2, startAt ≥ endAt
2. The system returns 400 Bad Request

### A3 – Overlapping Appointment

1. At step 3, a conflicting PENDING or ACTIVE appointment exists
2. The system returns 409 Conflict

## Postconditions

- An appointment with status PENDING is stored in the database
- It is linked to the specified pet
- It is visible in the daily agenda

## Application Layer Mapping

- **Use case class:** `DefaultCreateAppointmentUseCase`
- **Command:** `CreateAppointmentCommand`
- **Output:** `CreateAppointmentOutput`
- **Domain validator:** `AppointmentValidator`
- **Ports used:** `AppointmentGateway`, `PetGateway`
- **Controller:** `AppointmentController`

## API Endpoint

- **Method & Path:** `POST /appointments`
- **Success:** `201 Created` with appointment details
- **Errors:** `400 Bad Request`, `404 Not Found` (pet), `409 Conflict` (overlap)

## Business Rules

- **BR-A02:** Appointment requires service type, start time, and end time
- **BR-A03:** startAt must be before endAt
- **BR-A04:** No overlapping PENDING/ACTIVE appointments for the same pet


