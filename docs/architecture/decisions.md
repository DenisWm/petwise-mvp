---
layout: default
title: Architecture Decision Records
parent: Architecture
nav_order: 2
---

# Architecture Decision Records (ADRs)
{: .no_toc }

Key architectural decisions documented for future reference.
{: .fs-6 .fw-300 }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## What are ADRs?

Architecture Decision Records capture important architectural decisions:

- **Context:** What issue are we facing?
- **Decision:** What did we decide?
- **Consequences:** What are the trade-offs?

---

## Decision Records

### ADR-0001: PostgreSQL with H2 for Tests

**Status:** ✅ Accepted

**Context:**  
Need a reliable database for the MVP with easy test setup.

**Decision:**  
Use PostgreSQL as the primary database (via Docker Compose) and H2 as an in-memory database for integration tests. Flyway manages schema migrations.

**Consequences:**
- ✅ Production-grade database from the start
- ✅ H2 provides fast, zero-config test execution
- ✅ Flyway ensures consistent schema across environments
- ⚠️ Requires Docker for local development

---

### ADR-0002: Appointment Status Lifecycle Model

**Status:** ✅ Accepted

**Context:**  
Appointments need a clear state machine with valid transitions.

**Decision:**  
Implement a forward-only status lifecycle with transition validation in the aggregate:

```
PENDING → ACTIVE → COMPLETED
PENDING → CANCELED
```

**Valid transitions:**
- `PENDING` → `ACTIVE`
- `PENDING` → `CANCELED`
- `ACTIVE` → `COMPLETED`

No backward transitions. `COMPLETED` and `CANCELED` are terminal states.

**Consequences:**
- ✅ Business rules enforced at domain level
- ✅ Clear lifecycle prevents invalid states
- ✅ Simple and predictable

---

### ADR-0003: Domain Modeling with DDD Basics

**Status:** ✅ Accepted

**Context:**  
Need clear separation between entities, value objects, and aggregates.

**Decision:**  
Apply DDD tactical patterns:
- **Aggregate Roots:** `Tutor`, `Appointment` — consistency boundaries
- **Entities:** `Pet` — has identity, lives inside Tutor aggregate
- **Value Objects:** `Email`, `Phone`, `ServiceType`, `AppointmentStatus` — immutable

**Consequences:**
- ✅ Clear business logic encapsulation
- ✅ Testable domain model
- ℹ️ Requires discipline to maintain boundaries

---

### ADR-0004: Repository and Gateway Strategy

**Status:** ✅ Accepted

**Context:**  
Need persistence abstraction without coupling to JPA.

**Decision:**  
Use domain-level gateway interfaces as ports:
- Domain defines contracts (e.g., `TutorGateway`, `PetGateway`, `AppointmentGateway`)
- Infrastructure implements adapters (e.g., `TutorPostgresGateway`)

**Consequences:**
- ✅ Domain independent of persistence technology
- ✅ Easy to test with in-memory implementations
- ✅ Can swap JPA for other solutions

---

### ADR-0005: Use Case Pattern

**Status:** ✅ Accepted

**Context:**  
Need consistent structure for application layer operations.

**Decision:**  
Every use case follows single responsibility:
```java
public abstract class UseCase<INPUT, OUTPUT> {
    public abstract OUTPUT execute(INPUT input);
}
```

Variants: `UnitUseCase<INPUT>` (no output), `NullaryUseCase<OUTPUT>` (no input).

**Consequences:**
- ✅ Clear, testable, single-purpose operations
- ✅ 1:1 mapping between documented use cases and code
- ℹ️ One class per use case (increases file count)

---

## ADR Template

Use this structure for new ADRs:

```markdown
### ADR-NNNN: Title

**Status:** Proposed / Accepted / Deprecated / Superseded

**Context:**
What is the issue?

**Decision:**
What did we decide?

**Consequences:**
What are the trade-offs?
```
