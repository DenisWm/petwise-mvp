# Tutor Domain Implementation

## Overview

Complete implementation of the Tutor aggregate root following Domain-Driven Design principles for the PetWise veterinary management system.

## Components Created

### Domain Model (src/main/java/com/petwise/domain/tutor/)

1. **TutorID.java** - Unique identifier for Tutor aggregate
   - Extends `Identifier<String>`
   - Factory methods: `unique()` for new IDs, `from(String)` for reconstruction
   - Uses UUID-based ID generation

2. **Email.java** - Email value object
   - Optional field (returns null if not provided)
   - Validates email format using regex pattern
   - Throws `DomainException` for invalid formats
   - Immutable with proper equals/hashCode

3. **Phone.java** - Phone value object
   - Optional field (returns null if not provided)
   - Validates phone number format (supports international formats)
   - Accepts digits, spaces, parentheses, plus signs, and dashes
   - Length validation: 8-20 characters
   - Immutable with proper equals/hashCode

4. **Tutor.java** - Aggregate root
   - Extends `AggregateRoot<TutorID>`
   - Properties:
     - `name`: String (required, max 255 chars)
     - `email`: Email (optional)
     - `phone`: Phone (optional)
     - `createdAt`: Instant (immutable)
     - `updatedAt`: Instant (updated on changes)
   - Factory methods:
     - `newTutor(name, email, phone)` - Create new with generated ID
     - `with(...)` - Reconstruct from persistence
   - Business method:
     - `update(name, email, phone)` - Update tutor information
   - Validation via `TutorValidator`

5. **TutorValidator.java** - Domain validator
   - Validates name constraints:
     - Not null
     - Not empty/blank
     - Max 255 characters
   - Uses `ValidationHandler` pattern for error collection

6. **TutorGateway.java** - Port interface
   - `save(Tutor)` - Persist tutor
   - `findById(TutorID)` - Retrieve by ID
   - `deleteById(TutorID)` - Delete tutor

### Tests (src/test/java/com/petwise/domain/tutor/)

1. **EmailTest.java** - Email value object tests
   - Valid email creation
   - Null handling (optional)
   - Empty email rejection
   - Invalid format rejection
   - Equality and toString tests

2. **PhoneTest.java** - Phone value object tests
   - Valid phone creation (multiple formats)
   - Null handling (optional)
   - Empty phone rejection
   - Invalid format rejection
   - International phone support
   - Equality and toString tests

3. **TutorTest.java** - Tutor aggregate tests
   - Valid tutor creation with all fields
   - Valid tutor creation without optional fields
   - Name validation (null, empty, too long)
   - Invalid email rejection
   - Invalid phone rejection
   - Update functionality
   - Reconstruction from persistence

## Business Rules

- **BR-T01**: A tutor must have at least a name
- **BR-T02**: Email and phone are optional but must be valid if provided
- **BR-T03**: Name cannot exceed 255 characters
- **BR-T04**: Email must match standard email format
- **BR-T05**: Phone must be 8-20 characters with valid phone characters

## Usage Examples

### Creating a new tutor
```java
Tutor tutor = Tutor.newTutor(
    "John Doe",
    "john.doe@example.com",
    "+1 (555) 123-4567"
);

// Validate
Notification notification = Notification.create();
tutor.validate(notification);
if (notification.hasErrors()) {
    // Handle validation errors
}
```

### Creating a tutor with optional fields
```java
Tutor tutor = Tutor.newTutor("Jane Smith", null, null);
```

### Updating a tutor
```java
tutor.update("John Updated", "newemail@example.com", null);
```

### Reconstructing from persistence
```java
Tutor tutor = Tutor.with(
    "existing-id",
    "John Doe",
    "john@example.com",
    "555-1234",
    createdAt,
    updatedAt
);
```

## Architecture

The implementation follows Clean Architecture and DDD principles:

- **Domain Layer**: Pure business logic, no framework dependencies
- **Value Objects**: Immutable, self-validating (Email, Phone)
- **Aggregate Root**: Consistency boundary (Tutor)
- **Gateway Pattern**: Port for infrastructure (TutorGateway)
- **Validation**: Explicit validation with error collection

## Next Steps

To complete the Tutor feature:

1. **Application Layer**: Create use cases
   - CreateTutorUseCase
   - UpdateTutorUseCase
   - DeleteTutorUseCase
   - FindTutorByIdUseCase

2. **Infrastructure Layer**: Implement gateway
   - TutorJpaEntity
   - TutorJpaRepository
   - TutorJpaGateway (implements TutorGateway)

3. **API Layer**: REST endpoints
   - TutorController
   - DTOs (CreateTutorRequest, TutorResponse, etc.)

## Related Documentation

- [UC-01 Register Tutor](../../../docs/use-cases/uc-01-register-tutor.md)
- [Domain Model](../../../docs/architecture/domain.md)
- [Contributing Guidelines](../../../docs/contributing.md)

