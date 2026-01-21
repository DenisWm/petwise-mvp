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

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Goal

Allow an attendant to register a new tutor so that pets can be associated with this person.

---

## Primary Actor

- Attendant

---

## Preconditions

- The user is authenticated (not strictly enforced in the MVP, but assumed logically)
- There is network connectivity to the backend

---

## Main Flow (Happy Path)

1. The attendant opens the "New Tutor" form
2. The system asks for: name, email (optional), phone (optional)
3. The attendant fills in the data and submits the form
4. The system validates the input
5. The system creates a new tutor record in the database
6. The system returns the created tutor with its ID
7. The UI displays a success message and the tutor details

---

## Alternative Flows

### A1 – Validation Error

1. At step 4, the system detects invalid data (for example empty name or invalid email)
2. The system returns 400 Bad Request with validation details
3. The UI shows the errors so the user can correct them

---

## Postconditions

- A new tutor is stored in the database
- The tutor has a generated ID
- The tutor can now be selected when creating pets

---

## Application Layer Mapping

**Use case:** `CreateTutorUseCase` (application module)  
**Ports used:** `TutorGateway` (for persistence)  
**Infrastructure entrypoint:** `TutorController` (REST)

---

## Related Artifacts

- **API Endpoint:** `POST /api/v1/tutors`
- **API Reference:** [Create Tutor](../api-reference#create-tutor)
- **Sequence Diagram:** See repository `/docs/architecture/sequences/uc01-create-tutor.puml`

<div style="text-align: center;">
  <img src="{{ site.baseurl }}/architecture/sequences/uc01-create-tutor.png" alt="UC-01 Sequence Diagram" />
</div>

## Business Rules

- **BR-T01:** A tutor must have at least a name
- **BR-T02:** Email and phone are optional but must be valid if provided

{: .note }
> See [Business Rules](../architecture/domain/business-rules) for complete domain constraints.
