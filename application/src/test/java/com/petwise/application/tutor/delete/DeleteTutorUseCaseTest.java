package com.petwise.application.tutor.delete;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

/** Unit tests for {@link DefaultDeleteTutorUseCase}. */
@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteTutorUseCase Tests")
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate"
})
class DeleteTutorUseCaseTest {
    DeleteTutorUseCaseTest() {}

    @Mock private TutorGateway tutorGateway;
    @InjectMocks private DefaultDeleteTutorUseCase useCase;
    private TutorID tutorId;
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
    @DisplayName("Should delete tutor successfully")
    void givenExistingTutor_whenExecute_thenShouldDeleteTutor() {
        final var tutorIdValue = tutorId.getValue();

        when(tutorGateway.findById(tutorId)).thenReturn(Optional.of(tutor));
        doNothing().when(tutorGateway).deleteById(tutorId);
        assertDoesNotThrow(() -> useCase.execute(tutorIdValue));
        verify(tutorGateway).findById(tutorId);
        verify(tutorGateway).deleteById(tutorId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when tutor does not exist")
    void givenNonExistingTutor_whenExecute_thenShouldThrowNotFoundException() {
        final var tutorIdValue = tutorId.getValue();

        when(tutorGateway.findById(tutorId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> useCase.execute(tutorIdValue));

        verify(tutorGateway).findById(tutorId);
        verify(tutorGateway, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when ID is null")
    void givenNullId_whenExecute_thenShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));

        verify(tutorGateway, never()).findById(any());
        verify(tutorGateway, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when gateway is null")
    void givenNullGateway_whenConstruct_thenShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DefaultDeleteTutorUseCase(null));
    }

    @Test
    @DisplayName("Should verify tutor exists before deletion")
    void givenExistingTutor_whenExecute_thenShouldCallFindBeforeDelete() {
        final var tutorIdValue = tutorId.getValue();

        when(tutorGateway.findById(tutorId)).thenReturn(Optional.of(tutor));
        doNothing().when(tutorGateway).deleteById(tutorId);
        useCase.execute(tutorIdValue);

        // Then - verify findById was called before deleteById
        final var inOrder = inOrder(tutorGateway);
        inOrder.verify(tutorGateway).findById(tutorId);
        inOrder.verify(tutorGateway).deleteById(tutorId);
    }
}
