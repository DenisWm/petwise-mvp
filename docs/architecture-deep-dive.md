---
layout: default
title: Architecture Deep Dive
nav_order: 5
---

# Architecture Deep Dive
{: .no_toc }

An in-depth exploration of PetWise's architecture, design patterns, and technical decisions.
{: .fs-6 .fw-300 }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Architectural Style

PetWise implements a **Clean Architecture** approach inspired by:

- **Robert C. Martin's Clean Architecture** – Dependency inversion, testability, framework independence
- **Alistair Cockburn's Hexagonal Architecture** – Ports and adapters, isolation of business logic
- **Domain-Driven Design (DDD)** – Ubiquitous language, aggregates, bounded contexts

### Core Principles

1. **Dependency Rule** – Dependencies point inward (infrastructure → application → domain)
2. **Framework Independence** – The domain layer knows nothing about Spring, JPA, REST, or any infrastructure concern
3. **Testability** – Each layer can be tested independently
4. **Use-Case Driven** – Application behavior is modeled as explicit use cases

---

## Layered Architecture

```
┌──────────────────────────────────────────────────┐
│         Infrastructure Layer                     │
│  (Spring Boot, REST, JPA, Adapters)              │
└──────────────────────────────────────────────────┘
                      ↓ depends on
┌──────────────────────────────────────────────────┐
│         Application Layer                        │
│  (Use Cases, Orchestration)                      │
└──────────────────────────────────────────────────┘
                      ↓ depends on
┌──────────────────────────────────────────────────┐
│         Domain Layer                             │
│  (Business Logic, Pure Java)                     │
└──────────────────────────────────────────────────┘
```

### Layer Responsibilities

| Layer | Responsibility | May Depend On | Must Not Depend On |
|:------|:--------------|:--------------|:-------------------|
| **Domain** | Business rules, invariants, domain model | Nothing (pure Java) | Spring, JPA, HTTP, JSON |
| **Application** | Use case orchestration, workflow logic | Domain | Infrastructure, Spring |
| **Infrastructure** | Technical implementation, I/O, frameworks | Application, Domain | Nothing (outermost layer) |

---

## Module Boundaries

PetWise is structured as a **multi-module Gradle project**:

```
petwise/
├── domain/              (Java library)
├── application/         (Java library)
├── infrastructure/      (Spring Boot application)
└── build-logic/         (Gradle convention plugins)
```

{: .note }
> Gradle enforces module boundaries at compile time, preventing circular dependencies.

---

## Domain-Driven Design (DDD)

PetWise uses **tactical DDD patterns** to express business logic clearly.

### DDD Building Blocks

#### Aggregate Root

An **Aggregate Root** is the entry point to a cluster of related objects.

**Example: Tutor**
- Owns a collection of Pet entities
- Enforces invariants
- Has unique identity (TutorID)

#### Entity

An **Entity** has identity and lifecycle but lives inside an aggregate.

**Example: Pet**
- Part of the Tutor aggregate
- Has its own ID
- Lifecycle managed by aggregate root

#### Value Object

A **Value Object** is immutable and defined by its values.

**Examples:**
- `ServiceType` enum (CRECHE, HOTEL)
- `AppointmentStatus` enum (PENDING, ACTIVE, COMPLETED, CANCELED)

---

## Use Case Pattern

Instead of generic "service" classes, PetWise models application behavior as **explicit use cases**.

### Why Use Cases?

1. **Traceability** – 1:1 mapping between documented use cases (UC-01, UC-02...) and code
2. **Single Responsibility** – Each use case does one thing
3. **Testability** – Easy to test in isolation
4. **Documentation Alignment** – Code structure mirrors requirements

### Example

```java
public class CreateTutorUseCase extends UseCase<CreateTutorInput, CreateTutorOutput> {
    private final TutorGateway tutorGateway;

    @Override
    public CreateTutorOutput execute(CreateTutorInput input) {
        // 1. Validate input
        // 2. Create domain aggregate
        // 3. Save via gateway
        // 4. Return output
    }
}
```

