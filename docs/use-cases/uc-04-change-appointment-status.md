---
layout: default
title: UC-04 Change Appointment Status
parent: Use Cases
nav_order: 4
---

# UC-04 – Change Appointment Status
{: .no_toc }

Allow an attendant to update the status of an existing appointment following valid lifecycle transitions.
{: .fs-6 .fw-300 }

---

## Summary

| | |
|:--|:--|
| **Actor** | Attendant |
| **Precondition** | Appointment exists and is not in a terminal state |
| **Endpoint** | `PATCH /appointments/{id}/status` → `200 OK` |
| **Use case class** | `DefaultChangeAppointmentStatusUseCase` |
| **Command / Output** | `ChangeAppointmentStatusCommand` / `ChangeAppointmentStatusOutput` |
| **Gateways** | `AppointmentGateway` |

## Valid Transitions

```
PENDING  → ACTIVE
PENDING  → CANCELED
ACTIVE   → COMPLETED
```

COMPLETED and CANCELED are terminal — no further transitions allowed.

## Flow

1. Attendant submits the appointment ID and desired new status
2. System loads the appointment
3. System validates the transition
4. Valid — system updates and returns the appointment

### Errors

| Condition | Response |
|:----------|:---------|
| Appointment not found | `404 Not Found` |
| Invalid transition (e.g., COMPLETED → ACTIVE) | `409 Conflict` |

## Business Rules

| Rule | Description |
|:-----|:------------|
| BR-A05 | Status transitions follow forward-only lifecycle |
| BR-A06 | Completed appointments are immutable |
| BR-A07 | Canceled appointments cannot transition |

---

## Sequence Diagram

<div style="text-align: center;">
  <img src="{{ site.baseurl }}/assets/diagrams/architecture/sequences/uc04-change-appointment-status.png" alt="UC-04 Sequence Diagram" />
</div>

{: .note }
> If the diagram is not visible, run `make diagrams publish` from the project root.

📄 [View PlantUML Source](https://github.com/deniswm/petwise-mvp/blob/master/docs/architecture/sequences/uc04-change-appointment-status.puml)
