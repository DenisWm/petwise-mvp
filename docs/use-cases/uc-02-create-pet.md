# UC-02 – Register Pet

## Goal

Allow an attendant to register a new pet and associate it with an existing tutor.

## Primary Actor

- Attendant

## Preconditions

- The tutor already exists in the system.
- The user can view the list of tutors (implicit in the UI).
- Network connectivity to the backend is available.

## Main Flow (Happy Path)

1. The attendant chooses a tutor from the list.
2. The system displays a “New Pet” form.
3. The attendant enters: name (required), species, breed, birth date (optional).
4. The attendant submits the form.
5. The system validates:
    - non-empty name,
    - tutor exists,
    - birth date is not in the future.
6. The system creates a new pet record linked to the tutor.
7. The system returns the created pet data with its ID.
8. The UI shows a success message and the new pet details.

## Alternative Flows

### A1 – Tutor Not Found

1. At step 5, the system determines the tutor does not exist.
2. It returns 404 Not Found.
3. The UI displays an error message.

### A2 – Validation Error

1. At step 5, a rule is violated (for example name empty or birth date invalid).
2. The system returns 400 Bad Request using ProblemDetails (RFC 7807).
3. The UI highlights the invalid fields.

## Postconditions

- A new pet is stored in the database.
- The pet is linked to exactly one tutor.
- The pet is available for appointments.

## Application Layer Mapping

- Use case: CreatePetUseCase (application module)
- Ports used: PetGateway, TutorGateway
- Infrastructure entrypoint: PetController (REST)

## Related Artifacts

- Sequence diagram: docs/architecture/sequences/uc02-create-pet.puml
- API: POST /api/v1/pets
