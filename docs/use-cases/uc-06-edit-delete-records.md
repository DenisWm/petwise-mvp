---
layout: default
title: UC-06 Edit/Delete Records
parent: Use Cases
nav_order: 6
---

# UC-06 – Edit/Delete Records
{: .no_toc }

Allow an attendant to update or remove tutor and pet records, while respecting domain constraints.
{: .fs-6 .fw-300 }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Goal

Allow an attendant to update or remove tutor and pet records, while respecting domain constraints.

---

## Primary Actor

- Attendant

---

## Preconditions

- The tutor or pet record already exists
- The user can open the details or list screen for the given record

---

## Main Flow – Edit Tutor

1. The attendant opens the tutor details screen
2. The system displays the current tutor data
3. The attendant modifies fields such as name, email, or phone
4. The attendant submits the changes
5. The backend validates the new data
6. The backend updates the tutor record in the database
7. The system returns the updated tutor
8. The UI shows a confirmation message

---

## Main Flow – Edit Pet

1. The attendant opens the pet details screen
2. The system shows the current pet data
3. The attendant modifies fields such as name, species, breed, or birth date
4. The attendant submits the changes
5. The backend validates the new data
6. The backend updates the pet record in the database
7. The system returns the updated pet
8. The UI shows a confirmation message

---

## Main Flow – Delete Pet

1. The attendant opens the pet details or list screen
2. The attendant requests to delete a pet
3. The backend checks if the pet has ACTIVE appointments
4. If there are no active appointments, the backend deletes the pet
5. The backend returns 204 No Content
6. The UI removes the pet from the list and confirms deletion

---

## Main Flow – Delete Tutor

1. The attendant requests to delete a tutor
2. The backend checks if there are any pets linked to that tutor
3. If no pets exist, the backend deletes the tutor
4. The backend returns 204 No Content
5. The UI confirms deletion

---

## Alternative Flows

### A1 – Validation Error on Update

1. At step 5 (edit flows), validation fails (e.g., name empty, invalid email, or birth date in the future)
2. The backend returns 400 Bad Request (ProblemDetails)
3. The UI shows validation errors and keeps the data visible

### A2 – Tutor Not Found

1. At step 1 (edit or delete), the tutor ID does not exist
2. The backend returns 404 Not Found
3. The UI shows an error message

### A3 – Pet Not Found

1. Similar to A2, but for pets
2. The backend returns 404 Not Found
3. The UI shows an error

### A4 – Delete Tutor with Existing Pets

1. The attendant requests to delete a tutor
2. The backend checks if there are any pets linked to that tutor
3. One or more pets exist
4. The backend returns 409 Conflict with code `TUTOR_HAS_PETS`
5. The UI displays an error explaining pets must be deleted first

### A5 – Delete Pet with Active Appointments

1. The attendant requests to delete a pet
2. The backend checks for ACTIVE appointments
3. Active appointments exist
4. The backend returns 409 Conflict with code `PET_HAS_ACTIVE_APPOINTMENTS`
5. The UI displays an error explaining appointments must be completed or canceled first

---

## Postconditions

**On successful edit:**
- Record is updated in the database
- Updated data is visible in all views

**On successful delete:**
- Record is removed from the database
- Record no longer appears in lists or searches

---

## Application Layer Mapping

**Use cases:**
- `UpdateTutorUseCase`
- `DeleteTutorUseCase`
- `UpdatePetUseCase`
- `DeletePetUseCase`

**Ports used:** `TutorGateway`, `PetGateway`, `AppointmentGateway`

**Infrastructure entrypoints:** `TutorController`, `PetController`

---

## Related Artifacts

- **API Endpoints:**
  - `PUT /api/v1/tutors/{id}` - Update tutor
  - `DELETE /api/v1/tutors/{id}` - Delete tutor
  - `PUT /api/v1/pets/{id}` - Update pet
  - `DELETE /api/v1/pets/{id}` - Delete pet
- **API Reference:** [Update/Delete endpoints](../api-reference)

<div style="text-align: center;">
  <img src="{{ site.baseurl }}/architecture/sequences/uc06-edit-delete-records.png" alt="UC-06 Sequence Diagram" />
</div>

## Business Rules

- **BR-T04:** Tutor cannot be deleted if they still have pets
- **BR-P03:** Pet cannot be deleted if it has ACTIVE appointments
- **BR-P04:** Pet can be deleted if it only has COMPLETED or CANCELED appointments

{: .warning }
> **Important:** Deletion is restricted to maintain referential integrity and prevent orphaned records.
