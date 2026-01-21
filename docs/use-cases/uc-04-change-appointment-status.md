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

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Goal

Allow an attendant to update the status of an existing appointment following the valid lifecycle transitions.

---

## Primary Actor

- Attendant

---

## Preconditions

- Appointment exists
- Appointment is in a state where transitions are allowed
- User has access to appointment details

---

## Valid Status Transitions (MVP)

PENDING → ACTIVE
{: .label .label-green }

ACTIVE → COMPLETED
{: .label .label-blue }

PENDING → CANCELED
{: .label .label-red }

{: .important }
> **Forward-only transitions:** Status can only move forward in the lifecycle. You cannot revert from COMPLETED or CANCELED.

---

## Main Flow (Happy Path)

1. The attendant opens the appointment details screen
2. The UI displays the list of valid next statuses
3. The attendant selects the new status
4. The UI sends the update request to the API
5. The system loads the appointment by ID
6. The system validates the requested status transition
7. The transition is valid
8. The system updates the appointment in the database
9. The system returns the updated appointment
10. The UI shows confirmation

---

## Alternative Flows

### A1 – Appointment Not Found

1. At step 5, the system finds no record for the provided ID
2. Returns 404 Not Found
3. UI shows an error

### A2 – Invalid Status Transition

1. At step 6, the validator detects an invalid change (e.g., COMPLETED to ACTIVE or CANCELED to ACTIVE)
2. The system returns 409 Conflict with code `INVALID_STATUS_TRANSITION`
3. UI shows an error explaining the invalid transition

---

## Postconditions

- Appointment is updated with the new status
- The appointment remains visible in agenda queries
- Status history is not tracked in the MVP (no audit log)

---

## Application Layer Mapping

**Use case:** `ChangeAppointmentStatusUseCase` (application module)  
**Domain rule helper:** `AppointmentValidator` (domain module)  
**Ports used:** `AppointmentGateway`  
**Infrastructure entrypoint:** `AppointmentController` (REST)

---

## Related Artifacts

- **API Endpoint:** `PATCH /api/v1/appointments/{id}/status`
- **API Reference:** [Change Appointment Status](../api-reference#change-appointment-status)
- **Sequence Diagram:** See repository `/docs/architecture/sequences/uc04-change-appointment-status.puml`

<div style="text-align: center;">
  <img src="{{ site.baseurl }}/architecture/sequences/uc04-change-appointment-status.png" alt="UC-04 Sequence Diagram" />
</div>

## Business Rules

- **BR-A05:** Status transitions must follow the allowed lifecycle (forward-only)
- **BR-A06:** Completed appointments are immutable (cannot change status)

{: .note }
> See [ADR-0002: Appointment Status Model](../architecture/decisions/adr-0002) for the rationale behind forward-only transitions.
