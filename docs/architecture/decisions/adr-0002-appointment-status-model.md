# ADR-0002 – Appointment Status Model

- **Status**: Accepted
- **Date**: 2025-11-13
- **Authors**: PetWise Architecture Team

## Context

The MVP requires a simple yet reliable workflow for tracking the lifecycle of a pet appointment.  
The appointment may represent either:

- a **daycare (creche)** reservation, or
- a **hotel** reservation.

Since both scenarios share a similar operational flow, we needed a unified state model that:

1. Supports the basic operational workflow.
2. Prevents invalid transitions (e.g., reactivating a finished appointment).
3. Is easy to understand and extend in future iterations.

## Decision

Use a **three-stage forward-only lifecycle**, represented by these states:

1. **PENDING**
    - The appointment exists but has not begun.
    - User may edit or cancel it.

2. **ACTIVE**
    - The pet is currently in service.
    - Appointment is “in execution”.

3. **COMPLETED**
    - Service is finished.
    - Record becomes immutable (no further transitions).

A fourth state, **CANCELED**, is included but not part of the forward lifecycle.  
It is terminal and may only be reached from `PENDING`.

### Valid Transitions

| From        | To           | Reason |
|-------------|--------------|--------|
| PENDING     | ACTIVE       | Appointment begins. |
| ACTIVE      | COMPLETED    | Service ends. |
| PENDING     | CANCELED     | Appointment withdrawn before start. |

### Invalid Transitions

- COMPLETED → ACTIVE
- CANCELED → ACTIVE
- COMPLETED → PENDING
- ACTIVE → PENDING
- CANCELED → COMPLETED
- Any “backwards” transitions

These invalid transitions prevent inconsistencies such as:

- reactivating finished services,
- “time-traveling” status changes,
- reusing old appointments,
- bypassing operational checks.

## Consequences

### Positive

- **Simple** lifecycle easy for attendants to understand.
- **Unambiguous** operational meaning for each state.
- **Enforces data correctness** by preventing illegal transitions.
- **Extensible**: if future fe
