## Goal

Retrieve a paginated list of appointments for a given day, optionally filtered by status and service type.

## Primary Actors

- Attendant
- Manager

## Preconditions

- The user can access the appointments agenda endpoint.

## Main Flow (Happy Path)

1. The user specifies a date (required) and optionally a status and/or service type filter
2. The system queries for appointments whose start time falls within that day (UTC)
3. The system returns a paginated list of matching appointments, sorted by start time

## Alternative Flows

### A1 – No Appointments Found

1. The query returns no results for the given date/filters
2. The system returns 200 OK with an empty page

### A2 – Missing Date Parameter

1. The date parameter is not provided
2. The system returns 400 Bad Request

## Postconditions

- The user receives a paginated view of appointments for the selected day.

## Application Layer Mapping

- **Use case class:** `DefaultViewDailyAgendaUseCase`
- **Command:** `ViewDailyAgendaCommand`
- **Output:** `ViewDailyAgendaOutput`
- **Domain query:** `AppointmentSearchQuery`
- **Ports used:** `AppointmentGateway.findDailyAgenda(...)`
- **Controller:** `AppointmentController`

## API Endpoint

- **Method & Path:** `GET /appointments/agenda`
- **Query parameters:**

| Parameter | Type | Required | Default | Description |
|:----------|:-----|:---------|:--------|:------------|
| `date` | LocalDate | Yes | — | Date in YYYY-MM-DD format |
| `status` | Enum | No | — | PENDING, ACTIVE, COMPLETED, CANCELED |
| `serviceType` | Enum | No | — | CRECHE, HOTEL |
| `page` | int | No | 0 | Page number (0-based) |
| `perPage` | int | No | 20 | Items per page |
| `sort` | String | No | startAt | Sort field |
| `direction` | String | No | asc | Sort direction (asc/desc) |

- **Success:** `200 OK` with paginated list
- **Error:** `400 Bad Request` (missing or invalid date)

## Business Rules

- **BR-G01:** Agenda includes all appointments whose start time falls on the selected day (UTC)
- **BR-G02:** Agenda may be filtered by status and service type
- **BR-G03:** Default ordering is chronological by `startAt`
