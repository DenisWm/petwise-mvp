# UC-01 – Register Tutor

> Allow an attendant to register a new tutor so that pets can be associated with this person.

## Goal

Allow an attendant to register a new tutor so that pets can be associated with this person.

## Primary Actor

- Attendant

## Preconditions

- The user has access to the tutor creation endpoint or form.

## Main Flow (Happy Path)

1. The attendant submits tutor data: name (required), email (optional), phone (optional)
2. The system validates the input
3. The system creates a new tutor record
4. The system returns the created tutor with its generated ID

## Alternative Flows

### A1 – Validation Error

1. At step 2, the system detects invalid data (empty name, invalid email format, invalid phone format)
2. The system returns 400 Bad Request with validation details

## Postconditions

- A new tutor is stored in the database with a generated UUID
- The tutor can be selected when creating pets

## Application Layer Mapping

- **Use case class:** `DefaultCreateTutorUseCase`
- **Command:** `CreateTutorCommand`
- **Output:** `CreateTutorOutput`
- **Ports used:** `TutorGateway`
- **Controller:** `TutorController`

## API Endpoint

- **Method & Path:** `POST /tutors`
- **Success:** `201 Created` with tutor details
- **Error:** `400 Bad Request` on validation failure

## Business Rules

- **BR-T01:** A tutor must have at least a name
- **BR-T02:** Email and phone are optional but must be valid if provided


