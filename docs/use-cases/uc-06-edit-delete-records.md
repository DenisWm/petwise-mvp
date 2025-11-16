# UC-06 – Edit/Delete Records (Tutors and Pets)

## Goal

Allow an attendant to update or remove tutor and pet records, while respecting domain constraints.

## Primary Actor

- Attendant

## Preconditions

- The tutor or pet record already exists.
- The user can open the details or list screen for the given record.

## Main Flow (Happy Path) – Edit Tutor

1. The attendant opens the tutor details screen.
2. The system displays the current tutor data.
3. The attendant modifies fields such as name, email, or phone.
4. The attendant submits the changes.
5. The backend validates the new data.
6. The backend updates the tutor record in the database.
7. The system returns the updated tutor.
8. The UI shows a confirmation message.

## Main Flow (Happy Path) – Edit Pet

1. The attendant opens the pet details screen.
2. The system shows the current pet data.
3. The attendant modifies fields such as name, species, breed, or birth date.
4. The attendant submits the changes.
5. The backend validates the new data.
6. The backend updates the pet record in the database.
7. The system returns the updated pet.
8. The UI shows a confirmation message.

## Main Flow (Happy Path) – Delete Pet

1. The attendant opens the pet details or list screen.
2. The attendant requests to delete a pet.
3. The backend checks if the pet has ACTIVE appointments.
4. If there are no active appointments, the backend deletes the pet.
5. The backend returns 204 No Content.
6. The UI removes the pet from the list and confirms deletion.

## Alternative Flows

### A1 – Validation Error on Update

1. At step 5 (edit flows), validation fails (for example name empty, invalid email, or birth date in the future).
2. The backend returns 400 Bad Request (ProblemDetails).
3. The UI shows validation errors and keeps the data visible.

### A2 – Tutor Not Found

1. At step 1 (edit or delete), the tutor ID does not exist.
2. The backend returns 404 Not Found.
3. The UI shows an error message and may navigate back or refresh the list.

### A3 – Pet Not Found

1. Similar to A2, but for pets.
2. The backend returns 404 Not Found.
3. The UI shows an error.

### A4 – Delete Tutor with Existing Pets

1. The attendant requests to delete a tutor.
2. The backend checks if there are any pets linked to that tutor.
3. One or more pets exist.
4. The backend returns 409 Conflict with an error code such as TUTOR_HAS_PETS.
5. The UI displays a message explaining the tutor cannot be removed while pets are still attached.

### A5 – Delete Pet with Active Appointments

1. The attendant requests to delete a pet.
2. The backend checks for ACTIVE appointments associated with the pet.
3. One or more active appointments are found.
4. The backend returns 409 Conflict with an error code such as PET_HAS_ACTIVE_APPOINTMENTS.
5. The UI informs the user that the pet cannot be removed until active appointments are closed or canceled.

## Postconditions

- Records are updated or deleted only when domain rules allow.
- Referential integrity is maintained (no orphan references).
- The user receives clear feedback on success or conflict.

## Application Layer Mapping

- Use cases:
    - EditTutorUseCase, EditPetUseCase (application module)
    - DeleteTutorUseCase, DeletePetUseCase (application module)
- Ports used:
    - TutorGateway, PetGateway, AppointmentGateway
- Infrastructure entrypoints:
    - TutorController, PetController (REST)

## Related Artifacts

- Sequence diagram: docs/architecture/sequences/uc06-edit-delete-records.puml
- APIs:
    - PUT /api/v1/tutors/{id}
    - DELETE /api/v1/tutors/{id}
    - PUT /api/v1/pets/{id}
    - DELETE /api/v1/pets/{id}
