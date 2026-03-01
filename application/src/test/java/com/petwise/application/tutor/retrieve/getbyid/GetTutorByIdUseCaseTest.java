package com.petwise.application.tutor.retrieve.getbyid;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

/** Unit tests for {@link DefaultGetTutorByIdUseCase}. */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetTutorByIdUseCase Tests")
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.LongVariable"
})
class GetTutorByIdUseCaseTest {

    /** Default constructor. */
    GetTutorByIdUseCaseTest() {}

    /** The mocked tutor gateway. */
    @Mock private TutorGateway tutorGateway;

    /** The use case under test. */
    @InjectMocks private DefaultGetTutorByIdUseCase useCase;

    /** The tutor ID used across tests. */
    private TutorID tutorId;

    /** The tutor used across tests. */
    private Tutor tutor;

    @BeforeEach
    void setUp() {
        tutorId = TutorID.unique();
        tutor =
                Tutor.with(
                        tutorId,
                        "John Doe",
                        Email.from("john@example.com"),
                        Phone.from("+5511987654321"),
                        Instant.now().minusSeconds(3600),
                        Instant.now().minusSeconds(1800));
    }

    @Test
    @DisplayName("Should return tutor output when tutor exists")
    void givenExistingTutor_whenExecute_thenShouldReturnTutorOutput() {
        // Given
        when(tutorGateway.findById(tutorId)).thenReturn(Optional.of(tutor));

        // When
        final var output = useCase.execute(tutorId.getValue());

        // Then
        assertNotNull(output);
        assertEquals(tutorId.getValue(), output.id());
        assertEquals("John Doe", output.name());
        assertEquals("john@example.com", output.email());
        assertEquals("+5511987654321", output.phone());
        assertNotNull(output.createdAt());
        assertNotNull(output.updatedAt());

        verify(tutorGateway).findById(tutorId);
    }

    @Test
    @DisplayName("Should return tutor output with null optional fields")
    void givenTutorWithNullOptionalFields_whenExecute_thenShouldReturnOutputWithNulls() {
        // Given
        final var minimalTutor =
                Tutor.with(
                        tutorId,
                        "Minimal Tutor",
                        null,
                        null,
                        Instant.now().minusSeconds(3600),
                        Instant.now().minusSeconds(1800));

        when(tutorGateway.findById(tutorId)).thenReturn(Optional.of(minimalTutor));

        // When
        final var output = useCase.execute(tutorId.getValue());

        // Then
        assertNotNull(output);
        assertEquals("Minimal Tutor", output.name());
        assertNull(output.email());
        assertNull(output.phone());

        verify(tutorGateway).findById(tutorId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when tutor does not exist")
    void givenNonExistingTutor_whenExecute_thenShouldThrowNotFoundException() {
        // Given
        when(tutorGateway.findById(tutorId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> useCase.execute(tutorId.getValue()));
        verify(tutorGateway).findById(tutorId);
    }

    @Test
    @DisplayName("Should throw NullPointerException when ID is null")
    void givenNullId_whenExecute_thenShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verifyNoInteractions(tutorGateway);
    }

    @Test
    @DisplayName("Should throw NullPointerException when gateway is null")
    void givenNullGateway_whenConstruct_thenShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DefaultGetTutorByIdUseCase(null));
    }
}
