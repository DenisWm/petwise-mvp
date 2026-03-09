---
layout: default
title: UC-06 Edit/Delete Records
parent: Use Cases
nav_order: 6
---

# UC-06 – Edit/Delete Records
{: .no_toc }

Allow an attendant to update or remove tutor and pet records, respecting domain constraints.
{: .fs-6 .fw-300 }

---

## Summary

| | |
|:--|:--|
| **Actor** | Attendant |
| **Precondition** | Target record exists |
| **Use cases** | `DefaultUpdateTutorUseCase`, `DefaultDeleteTutorUseCase`, `DefaultUpdatePetUseCase`, `DefaultDeletePetUseCase` |
| **Gateways** | `TutorGateway`, `PetGateway`, `AppointmentGateway` |

## Endpoints

| Method | Path | Success |
|:-------|:-----|:--------|
| `PUT` | `/tutors/{id}` | `200 OK` |
| `DELETE` | `/tutors/{id}` | `204 No Content` |
| `PUT` | `/pets/{id}` | `200 OK` |
| `DELETE` | `/pets/{id}` | `204 No Content` |

## Flows

**Edit Tutor/Pet:** Submit updated data → validate → persist → return updated record.

**Delete Pet:** Check for ACTIVE appointments → none found → delete.

**Delete Tutor:** Check for linked pets → none found → delete.

### Errors

| Condition | Response |
|:----------|:---------|
| Record not found | `404 Not Found` |
| Validation failure (empty name, invalid email, future birth date) | `400 Bad Request` |
| Delete tutor with existing pets | `409 Conflict` |
| Delete pet with ACTIVE appointments | `409 Conflict` |

## Business Rules

| Rule | Description |
|:-----|:------------|
| BR-T04 | Tutor cannot be deleted if they still have pets |
| BR-P03 | Pet cannot be deleted if it has ACTIVE appointments |
| BR-P04 | Pet can be deleted if it only has COMPLETED or CANCELED appointments |

---

## Sequence Diagram

<div style="text-align: center;">
  <img src="{{ site.baseurl }}/assets/diagrams/architecture/sequences/uc06-edit-delete-records.png" alt="UC-06 Sequence Diagram" />
</div>

{: .note }
> If the diagram is not visible, run `make diagrams publish` from the project root.

📄 [View PlantUML Source](https://github.com/deniswm/petwise-mvp/blob/master/docs/architecture/sequences/uc06-edit-delete-records.puml)
