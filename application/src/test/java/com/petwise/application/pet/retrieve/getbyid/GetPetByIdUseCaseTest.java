package com.petwise.application.pet.retrieve.getbyid;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.pet.Pet;
import com.petwise.domain.pet.PetGateway;
import com.petwise.domain.pet.PetID;
import com.petwise.domain.tutor.TutorID;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Unit tests for {@link DefaultGetPetByIdUseCase}. */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetPetByIdUseCase Tests")
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate"
})
class GetPetByIdUseCaseTest {

    /** Default constructor. */
    GetPetByIdUseCaseTest() {}

    /** The mocked pet gateway. */
    @Mock private PetGateway petGateway;

    /** The use case under test. */
    @InjectMocks private DefaultGetPetByIdUseCase useCase;

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
                        "Persian",
                        LocalDate.of(2020, 3, 15),
                        "Some notes",
                        Instant.now().minusSeconds(3600),
                        Instant.now().minusSeconds(1800));
    }

    @Test
    @DisplayName("Should return pet output when pet exists")
    void givenExistingPet_whenExecute_thenShouldReturnPetOutput() {
        // Given
        when(petGateway.findById(petId)).thenReturn(Optional.of(pet));

        // When
        final var output = useCase.execute(petId.getValue());

        // Then
        assertNotNull(output);
        assertEquals(petId.getValue(), output.id());
        assertEquals("Fluffy", output.name());
        assertEquals("Cat", output.species());
        assertEquals("Persian", output.breed());

        verify(petGateway).findById(petId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when pet does not exist")
    void givenNonExistingPet_whenExecute_thenShouldThrowNotFoundException() {
        // Given
        when(petGateway.findById(petId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> useCase.execute(petId.getValue()));

        verify(petGateway).findById(petId);
    }

    @Test
    @DisplayName("Should throw NullPointerException when ID is null")
    void givenNullId_whenExecute_thenShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verify(petGateway, never()).findById(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when gateway is null")
    void givenNullGateway_whenConstruct_thenShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DefaultGetPetByIdUseCase(null));
    }
}