{: .highlight }
See [ADR-0005: UseCase Pattern](architecture/decisions/adr-0005) for full rationale.

---

## Dependency Flow

### The Dependency Rule

{: .important }
**Rule:** Dependencies point inward. Inner layers know nothing about outer layers.

```
Infrastructure (Spring, JPA, REST)
        ↓ depends on
   Application (Use Cases)
        ↓ depends on
     Domain (Business Logic)
        ↓ depends on
      (nothing – pure Java)
```

### Ports and Adapters

**Domain defines interfaces (ports):**

```java
public interface TutorGateway {
    Tutor save(Tutor tutor);
    Optional<Tutor> findById(TutorID id);
}
```

**Infrastructure implements adapters:**

```java
public class TutorJpaGateway implements TutorGateway {
    private final TutorJpaRepository jpaRepository;

    @Override
    public Tutor save(Tutor tutor) {
        // Map domain → JPA → domain
    }
}
```

---

## Build System Architecture

PetWise uses **Gradle convention plugins** to centralize build logic.

### Convention Plugins

| Plugin | Purpose |
|:-------|:--------|
| `petwise.java-library-conventions` | Base Java library (Java 21, JUnit, Spotless, JaCoCo) |
| `petwise.spring-boot-app-conventions` | Spring Boot application setup |
| `petwise.lint-conventions` | Code formatting (Spotless + Google Java Format) |
| `petwise.jacoco-conventions` | Code coverage reporting |

{: .highlight }
For detailed build system documentation, see [Build System Guide](build-system).

---

## Testing Strategy

PetWise follows a **layered testing approach**:

### Test Pyramid

```
        ╱╲
       ╱  ╲      E2E / Integration Tests
      ╱────╲     (Few, slow, infrastructure-heavy)
     ╱      ╲
    ╱────────╲   Application Layer Tests
   ╱          ╲  (Use cases with mocked gateways)
  ╱────────────╲
 ╱              ╲ Domain Layer Tests
╱────────────────╲ (Many, fast, pure unit tests)
```

### Domain Layer Tests

- Pure unit tests, no mocks needed
- Test business rules in isolation
- Fast and independent

### Application Layer Tests

- Test use case orchestration
- Mock gateways
- Focus on workflow logic

### Infrastructure Layer Tests

- Integration tests with `@SpringBootTest`
- Test REST endpoints and JPA repositories
- May use test containers

---

## Trade-offs and Decisions

### Why SQLite for MVP?

**Decision:** Use SQLite instead of PostgreSQL for local development

{: .note }
> - Zero setup friction
> - Easy to run on developer machines
> - Good enough for small datasets
> - PostgreSQL available via Docker for production-like testing

See [ADR-0001: Database SQLite First](architecture/decisions/adr-0001)

### Why Forward-Only Appointment Status?

**Decision:** Appointment status can only move forward (PENDING → ACTIVE → COMPLETED)

{: .note }
> - Simpler state machine
> - Prevents inconsistencies
> - Matches real-world workflow

See [ADR-0002: Appointment Status Model](architecture/decisions/adr-0002)

---

## Summary

PetWise demonstrates how to build a **clean, testable, and well-documented** Spring Boot application using:

- ✅ **Clean Architecture** – Dependency inversion, framework independence
- ✅ **Domain-Driven Design** – Aggregates, entities, value objects, ubiquitous language
- ✅ **Use Case Pattern** – Explicit modeling of application workflows
- ✅ **Gradle Convention Plugins** – DRY, consistent build configuration
- ✅ **Layered Testing** – Unit tests for domain, integration tests for infrastructure

---

## Further Reading

- [Architecture Overview](architecture/)
- [Domain Model](architecture/domain/)
- [ADRs](architecture/decisions/)
- [C4 Diagrams](architecture/c4/)
- [Use Cases](use-cases/)

