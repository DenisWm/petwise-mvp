# ADR-0005 – UseCase Pattern Instead of Service Layer

## Status
Accepted (MVP)

## Context
Many Spring-based applications adopt a generic “service layer” with classes named
`XxxService`, mixing responsibilities such as:

- orchestration of workflows,
- domain validation,
- persistence calls,
- sometimes even HTTP-related concerns.

In PetWise, we want:

- clear separation between application workflow and domain rules,
- easier testing of business flows,
- a structure that matches the use case–oriented documentation (UC-01 to UC-06).

We already have:

- **domain module** – aggregates, entities, value objects, domain events, validation
- **application module** – generic abstractions: `UseCase`, `UnitUseCase`, `NullaryUseCase`
- **infrastructure module** – controllers, adapters, persistence, configuration

The question is: should we keep a generic `*Service` layer, or model application behavior as explicit **UseCases**?

## Decision
We will model application behavior using **UseCase classes**, not generic services.

Examples:

- `CreateTutorUseCase`
- `CreatePetUseCase`
- `CreateAppointmentUseCase`
- `ChangeAppointmentStatusUseCase`
- `ViewDailyAgendaUseCase`
- `EditTutorUseCase`, `DeleteTutorUseCase`, etc.

Each UseCase:

- represents a single **application-level operation**,
- orchestrates domain objects and gateways,
- does not contain persistence or HTTP details,
- is the primary entry point from controllers into the application layer.

Controllers call UseCases, and UseCases call **Gateways** (ports) and domain objects.

## Consequences

### Positive

- **Traceability:**  
  There is a 1:1 mapping between documented use cases (UC-01, UC-02, …) and UseCase classes.

- **Testability:**  
  UseCases can be unit/integration tested in isolation, mocking gateways.

- **Separation of concerns:**
    - Controllers: HTTP and request/response mapping
    - UseCases: application workflows and orchestration
    - Domain: rules, invariants, aggregates
    - Infrastructure: actual I/O and persistence

- **Evolution:**  
  Adding, deprecating, or refactoring features is done at the UseCase level, without affecting unrelated flows.

### Negative

- **More classes:**  
  For each use case, we create at least one class instead of reusing a generic service class.

- **Naming discipline required:**  
  The team must keep names aligned with the business (verbs and use case semantics).

## Alternatives Considered

### 1. Single `XxxService` per Aggregate
Example: `TutorService`, `PetService`, `AppointmentService`.

Rejected because:

- Tends to grow with unrelated responsibilities.
- Makes it harder to see exactly which methods correspond to which use cases.
- Complicates testing and reasoning about cross-cutting flows.

### 2. Mix UseCases and Services
Some features as UseCases, others as methods in services.

Rejected for MVP because:

- Inconsistent patterns increase cognitive load.
- Harder to onboard new team members and maintain documentation alignment.

## Notes
This ADR applies to the MVP but is intended to scale.  
If PetWise grows into multiple bounded contexts or microservices, each context/service
can maintain its own UseCase set aligned with its use case catalog.
