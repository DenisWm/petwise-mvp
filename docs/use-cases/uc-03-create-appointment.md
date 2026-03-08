---
layout: default
title: UC-03 Create Appointment
parent: Use Cases
nav_order: 3
---

# UC-03 – Create Appointment
{: .no_toc }

Allow an attendant to create a daycare or hotel appointment for a pet.
{: .fs-6 .fw-300 }

---

## Summary

| | |
|:--|:--|
| **Actor** | Attendant |
| **Precondition** | Pet exists and belongs to a registered tutor |
| **Endpoint** | `POST /appointments` → `201 Created` |
| **Use case class** | `DefaultCreateAppointmentUseCase` |
| **Command / Output** | `CreateAppointmentCommand` / `CreateAppointmentOutput` |
| **Gateways** | `AppointmentGateway`, `PetGateway` |

## Flow

1. Attendant submits: **petId**, **serviceType** (DAYCARE/HOTEL), **startAt**, **endAt**, notes (optional)
2. System validates: pet exists, startAt < endAt, serviceType is valid
3. System checks for overlapping PENDING/ACTIVE appointments for the same pet
4. No conflict — system creates appointment with status `PENDING` and returns it

### Errors

| Condition | Response |
|:----------|:---------|
| Pet does not exist | `404 Not Found` |
| startAt ≥ endAt | `400 Bad Request` |
| Overlapping PENDING/ACTIVE appointment | `409 Conflict` |

## Business Rules

| Rule | Description |
|:-----|:------------|
| BR-A02 | Appointment requires service type, start time, and end time |
| BR-A03 | startAt must be before endAt |
| BR-A04 | No overlapping PENDING/ACTIVE appointments for the same pet |

---

## Sequence Diagram

<div style="text-align: center;">
  <img src="{{ site.baseurl }}/assets/diagrams/architecture/sequences/uc03-create-appointment.png" alt="UC-03 Sequence Diagram" />
</div>

{: .note }
> If the diagram is not visible, run `make diagrams publish` from the project root.

📄 [View PlantUML Source](https://github.com/deniswm/petwise-mvp/blob/master/docs/architecture/sequences/uc03-create-appointment.puml)
