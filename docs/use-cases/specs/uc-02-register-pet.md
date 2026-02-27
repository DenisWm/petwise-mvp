# UC-02 – Register Pet

> Allow an attendant to register a new pet and associate it with an existing tutor.

## Goal

Allow an attendant to register a new pet and associate it with an existing tutor.

## Primary Actor

- Attendant

## Preconditions

- The tutor already exists in the system.

## Main Flow (Happy Path)

1. The attendant submits pet data: tutorId (required), name (required), species (optional), breed (optional), birthDate (optional), notes (optional)
2. The system validates: non-empty name, tutor exists, birth date not in the future
3. The system creates a new pet record linked to the tutor
4. The system returns the created pet with its generated ID

## Alternative Flows

### A1 – Tutor Not Found

1. At step 2, the tutor does not exist
2. The system returns 404 Not Found

### A2 – Validation Error

1. At step 2, a validation rule is violated (e.g., empty name, invalid birth date)
2. The system returns 400 Bad Request with details

## Postconditions

- A new pet is stored in the database linked to the tutor
- The pet is available for appointments

## Application Layer Mapping

- **Use case class:** `DefaultCreatePetUseCase`
- **Command:** `CreatePetCommand`
- **Output:** `CreatePetOutput`
- **Ports used:** `PetGateway`, `TutorGateway`
- **Controller:** `PetController`

## API Endpoint

- **Method & Path:** `POST /pets`
- **Success:** `201 Created` with pet details
- **Errors:** `400 Bad Request`, `404 Not Found` (tutor)

## Business Rules

- **BR-P01:** Pet must have a non-empty name
- **BR-P02:** Species, breed, birth date, notes are optional
- **BR-P03:** Pet must belong to exactly one tutor
- **BR-P05:** Birth date cannot be in the future


