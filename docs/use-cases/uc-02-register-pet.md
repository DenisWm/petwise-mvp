---
layout: default
title: UC-02 Register Pet
parent: Use Cases
nav_order: 2
---

# UC-02 – Register Pet
{: .no_toc }

Allow an attendant to register a new pet and associate it with an existing tutor.
{: .fs-6 .fw-300 }

---

## Summary

| | |
|:--|:--|
| **Actor** | Attendant |
| **Precondition** | Tutor exists in the system |
| **Endpoint** | `POST /pets` → `201 Created` |
| **Use case class** | `DefaultCreatePetUseCase` |
| **Command / Output** | `CreatePetCommand` / `CreatePetOutput` |
| **Gateways** | `PetGateway`, `TutorGateway` |

## Flow

1. Attendant submits: **tutorId** (required), **name** (required), species, breed, birthDate, notes (all optional)
2. System validates: non-empty name, tutor exists, birth date not in the future
3. System creates the pet linked to the tutor and returns it with a generated UUID

### Errors

| Condition | Response |
|:----------|:---------|
| Tutor does not exist | `404 Not Found` |
| Empty name, invalid birth date | `400 Bad Request` |

## Business Rules

| Rule | Description |
|:-----|:------------|
| BR-P01 | Pet must have a non-empty name |
| BR-P03 | Pet must belong to exactly one tutor |
| BR-P05 | Birth date cannot be in the future |

---

## Sequence Diagram

<div style="text-align: center;">
  <img src="{{ site.baseurl }}/assets/diagrams/architecture/sequences/uc02-create-pet.png" alt="UC-02 Sequence Diagram" />
</div>

{: .note }
> If the diagram is not visible, run `make diagrams publish` from the project root.

📄 [View PlantUML Source](https://github.com/deniswm/petwise-mvp/blob/master/docs/architecture/sequences/uc02-create-pet.puml)
