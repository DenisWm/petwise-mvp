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

- **Robert C. Martin's Clean Architecture** - Dependency inversion, testability, framework independence
- **Alistair Cockburn's Hexagonal Architecture** - Ports and adapters, isolation of business logic
- **Domain-Driven Design (DDD)** - Ubiquitous language, aggregates, bounded contexts

---

## Layered Architecture

```
┌──────────────────────────────────────────────────┐
│         Infrastructure Layer                      │
│  (Spring Boot, REST, JPA, Adapters)              │
│                                                   │
│  - Controllers (REST endpoints)                   │
│  - JPA Repositories (persistence adapters)        │
│  - Gateway Implementations (ports → adapters)     │
│  - Configuration (Spring beans, profiles)         │
└──────────────────────────────────────────────────┘
                      ↓ depends on
┌──────────────────────────────────────────────────┐
│         Application Layer                         │
│  (Use Cases, Orchestration)                      │
│                                                   │
│  - Use Cases (CreateTutorUseCase, etc.)          │
│  - Gateway Interfaces (ports)                     │
│  - Input/Output DTOs (use case boundaries)        │
└──────────────────────────────────────────────────┘
                      ↓ depends on
┌──────────────────────────────────────────────────┐
│         Domain Layer                              │
│  (Business Logic, Pure Java)                     │
│                                                   │
│  - Aggregates (Tutor, Appointment)               │
│  - Entities (Pet)                                 │
│  - Value Objects                                  │
│  - Domain Events                                  │
│  - Validation Framework                           │
└──────────────────────────────────────────────────┘
```

---

## Core Principles

### 1. Dependency Rule

{: .important }
Dependencies point **inward**. Inner layers know nothing about outer layers.

```
Infrastructure → Application → Domain → (nothing)
```

### 2. Framework Independence

The **domain layer** has zero framework dependencies:
- No Spring annotations
- No JPA annotations
- No HTTP/JSON concerns
- Pure business logic

### 3. Testability

Each layer can be tested independently:
- **Domain**: Pure unit tests, no mocks needed
- **Application**: Use case tests with mocked gateways
- **Infrastructure**: Integration tests with Spring Boot

### 4. Use-Case Driven

Application behavior is modeled as **explicit use cases**:
- One use case = one class
- Clear input/output boundaries
- Single responsibility

---

## Module Structure

PetWise is organized as a **multi-module Gradle project**:

| Module | Type | Purpose | Dependencies |
|--------|------|---------|--------------|
| `domain` | Java Library | Business logic, aggregates, rules | None |
| `application` | Java Library | Use cases, orchestration | domain |
| `infrastructure` | Spring Boot App | REST API, JPA, config | application, domain |
| `build-logic` | Gradle Plugins | Convention plugins | N/A |

### Benefits

- ✅ Enforced boundaries (Gradle prevents circular dependencies)
- ✅ Clear separation of concerns
- ✅ Reusable domain and application layers
- ✅ Independent testing

---

## Domain-Driven Design

PetWise uses **tactical DDD patterns**:

### Aggregates

**Aggregate Roots** are consistency boundaries:

- **Tutor** - Owns pets, manages the collection
- **Appointment** - Owns lifecycle, status transitions

### Entities

**Entities** have identity but live inside aggregates:

- **Pet** - Part of Tutor aggregate

### Value Objects

Immutable objects defined by their values:
- `ServiceType` enum (CRECHE, HOTEL)
- `AppointmentStatus` enum

{: .note }
For detailed domain model documentation, see [Domain Model](domain/).

---

## Vertical Slices

Each feature is implemented as a **vertical slice** through all layers:

```
HTTP Request
    ↓
Controller (Infrastructure)
    ↓
Use Case (Application)
    ↓
Domain Aggregate
    ↓
Gateway Interface (Port)
    ↓
JPA Repository (Adapter)
    ↓
Database
```

**Benefits:**
- Complete, working features
- End-to-end testable
- Independent development
- Early integration

---

## Key Design Decisions

All architectural decisions are documented as **ADRs** (Architectural Decision Records):

- [ADR-0001: Database SQLite First](decisions/adr-0001)
- [ADR-0002: Appointment Status Model](decisions/adr-0002)
- [ADR-0003: Domain Modeling DDD Basics](decisions/adr-0003)
- [ADR-0004: Repository and Gateway Strategy](decisions/adr-0004)
- [ADR-0005: UseCase Pattern](decisions/adr-0005)

{: .highlight }
See [All ADRs](decisions/) for complete list.

---

## Next Steps

<div class="code-example" markdown="1">

### 📖 Detailed Architecture Docs

- [**Architecture Deep Dive**](../architecture-deep-dive) - In-depth architectural patterns
- [**Domain Model**](domain) - Entities, aggregates, value objects
- [**Architecture Decisions (ADRs)**](decisions) - Key design decisions explained
- [**Diagrams**](diagrams) - C4 model and sequence diagrams

### 🏗️ Implementation Guides

- [**Build System**](../build-system) - Gradle multi-module setup
- [**Use Cases**](../use-cases) - Functional requirements
- [**API Reference**](../api-reference) - REST endpoints

</div>


