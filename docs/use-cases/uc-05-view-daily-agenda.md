---
layout: default
title: UC-05 View Daily Agenda
parent: Use Cases
nav_order: 5
---

# UC-05 – View Daily Agenda
{: .no_toc }

Allow an attendant or manager to view appointments for a specific date, with optional filters.
{: .fs-6 .fw-300 }

---

## Summary

| | |
|:--|:--|
| **Actors** | Attendant, Manager |
| **Precondition** | User can access the agenda endpoint |
| **Endpoint** | `GET /appointments/agenda?date=YYYY-MM-DD` → `200 OK` |
| **Use case class** | `DefaultViewDailyAgendaUseCase` |
| **Command / Output** | `ViewDailyAgendaCommand` / `ViewDailyAgendaOutput` |
| **Gateways** | `AppointmentGateway.findDailyAgenda(...)` |

## Query Parameters

| Parameter | Type | Required | Default | Description |
|:----------|:-----|:---------|:--------|:------------|
| `date` | LocalDate | Yes | — | `YYYY-MM-DD` |
| `status` | Enum | No | — | PENDING, ACTIVE, COMPLETED, CANCELED |
| `serviceType` | Enum | No | — | DAYCARE, HOTEL |
| `page` | int | No | 0 | Page number (0-based) |
| `perPage` | int | No | 20 | Items per page |
| `sort` | String | No | startAt | Sort field |
| `direction` | String | No | asc | asc / desc |

## Flow

1. User specifies a date and optional status/serviceType filters
2. System queries appointments whose `startAt` falls within that day (UTC)
3. System returns a paginated list sorted by start time

### Errors

| Condition | Response |
|:----------|:---------|
| Missing date parameter | `400 Bad Request` |
| No results | `200 OK` with empty page |

## Business Rules

| Rule | Description |
|:-----|:------------|
| BR-A08 | Agenda includes appointments whose startAt falls on the selected day (UTC) |

---

## Sequence Diagram

<div style="text-align: center;">
  <img src="{{ site.baseurl }}/assets/diagrams/architecture/sequences/uc05-view-daily-agenda.png" alt="UC-05 Sequence Diagram" />
</div>

{: .note }
> If the diagram is not visible, run `make diagrams publish` from the project root.

📄 [View PlantUML Source](https://github.com/deniswm/petwise-mvp/blob/master/docs/architecture/sequences/uc05-view-daily-agenda.puml)
