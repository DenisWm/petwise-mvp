package com.petwise.application.pet.delete;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.pet.Pet;
import com.petwise.domain.pet.PetGateway;
import com.petwise.domain.pet.PetID;
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

/** Unit tests for {@link DefaultDeletePetUseCase}. */
@ExtendWith(MockitoExtension.class)
@DisplayName("DeletePetUseCase Tests")
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate"
})
class DeletePetUseCaseTest {

    /** Default constructor. */
    DeletePetUseCaseTest() {}

    /** The mocked pet gateway. */
    @Mock private PetGateway petGateway;

    /** The use case under test. */
    @InjectMocks private DefaultDeletePetUseCase useCase;

    /** The pet ID used across tests. */
    private PetID petId;

    /** The pet used across tests. */
    private Pet pet;

    @BeforeEach
    void setUp() {
        petId = PetID.unique();
        pet =
                Pet.with(
                        petId,
                        TutorID.unique(),
                        "Fluffy",
                        "Cat",
                        null,
                        null,
                        null,
                        Instant.now().minusSeconds(3600),
                        Instant.now().minusSeconds(1800));
    }

    @Test
    @DisplayName("Should delete pet successfully")
    void shouldDeletePetSuccessfully() {
        // Given
        final var petIdValue = petId.getValue();
        when(petGateway.findById(petId)).thenReturn(Optional.of(pet));
        doNothing().when(petGateway).deleteById(petId);

        // When
        assertDoesNotThrow(() -> useCase.execute(petIdValue));

        // Then
        verify(petGateway).findById(petId);
        verify(petGateway).deleteById(petId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when pet does not exist")
    void shouldThrowNotFoundExceptionWhenPetDoesNotExist() {
        // Given
        final var petIdValue = petId.getValue();
        when(petGateway.findById(petId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> useCase.execute(petIdValue));

        verify(petGateway).findById(petId);
        verify(petGateway, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when ID is null")
    void shouldThrowNullPointerExceptionWhenIdIsNull() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));

        verify(petGateway, never()).findById(any());
        verify(petGateway, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when gateway is null")
    void shouldThrowNullPointerExceptionWhenGatewayIsNull() {
        assertThrows(NullPointerException.class, () -> new DefaultDeletePetUseCase(null));
    }
}
