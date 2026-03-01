## Goal

Update the status of an existing appointment following the valid lifecycle transitions.

## Primary Actor

- Attendant

## Preconditions

- The appointment exists and is in a state where transitions are allowed.

## Valid Status Transitions

```
PENDING  → ACTIVE
PENDING  → CANCELED
ACTIVE   → COMPLETED
```

COMPLETED and CANCELED are terminal states — no further transitions.

## Main Flow (Happy Path)

1. The attendant submits the appointment ID and the desired new status
2. The system loads the appointment by ID
3. The system validates the requested status transition
4. The transition is valid — the system updates the appointment
5. The system returns the updated appointment

## Alternative Flows

### A1 – Appointment Not Found

1. At step 2, no appointment exists for the given ID
2. The system returns 404 Not Found

### A2 – Invalid Status Transition

1. At step 3, the transition is not allowed (e.g., COMPLETED → ACTIVE)
2. The system returns 409 Conflict

## Postconditions

- The appointment status is updated in the database
- The appointment remains visible in agenda queries

## Application Layer Mapping

- **Use case class:** `DefaultChangeAppointmentStatusUseCase`
- **Command:** `ChangeAppointmentStatusCommand`
- **Output:** `ChangeAppointmentStatusOutput`
- **Ports used:** `AppointmentGateway`
- **Controller:** `AppointmentController`

## API Endpoint

- **Method & Path:** `PATCH /appointments/{id}/status`
- **Request body:** `{ "status": "ACTIVE" }`
- **Success:** `200 OK` with updated appointment
- **Errors:** `404 Not Found`, `409 Conflict` (invalid transition)

## Business Rules

- **BR-A05:** Status transitions must follow the forward-only lifecycle
- **BR-A06:** Completed appointments are immutable
- **BR-A07:** Canceled appointments cannot transition to another status


