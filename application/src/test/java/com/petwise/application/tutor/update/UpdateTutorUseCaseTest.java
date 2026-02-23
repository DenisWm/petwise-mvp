package com.petwise.application.tutor.update;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import com.petwise.domain.exceptions.DomainException;
import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.tutor.Email;
import com.petwise.domain.tutor.Phone;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorGateway;
import com.petwise.domain.tutor.TutorID;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Unit tests for {@link DefaultUpdateTutorUseCase}. */
@ExtendWith(MockitoExtension.class)
@DisplayName("UpdateTutorUseCase Tests")
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals",
    "PMD.LiteralsFirstInComparisons"
})
class UpdateTutorUseCaseTest {

    /** Default constructor. */
    UpdateTutorUseCaseTest() {}

    /** The mocked tutor gateway. */
    @Mock private TutorGateway tutorGateway;

    /** The use case under test. */
    @InjectMocks private DefaultUpdateTutorUseCase useCase;

    /** The tutor ID used across tests. */
    private TutorID tutorId;

    /** The existing tutor used across tests. */
    private Tutor existingTutor;

    @BeforeEach
    void setUp() {
        tutorId = TutorID.unique();
        existingTutor =
                Tutor.with(
                        tutorId,
                        "John Doe",
                        Email.from("john@example.com"),
                        Phone.from("+5511987654321"),
                        Instant.now().minusSeconds(3600),
                        Instant.now().minusSeconds(3600));
    }

    @Test
    @DisplayName("Should update tutor successfully with all fields")
    void shouldUpdateTutorSuccessfully() {
        // Given
        final var command =
                UpdateTutorCommand.with(
                        tutorId.getValue(), "Jane Doe", "jane@example.com", "+5511912345678");

        when(tutorGateway.findById(tutorId)).thenReturn(Optional.of(existingTutor));
        when(tutorGateway.save(any(Tutor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        final var output = useCase.execute(command);

        // Then
        assertNotNull(output);
        assertEquals(tutorId.getValue(), output.id());

        verify(tutorGateway).findById(tutorId);
        verify(tutorGateway)
                .save(
                        argThat(
                                tutor ->
                                        tutor.getName().equals("Jane Doe")
                                                && tutor.getEmail()
                                                        .getValue()
                                                        .equals("jane@example.com")
                                                && tutor.getPhone()
                                                        .getValue()
                                                        .equals("+5511912345678")));
    }

    @Test
    @DisplayName("Should update tutor with optional fields as null")
    void shouldUpdateTutorWithNullOptionalFields() {
        // Given
        final var command = UpdateTutorCommand.with(tutorId.getValue(), "Jane Doe", null, null);

        when(tutorGateway.findById(tutorId)).thenReturn(Optional.of(existingTutor));
        when(tutorGateway.save(any(Tutor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        final var output = useCase.execute(command);

        // Then
        assertNotNull(output);
        assertEquals(tutorId.getValue(), output.id());

        verify(tutorGateway).findById(tutorId);
        verify(tutorGateway).save(any(Tutor.class));
    }

    @Test
    @DisplayName("Should throw NotFoundException when tutor does not exist")
    void shouldThrowNotFoundExceptionWhenTutorDoesNotExist() {
        // Given
        final var command =
                UpdateTutorCommand.with(
                        tutorId.getValue(), "Jane Doe", "jane@example.com", "+5511912345678");

        when(tutorGateway.findById(tutorId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> useCase.execute(command));

        verify(tutorGateway).findById(tutorId);
        verify(tutorGateway, never()).save(any(Tutor.class));
    }

    @Test
    @DisplayName("Should throw DomainException when validation fails")
    void shouldThrowDomainExceptionWhenValidationFails() {
        // Given
        final var command =
                UpdateTutorCommand.with(
                        tutorId.getValue(),
                        "", // Empty name should fail validation
                        "jane@example.com",
                        "+5511912345678");

        when(tutorGateway.findById(tutorId)).thenReturn(Optional.of(existingTutor));

        // When & Then
        assertThrows(DomainException.class, () -> useCase.execute(command));

        verify(tutorGateway).findById(tutorId);
        verify(tutorGateway, never()).save(any(Tutor.class));
    }

    @Test
    @DisplayName("Should throw NullPointerException when command is null")
    void shouldThrowNullPointerExceptionWhenCommandIsNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> useCase.execute(null));

        verify(tutorGateway, never()).findById(any());
        verify(tutorGateway, never()).save(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when gateway is null")
    void shouldThrowNullPointerExceptionWhenGatewayIsNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> new DefaultUpdateTutorUseCase(null));
    }
}
