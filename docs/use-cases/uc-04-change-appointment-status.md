# UC-04 – Change Appointment Status

## Goal

Allow an attendant to update the status of an existing appointment following the valid lifecycle transitions.

## Primary Actor

- Attendant

## Preconditions

- Appointment exists.
- Appointment is in a state where transitions are allowed.
- User has access to appointment details.

## Valid Status Transitions (MVP)

- PENDING → ACTIVE
- ACTIVE → COMPLETED
- PENDING → CANCELED

## Main Flow (Happy Path)

1. The attendant opens the appointment details screen.
2. The UI displays the list of valid next statuses.
3. The attendant selects the new status.
4. The UI sends the update request to the API.
5. The system loads the appointment by ID.
6. The system validates the requested status transition.
7. The transition is valid.
8. The system updates the appointment in the database.
9. The system returns the updated appointment.
10. The UI shows confirmation.

## Alternative Flows

### A1 – Appointment Not Found

1. At step 5, the system finds no record for the provided ID.
2. Returns 404 Not Found.
3. UI shows an error.

### A2 – Invalid Status Transition

1. At step 6, the validator detects an invalid change (for example COMPLETED to ACTIVE or CANCELED to ACTIVE).
2. The system returns 409 Conflict with code INVALID_STATUS_TRANSITION.
3. UI shows an error explaining the invalid transition.

## Postconditions

- Appointment is updated with the new status.
- The appointment remains visible in agenda queries.
- Status history is not tracked in the MVP (no audit log).

## Application Layer Mapping

- Use case: ChangeAppointmentStatusUseCase (application module)
- Domain rule helper: AppointmentValidator (domain module)
- Ports used: AppointmentGateway
- Infrastructure entrypoint: AppointmentController (REST)

## Related Artifacts

- Sequence diagram: docs/architecture/sequences/uc04-change-appointment-status.puml
- API: PATCH /api/v1/appointments/{id}/status
