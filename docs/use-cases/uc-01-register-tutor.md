---
layout: default
title: UC-01 Register Tutor
parent: Use Cases
nav_order: 1
---

# UC-01 – Register Tutor
{: .no_toc }

Allow an attendant to register a new tutor so that pets can be associated with this person.
{: .fs-6 .fw-300 }

---

## Summary

| | |
|:--|:--|
| **Actor** | Attendant |
| **Precondition** | User has access to the tutor creation endpoint |
| **Endpoint** | `POST /tutors` → `201 Created` |
| **Use case class** | `DefaultCreateTutorUseCase` |
| **Command / Output** | `CreateTutorCommand` / `CreateTutorOutput` |
| **Gateways** | `TutorGateway` |

## Flow

1. Attendant submits tutor data: **name** (required), **email** (optional), **phone** (optional)
2. System validates input
3. System creates a new tutor record and returns it with a generated UUID

### Errors

| Condition | Response |
|:----------|:---------|
| Empty name, invalid email/phone format | `400 Bad Request` |

## Business Rules

| Rule | Description |
|:-----|:------------|
| BR-T01 | A tutor must have at least a name |
| BR-T02 | Email and phone are optional but must be valid if provided |

---

## Sequence Diagram

<div style="text-align: center;">
  <img src="{{ site.baseurl }}/assets/diagrams/architecture/sequences/uc01-create-tutor.png" alt="UC-01 Sequence Diagram" />
</div>

{: .note }
> If the diagram is not visible, run `make diagrams publish` from the project root.

📄 [View PlantUML Source](https://github.com/deniswm/petwise-mvp/blob/master/docs/architecture/sequences/uc01-create-tutor.puml)
