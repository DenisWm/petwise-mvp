## Goal

Update or remove tutor and pet records, while respecting domain constraints.

## Primary Actor

- Attendant

## Preconditions

- The tutor or pet record exists.

## Main Flow – Edit Tutor

1. The attendant submits updated tutor data (name, email, phone)
2. The system validates the new data
3. The system updates the tutor record
4. The system returns the updated tutor

## Main Flow – Edit Pet

1. The attendant submits updated pet data (name, species, breed, birthDate, notes)
2. The system validates the new data
3. The system updates the pet record
4. The system returns the updated pet

## Main Flow – Delete Pet

1. The attendant requests to delete a pet
2. The system checks for ACTIVE appointments
3. No active appointments — the system deletes the pet
4. The system returns 204 No Content

## Main Flow – Delete Tutor

1. The attendant requests to delete a tutor
2. The system checks for linked pets
3. No pets exist — the system deletes the tutor
4. The system returns 204 No Content

## Alternative Flows

### A1 – Validation Error on Update

1. Validation fails (empty name, invalid email, birth date in the future)
2. The system returns 400 Bad Request

### A2 – Record Not Found

1. The tutor or pet ID does not exist
2. The system returns 404 Not Found

### A3 – Delete Tutor with Existing Pets

1. The tutor has linked pets
2. The system returns 409 Conflict (pets must be removed first)

### A4 – Delete Pet with Active Appointments

1. The pet has ACTIVE appointments
2. The system returns 409 Conflict (appointments must be completed or canceled first)

## Postconditions

**On edit:** Record is updated in the database.
**On delete:** Record is removed from the database.

## Application Layer Mapping

- **Use cases:** `DefaultUpdateTutorUseCase`, `DefaultDeleteTutorUseCase`, `DefaultUpdatePetUseCase`, `DefaultDeletePetUseCase`
- **Ports used:** `TutorGateway`, `PetGateway`, `AppointmentGateway`
- **Controllers:** `TutorController`, `PetController`

## API Endpoints

| Method | Path | Description |
|:-------|:-----|:------------|
| `PUT` | `/tutors/{id}` | Update tutor |
| `DELETE` | `/tutors/{id}` | Delete tutor |
| `PUT` | `/pets/{id}` | Update pet |
| `DELETE` | `/pets/{id}` | Delete pet |

## Business Rules

- **BR-T04:** Tutor cannot be deleted if they still have pets
- **BR-P03:** Pet cannot be deleted if it has ACTIVE appointments
- **BR-P04:** Pet can be deleted if it only has COMPLETED or CANCELED appointments


