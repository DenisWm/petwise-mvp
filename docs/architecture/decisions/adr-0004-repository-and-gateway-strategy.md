# ADR-0004 – Repository and Gateway Strategy

## Status
Accepted (MVP)

## Context
The PetWise MVP follows a layered architecture:

- domain – aggregates, entities, value objects, domain events, validation
- application – use cases orchestrating domain behavior
- infrastructure – controllers, persistence, configuration, adapters

We need a clear strategy for how the application layer accesses persistence while keeping:

- the domain model free from infrastructure concerns (JPA, SQL, etc.)
- the application layer focused on use cases, not database details
- the infrastructure layer responsible for actual I/O and mapping

At the same time, we want to keep the design simple and practical for an MVP.

## Decision


### 1. Gateways (Ports)
We define **gateway interfaces** that express what the application needs from persistence:

- TutorGateway
- PetGateway
- AppointmentGateway

These interfaces are defined in the **domain module**, close to aggregates and ubiquitous language, and
hide concrete persistence technology. UseCases depend only on these gateways, not on JPA or SQL specifics.

### 2. Default Gateways and Repositories (Adapters)
For the MVP:

- Each gateway is implemented by a `XxxDefaultGateway` class in the infrastructure module.
- Each `XxxDefaultGateway` uses a Spring Data `XxxJpaRepository` internally.
- JPA entities follow the `XxxJpaEntity` naming convention.

Example structure:

- `TutorGateway` (domain, port)
- `TutorDefaultGateway` (infrastructure, adapter)
- `TutorJpaRepository` + `TutorJpaEntity` (infrastructure, persistence detail)

These classes are responsible for mapping between domain aggregates/entities and database models.
For the MVP:

- Gateways are implemented by JPA-based repositories in the infrastructure module.
- Implementations have names like:
    - JpaTutorRepository
    - JpaPetRepository
    - JpaAppointmentRepository

These classes:

- Implement the corresponding gateway interface.
- Are responsible for mapping between domain aggregates/entities and database models.
- Use Spring Data JPA or plain JPA as needed.

### 3. UseCases depend on Gateways only
UseCases (for example CreateTutorUseCase, CreatePetUseCase, CreateAppointmentUseCase) receive gateway interfaces via constructor injection.

They:

- Do not know about JPA, EntityManager, or SQL.
- Work purely with domain objects (aggregates, entities, value objects).
- Delegate persistence concerns to gateways.

### 4. Domain Layer stays persistence-agnostic
The domain module:

- Knows nothing about JPA annotations, repositories, or data sources.
- Exposes domain types such as Tutor, Pet, Appointment, and base types like AggregateRoot, Entity, ValueObject.
- Can be tested in isolation without the database.

## Consequences

### Positive
- Clear separation of concerns:
    - Controllers: HTTP and request/response mapping.
    - UseCases: application workflows.
    - Domain: rules and invariants.
    - Repositories: persistence details.

- Testing becomes easier:
    - UseCases can be tested with in-memory gateway implementations or mocks.
    - Domain can be tested without any infrastructure.

- Future-proof:
    - Changing from SQLite to PostgreSQL or another database does not affect domain or use cases.
    - Additional adapters (for example, external APIs or event stores) can implement the same gateways.

### Negative
- Slightly more boilerplate:
    - Interfaces (gateways) + concrete implementations (JPA repositories).
- For very simple CRUD, this can feel more complex than using Spring Data repositories directly in controllers or services.

## Alternatives Considered

### 1. Direct Use of Spring Data Repositories in UseCases/Services
Rejected because:

- Couples application layer directly to Spring Data and JPA abstractions.
- Makes it harder to test use cases without a Spring context.
- Leaks persistence details into business code.

### 2. Domain Layer Owning Repositories
Rejected because:

- Violates the idea of domain purity.
- Forces domain module to depend on infrastructure frameworks or annotations.
- Increases coupling and reduces portability.

## Notes
This ADR describes the MVP strategy. If new bounded contexts or more complex persistence requirements appear (multi-database, outbox pattern, CQRS), this strategy may be refined or extended in future ADRs.
