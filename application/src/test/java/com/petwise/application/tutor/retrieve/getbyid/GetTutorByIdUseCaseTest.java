package com.petwise.application.tutor.retrieve.getbyid;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.tutor.TutorGateway;
import com.petwise.domain.tutor.TutorID;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Clean, minimal tests for GetTutorById use case. Focuses on constructor null checks, null command
 * and not-found behavior.
 */
@DisplayName("GetTutorByIdUseCase Tests")
@ExtendWith(MockitoExtension.class)
class GetTutorByIdUseCaseTest {

    @Mock private TutorGateway tutorGateway;

    @InjectMocks private DefaultGetTutorByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        // Mockito will initialize mocks and inject into useCase because of @ExtendWith and
        // @InjectMocks.
    }

    @Test
    @DisplayName("Should throw NullPointerException when gateway is null")
    void shouldThrowNullPointerExceptionWhenGatewayIsNull() {
        assertThrows(NullPointerException.class, () -> new DefaultGetTutorByIdUseCase(null));
    }

    @Test
    @DisplayName("Should throw NullPointerException when command is null")
    void shouldThrowNullPointerExceptionWhenCommandIsNull() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verifyNoInteractions(tutorGateway);
    }

    @Test
    @DisplayName("Should throw NotFoundException when tutor does not exist")
    void shouldThrowNotFoundExceptionWhenTutorDoesNotExist() {
        final TutorID tutorId = TutorID.unique();
        when(tutorGateway.findById(any(TutorID.class))).thenReturn(Optional.empty());

        final var command = tutorId.getValue();

        assertThrows(NotFoundException.class, () -> useCase.execute(command));
        verify(tutorGateway).findById(any(TutorID.class));
    }
}
