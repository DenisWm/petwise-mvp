---
layout: default
title: Glossary
parent: Domain Model
grand_parent: Architecture
nav_order: 2
---

# Domain Glossary
{: .no_toc }

Core domain terms and their representation in the codebase.
{: .fs-6 .fw-300 }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Core Business Concepts

### Tutor
Person responsible for one or more pets (the legal owner or guardian).
- **DDD Role:** Aggregate Root
- **Identity:** `TutorID` (UUID)

### Pet
Animal registered in the system and linked to a single tutor.
- **DDD Role:** Entity (inside Tutor aggregate)
- **Identity:** `PetID` (UUID)
- Cannot be deleted when ACTIVE appointments exist.

### Appointment
A scheduled period during which a pet receives daycare (creche) or hotel service.
- **DDD Role:** Aggregate Root
- **Identity:** `AppointmentID` (UUID)
- Owns its lifecycle and status transitions.

### Service Type
Type of service: `CRECHE` (daycare) or `HOTEL` (boarding).

### Appointment Status
Lifecycle state: `PENDING` → `ACTIVE` → `COMPLETED`, or `PENDING` → `CANCELED`.

---

## DDD Building Blocks

| Building Block | Base Class | Description |
|:---------------|:-----------|:------------|
| **Aggregate Root** | `AggregateRoot<ID>` | Consistency boundary, enforces invariants |
| **Entity** | `Entity<ID>` | Mutable domain object with identity, lives inside an aggregate |
| **Value Object** | `ValueObject` | Immutable, defined by values (Email, Phone) |
| **Identifier** | `Identifier` | Strong-typed ID for entities and aggregates |
| **Domain Event** | `DomainEvent` | Something meaningful that happened (future use) |
| **Domain Exception** | `DomainException` | Signals domain rule violations |

---

## Persistence Concepts

| Concept | Description |
|:--------|:------------|
| **Gateway (Port)** | Domain-level interface (e.g., `TutorGateway`) — defines persistence contract |
| **Repository (Adapter)** | Infrastructure implementation (e.g., `TutorPostgresGateway`) — uses JPA |
| **JPA Entity** | Infrastructure mapping class (e.g., `TutorJpaEntity`) — not a domain concept |
