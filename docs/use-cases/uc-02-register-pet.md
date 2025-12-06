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

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Goal

Allow an attendant to register a new pet and associate it with an existing tutor.

---

## Primary Actor

- Attendant

---

## Preconditions

- The tutor already exists in the system
- The user can view the list of tutors (implicit in the UI)
- Network connectivity to the backend is available

---

## Main Flow (Happy Path)

1. The attendant chooses a tutor from the list
2. The system displays a "New Pet" form
3. The attendant enters: name (required), species, breed, birth date (optional)
4. The attendant submits the form
5. The system validates:
   - Non-empty name
   - Tutor exists
   - Birth date is not in the future
6. The system creates a new pet record linked to the tutor
7. The system returns the created pet data with its ID
8. The UI shows a success message and the new pet details

---

## Alternative Flows

### A1 – Tutor Not Found

1. At step 5, the system determines the tutor does not exist
2. It returns 404 Not Found
3. The UI displays an error message

### A2 – Validation Error

1. At step 5, a rule is violated (for example name empty or birth date invalid)
2. The system returns 400 Bad Request using ProblemDetails (RFC 7807)
3. The UI highlights the invalid fields

---

## Postconditions

- A new pet is stored in the database
- The pet is linked to exactly one tutor
- The pet is available for appointments

---

## Application Layer Mapping

**Use case:** `CreatePetUseCase` (application module)  
**Ports used:** `PetGateway`, `TutorGateway`  
**Infrastructure entrypoint:** `PetController` (REST)

---

## Related Artifacts

- **API Endpoint:** `POST /api/v1/pets`
- **API Reference:** [Create Pet](../api-reference#create-pet)
- **Sequence Diagram:** See repository `/docs/architecture/sequences/uc02-create-pet.puml`

<div style="text-align: center;">
  <img src="{{ site.baseurl }}/assets/diagrams/architecture/sequences/uc02-create-pet.png" alt="UC-02 Sequence Diagram" />
</div>

## Business Rules

- **BR-P01:** Pet must have a non-empty name
- **BR-P02:** Species, breed, birth date, notes are optional
- **BR-P03:** Pet must belong to exactly one tutor
- **BR-P05:** Birth date cannot be in the future

{: .note }
> See [Business Rules](../architecture/domain/business-rules) for complete domain constraints.
