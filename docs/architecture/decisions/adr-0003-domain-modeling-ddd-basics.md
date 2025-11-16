# ADR-0003 – Domain Modeling Strategy (DDD Kernel)

## Status
Accepted (MVP)

## Context
As the PetWise MVP grows, the team decided to use a lightweight Domain-Driven Design
approach to keep business rules explicit, testable, and isolated from infrastructure.

We already maintain three modules:

- **domain** – pure business rules, aggregates, value objects, invariants
- **application** – use cases, orchestrating domain behavior
- **infrastructure** – controllers, repositories, persistence, configuration

To support this layered architecture, a reusable “DDD kernel” was added to the domain module,
containing:

- `AggregateRoot<ID>`
- `Entity<ID>`
- `ValueObject`
- `Identifier`
- Validation helpers (`Validator`, `Notification`)
- Domain exceptions
- Domain events (`DomainEvent`, `DomainEventPublisher`)

These abstractions encourage consistency and allow developers to express the domain
explicitly without repeating boilerplate.

## Decision
We adopt a minimal DDD modeling strategy:

### Aggregate Roots
Two aggregate roots exist in the MVP:

- **Tutor**  
  Owns pets. Only the aggregate root may manage the pet collection.

- **Appointment**  
  Owns appointment lifecycle, status transitions, and time-range invariants.

### Entities
- **Pet**  
  Lives inside the Tutor aggregate. Cannot exist without a Tutor.

### Value Objects
No explicit value objects are used in the MVP yet, but the base `ValueObject` class
is ready for future concepts (email, phone, time range, etc.).

### Domain Events
No domain events are produced in the MVP, but the kernel supports them for future features
such as notifications, integrations, or outbox patterns.

## Consequences
- Business rules are centralized inside the aggregates.
- Persistence is treated as an implementation detail (repositories/gateways).
- UseCases orchestrate operations across aggregates but do not contain business logic.
- Controllers become thin adapters.
- Future features such as audit logging, event sourcing, or integrations become easier.

## Alternatives Considered
### 1. Anemic Domain Model
Rejected because:
- Business rules leak into application or infrastructure.
- Harder to maintain invariants.
- Leads to duplicated validation logic.

### 2. Full Tactical DDD + Modules per bounded context
Rejected for MVP:
- Overkill for the current scope.
- Adds unnecessary cognitive load and file fragmentation.

## Notes
This ADR should evolve as the domain grows. When new aggregates or value objects appear,
they should be documented here.
