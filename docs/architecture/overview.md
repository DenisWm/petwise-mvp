---
layout: default
title: Overview
parent: Architecture
nav_order: 1
---

# Architecture Overview
{: .no_toc }

PetWise demonstrates Clean Architecture and Domain-Driven Design principles in a Spring Boot application.
{: .fs-6 .fw-300 }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Architectural Style

PetWise implements a **Clean Architecture** approach inspired by:

- **Robert C. Martin's Clean Architecture** — Dependency inversion, testability, framework independence
- **Alistair Cockburn's Hexagonal Architecture** — Ports and adapters, isolation of business logic
- **Domain-Driven Design (DDD)** — Ubiquitous language, aggregates, bounded contexts

---

## Layered Architecture

```
┌──────────────────────────────────────────────────┐
│         Infrastructure Layer                      │
│  (Spring Boot, REST, JPA, Adapters)              │
└──────────────────────────────────────────────────┘
                      ↓ depends on
┌──────────────────────────────────────────────────┐
│         Application Layer                         │
│  (Use Cases, Orchestration)                      │
└──────────────────────────────────────────────────┘
                      ↓ depends on
┌──────────────────────────────────────────────────┐
│         Domain Layer                              │
│  (Business Logic, Pure Java)                     │
└──────────────────────────────────────────────────┘
```

### Layer Responsibilities

| Layer | Responsibility | May Depend On | Must Not Depend On |
|:------|:--------------|:--------------|:-------------------|
| **Domain** | Business rules, invariants, domain model | Nothing (pure Java) | Spring, JPA, HTTP, JSON |
| **Application** | Use case orchestration, workflow logic | Domain | Infrastructure, Spring |
| **Infrastructure** | Technical implementation, I/O, frameworks | Application, Domain | — (outermost layer) |

---

## Module Structure

| Module | Type | Purpose | Dependencies |
|:-------|:-----|:--------|:-------------|
| `domain` | Java Library | Aggregates, entities, value objects, business rules | None |
| `application` | Java Library | Use cases, commands, outputs | `domain` |
| `infrastructure` | Spring Boot App | REST controllers, JPA, configuration | `application`, `domain` |
| `build-logic` | Gradle Plugins | Convention plugins | N/A (composite build) |

{: .note }
> Gradle enforces module boundaries at compile time, preventing circular dependencies.

---

## Domain-Driven Design

PetWise uses **tactical DDD patterns**:

### Aggregate Roots (Consistency boundaries)

- **Tutor** — Owns pets, enforces invariants
- **Appointment** — Owns lifecycle, manages status transitions

### Entities

- **Pet** — Has identity, lives inside Tutor aggregate

### Value Objects

- `Email`, `Phone` — Validated, immutable contact info
- `ServiceType` — CRECHE, HOTEL
- `AppointmentStatus` — PENDING, ACTIVE, COMPLETED, CANCELED

{: .note }
For detailed domain model documentation, see [Domain Model](domain).

---

## Use Case Pattern

Application behavior is modeled as **explicit use cases**, not generic service classes.

```java
public abstract class UseCase<INPUT, OUTPUT> {
    public abstract OUTPUT execute(INPUT input);
}
```

**Example:**
```java
public class DefaultCreateTutorUseCase extends CreateTutorUseCase {
    private final TutorGateway tutorGateway;

    @Override
    public CreateTutorOutput execute(CreateTutorCommand aCommand) {
        // 1. Create domain aggregate
        // 2. Validate
        // 3. Save via gateway
        // 4. Return output
    }
}
```

**Benefits:**
- 1:1 mapping between documented use cases (UC-01 through UC-06) and code
- Single responsibility — each use case does one thing
- Easy to test in isolation with mocked gateways

---

## Ports and Adapters

**Domain defines interfaces (ports):**

```java
public interface TutorGateway {
    Tutor save(Tutor tutor);
    Optional<Tutor> findById(TutorID id);
    Pagination<Tutor> findAll(SearchQuery query);
    void deleteById(TutorID id);
}
```

**Infrastructure implements adapters:**

```java
public class TutorPostgresGateway implements TutorGateway {
    private final TutorRepository jpaRepository;

    @Override
    public Tutor save(Tutor tutor) {
        // Map domain → JPA entity → persist → map back to domain
    }
}
```

---

## Vertical Slices

Each feature is implemented as a **vertical slice** through all layers:

```
HTTP Request → Controller → Use Case → Domain Aggregate → Gateway → JPA Repository → Database
```

---

## Build System

PetWise uses **Gradle convention plugins** to centralize build configuration:

| Plugin | Purpose |
|:-------|:--------|
| `petwise.java-library-conventions` | Java 21, JUnit 5, Spotless, JaCoCo |
| `petwise.spring-boot-app-conventions` | Spring Boot application setup |
| `petwise.lint-conventions` | Code formatting (Spotless + Google Java Format) |
| `petwise.jacoco-conventions` | Code coverage reporting |
| `petwise.owasp-dependency-check-conventions` | CVE scanning |

{: .highlight }
For detailed build system documentation, see [Build System Guide](../build-system).

---

## Testing Strategy

```
        ╱╲
       ╱  ╲      Integration Tests
      ╱────╲     (Spring Boot, REST, JPA)
     ╱      ╲
    ╱────────╲   Application Tests
   ╱          ╲  (Use cases + mocked gateways)
  ╱────────────╲
 ╱              ╲ Domain Tests
╱────────────────╲ (Pure unit tests, fast)
```

| Layer | Testing Approach |
|:------|:-----------------|
| **Domain** | Pure unit tests, no mocks, test business rules directly |
| **Application** | Mock gateways, test use case orchestration |
| **Infrastructure** | `@SpringBootTest`, test REST endpoints and JPA repositories |

---

## Key Design Decisions

All architectural decisions are documented as [ADRs](decisions):

- **ADR-0001:** PostgreSQL with H2 for Tests
- **ADR-0002:** Forward-only Appointment Status Lifecycle
- **ADR-0003:** Domain Modeling with DDD Basics
- **ADR-0004:** Repository and Gateway Strategy
- **ADR-0005:** Use Case Pattern

---

## Further Reading

- [Domain Model](domain) — Entities, aggregates, value objects
- [Architecture Decision Records](decisions) — Key design decisions
- [Diagrams](diagrams) — C4 model and sequence diagrams
- [Use Cases](../use-cases) — Functional requirements
- [Build System](../build-system) — Gradle setup
- [API Reference](../api-reference) — REST endpoints
