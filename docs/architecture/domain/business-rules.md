---
layout: default
title: Business Rules & Glossary
parent: Domain Model
grand_parent: Architecture
nav_order: 1
---

# Business Rules & Glossary
{: .no_toc }

Domain terms, invariants, and constraints for the PetWise MVP.
{: .fs-6 .fw-300 }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Glossary

| Term | DDD Role | Identity | Description |
|:-----|:---------|:---------|:------------|
| **Tutor** | Aggregate Root | `TutorID` (UUID) | Pet owner or guardian |
| **Pet** | Entity (inside Tutor) | `PetID` (UUID) | Animal linked to a single tutor |
| **Appointment** | Aggregate Root | `AppointmentID` (UUID) | Scheduled daycare or hotel service for a pet |
| **ServiceType** | Value Object | — | `DAYCARE` or `HOTEL` |
| **AppointmentStatus** | Value Object | — | `PENDING` → `ACTIVE` → `COMPLETED`, or `PENDING` → `CANCELED` |
| **Email**, **Phone** | Value Object | — | Validated, immutable contact info |

### DDD Building Blocks

| Building Block | Base Class | Purpose |
|:---------------|:-----------|:--------|
| Aggregate Root | `AggregateRoot<ID>` | Consistency boundary, enforces invariants |
| Entity | `Entity<ID>` | Mutable object with identity, lives inside an aggregate |
| Value Object | `ValueObject` | Immutable, identity-free (Email, Phone) |
| Identifier | `Identifier` | Strong-typed ID wrapper |
| Domain Exception | `DomainException` | Signals domain rule violations |

### Persistence Mapping

| Domain Concept | Infrastructure Adapter |
|:---------------|:-----------------------|
| `TutorGateway` (port) | `TutorPostgresGateway` → `TutorRepository` (JPA) |
| `PetGateway` (port) | `PetPostgresGateway` → `PetRepository` (JPA) |
| `AppointmentGateway` (port) | `AppointmentPostgresGateway` → `AppointmentRepository` (JPA) |

---

## Tutor Rules

| Rule | Description |
|:-----|:------------|
| **BR-T01** | A tutor must have at least a name |
| **BR-T02** | A tutor may have zero or more pets |
| **BR-T03** | A pet must belong to exactly one tutor |
| **BR-T04** | A tutor cannot be deleted if they still have pets |

---

## Pet Rules

| Rule | Description |
|:-----|:------------|
| **BR-P01** | A pet must have a non-empty name |
| **BR-P02** | Species, breed, birth date, and notes are optional |
| **BR-P03** | A pet cannot be deleted if it has ACTIVE appointments |
| **BR-P04** | A pet may be deleted if it only has COMPLETED or CANCELED appointments |
| **BR-P05** | Birth date cannot be in the future |

---

## Appointment Rules

| Rule | Description |
|:-----|:------------|
| **BR-A01** | An appointment must belong to exactly one pet |
| **BR-A02** | Appointments require service type (DAYCARE/HOTEL), start time, and end time |
| **BR-A03** | `startAt` must be strictly earlier than `endAt` |
| **BR-A04** | No overlapping PENDING/ACTIVE appointments for the same pet |
| **BR-A05** | Status lifecycle is forward-only: PENDING→ACTIVE→COMPLETED, PENDING→CANCELED |
| **BR-A06** | Completed appointments cannot be modified |
| **BR-A07** | Canceled appointments cannot transition to another status |
| **BR-A08** | Appointments appear in the daily agenda if `startAt` falls on the selected day (UTC) |

---

## Validation & Integrity

| Rule | Description |
|:-----|:------------|
| **BR-V01** | Errors use RFC 7807 Problem Details |
| **BR-V02** | Referential integrity enforced: Pet→Tutor, Appointment→Pet |
| **BR-V03** | Domain invariants enforced inside aggregates, not in services |
| **BR-DI01** | Identifiers are UUIDs, unique per entity type |
| **BR-DI02** | Timestamps follow ISO-8601 |

---

## MVP Scope Boundaries

| Rule | Description |
|:-----|:------------|
| **BR-MVP01** | No capacity constraints (daycare/hotel space limits out of scope) |
| **BR-MVP02** | No multi-pet appointments |
| **BR-MVP03** | No recurring appointments |
| **BR-MVP04** | No audit history for status changes |
