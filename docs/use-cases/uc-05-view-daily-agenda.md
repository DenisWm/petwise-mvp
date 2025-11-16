# UC-05 – View Daily Agenda

## Goal

Allow an attendant or manager to see the list of appointments for a given day, optionally filtered by service type and status.

## Primary Actors

- Attendant
- Manager

## Preconditions

- Appointments may or may not exist for the selected day.
- The user can access the agenda screen.
- Network connectivity to the backend is available.

## Main Flow (Happy Path)

1. The user opens the “Agenda” screen.
2. The system shows a date selector and optional filters:
    - service type (CRECHE, HOTEL),
    - status (PENDING, ACTIVE, COMPLETED, CANCELED).
3. The user selects a date (default: today).
4. The user optionally chooses filters (for example only ACTIVE and CRECHE).
5. The UI sends a request to the backend with date and filters.
6. The backend retrieves matching appointments from the database.
7. The backend returns a paginated list of appointments with pet and tutor information (or references).
8. The UI renders the agenda in a list or table grouped by time.

## Alternative Flows

### A1 – No Appointments Found

1. At step 6, the query returns no results.
2. The backend returns 200 OK with an empty list.
3. The UI displays a friendly empty state message.

### A2 – Invalid Date Parameter

1. At step 5, the date parameter is invalid (bad format or not parseable).
2. The backend returns 400 Bad Request (ProblemDetails).
3. The UI shows a validation error and may reset the date field.

## Postconditions

- The user has a clear view of the agenda for the selected day.
- The user can decide operational actions based on the visible appointments.

## Application Layer Mapping

- Use case: ViewDailyAgendaUseCase (application module)
- Ports used: AppointmentGateway
- Infrastructure entrypoint: AppointmentController (REST)

## Related Artifacts

- Sequence diagram: docs/architecture/sequences/uc05-view-daily-agenda.puml
- API: GET /api/v1/appointments?date=YYYY-MM-DD&status=&type=&page=&size=
