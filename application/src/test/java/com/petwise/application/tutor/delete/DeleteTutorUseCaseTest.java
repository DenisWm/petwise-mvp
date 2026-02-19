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

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteTutorUseCase Tests")
class DeleteTutorUseCaseTest {

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
    void shouldDeleteTutorSuccessfully() {
        // Given
        final var tutorIdValue = tutorId.getValue();

        when(tutorGateway.findById(tutorId)).thenReturn(Optional.of(tutor));
        doNothing().when(tutorGateway).deleteById(tutorId);

        // When
        assertDoesNotThrow(() -> useCase.execute(tutorIdValue));

        // Then
        verify(tutorGateway).findById(tutorId);
        verify(tutorGateway).deleteById(tutorId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when tutor does not exist")
    void shouldThrowNotFoundExceptionWhenTutorDoesNotExist() {
        // Given
        final var tutorIdValue = tutorId.getValue();

        when(tutorGateway.findById(tutorId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> useCase.execute(tutorIdValue));

        verify(tutorGateway).findById(tutorId);
        verify(tutorGateway, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when ID is null")
    void shouldThrowNullPointerExceptionWhenIdIsNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> useCase.execute(null));

        verify(tutorGateway, never()).findById(any());
        verify(tutorGateway, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when gateway is null")
    void shouldThrowNullPointerExceptionWhenGatewayIsNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> new DefaultDeleteTutorUseCase(null));
    }

    @Test
    @DisplayName("Should verify tutor exists before deletion")
    void shouldVerifyTutorExistsBeforeDeletion() {
        // Given
        final var tutorIdValue = tutorId.getValue();

        when(tutorGateway.findById(tutorId)).thenReturn(Optional.of(tutor));
        doNothing().when(tutorGateway).deleteById(tutorId);

        // When
        useCase.execute(tutorIdValue);

        // Then - verify findById was called before deleteById
        var inOrder = inOrder(tutorGateway);
        inOrder.verify(tutorGateway).findById(tutorId);
        inOrder.verify(tutorGateway).deleteById(tutorId);
    }
}
