---
layout: default
title: Business Rules
parent: Domain Model
grand_parent: Architecture
nav_order: 1
---

# Business Rules – PetWise MVP
{: .no_toc }

Core domain rules, invariants, and constraints.
{: .fs-6 .fw-300 }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

In the PetWise domain model:

- **Tutor** is an **Aggregate Root**
- **Pet** is an **Entity** inside the Tutor aggregate
- **Appointment** is an **Aggregate Root**
- **ServiceType** and **AppointmentStatus** are **Value Objects**
- All invariants are enforced inside the **Domain Layer**

---

## 1. Tutor Rules (Aggregate Root)

| Rule | Description |
|:-----|:------------|
| **BR-T01** | A tutor must have at least a name |
| **BR-T02** | A tutor may have zero or more pets |
| **BR-T03** | A pet must belong to exactly one tutor |
| **BR-T04** | A tutor cannot be deleted if they still have pets |

---

## 2. Pet Rules (Entity)

| Rule | Description |
|:-----|:------------|
| **BR-P01** | A pet must have a non-empty name |
| **BR-P02** | Species, breed, birth date, and notes are optional |
| **BR-P03** | A pet cannot be deleted if it has ACTIVE appointments |
| **BR-P04** | A pet may be deleted if it only has COMPLETED or CANCELED appointments |
| **BR-P05** | Birth date cannot be in the future |

---

## 3. Appointment Rules (Aggregate Root)

| Rule | Description |
|:-----|:------------|
| **BR-A01** | An appointment must belong to exactly one pet |
| **BR-A02** | Appointments require service type (CRECHE/HOTEL), start time, and end time |
| **BR-A03** | `startAt` must be strictly earlier than `endAt` |
| **BR-A04** | No overlapping PENDING/ACTIVE appointments for the same pet |
| **BR-A05** | Status lifecycle is forward-only: PENDING→ACTIVE→COMPLETED, PENDING→CANCELED |
| **BR-A06** | Completed appointments cannot be modified |
| **BR-A07** | Canceled appointments cannot transition to another status |
| **BR-A08** | Appointments are included in the daily agenda if their `startAt` falls on the selected day (UTC) |

---

## 4. Validation & Integrity

| Rule | Description |
|:-----|:------------|
| **BR-V01** | Errors use RFC 7807 Problem Details |
| **BR-V02** | Referential integrity enforced: Pet→Tutor, Appointment→Pet |
| **BR-V03** | Domain invariants enforced inside aggregates, not in services |
| **BR-DI01** | Identifiers are UUIDs, unique per entity type |
| **BR-DI02** | Timestamps follow ISO-8601 |

---

## 5. MVP Scope

| Rule | Description |
|:-----|:------------|
| **BR-MVP01** | No capacity constraints (daycare/hotel space limits out of scope) |
| **BR-MVP02** | No multi-pet appointments |
| **BR-MVP03** | No recurring appointments |
| **BR-MVP04** | No audit history for status changes |
