package com.petwise.application.pet.update;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.exceptions.NotificationException;
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

/** Unit tests for {@link DefaultUpdatePetUseCase}. */
@ExtendWith(MockitoExtension.class)
@DisplayName("UpdatePetUseCase Tests")
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals"
})
class UpdatePetUseCaseTest {
    UpdatePetUseCaseTest() {}

    @Mock private PetGateway petGateway;
    @InjectMocks private DefaultUpdatePetUseCase useCase;
    private PetID petId;
    private Pet existingPet;

    @BeforeEach
    void setUp() {
        petId = PetID.unique();
        existingPet =
                Pet.with(
                        petId,
                        TutorID.unique(),
                        "Fluffy",
                        "Cat",
                        "Persian",
                        LocalDate.of(2020, 3, 15),
                        "Old notes",
                        Instant.now().minusSeconds(3600),
                        Instant.now().minusSeconds(3600));
    }

    @Test
    @DisplayName("Should update pet successfully with all fields")
    void shouldUpdatePetSuccessfully() {
        final var command =
                UpdatePetCommand.with(
                        petId.getValue(),
                        "Max",
                        "Dog",
                        "Labrador",
                        LocalDate.of(2019, 6, 1),
                        "New notes");

        when(petGateway.findById(petId)).thenReturn(Optional.of(existingPet));
        when(petGateway.save(any(Pet.class))).thenAnswer(inv -> inv.getArgument(0));
        final var output = useCase.execute(command);
        assertNotNull(output);
        assertEquals(petId.getValue(), output.id());

        verify(petGateway).findById(petId);
        verify(petGateway)
                .save(argThat(p -> "Max".equals(p.getName()) && "Dog".equals(p.getSpecies())));
    }

    @Test
    @DisplayName("Should throw NotFoundException when pet does not exist")
    void shouldThrowNotFoundExceptionWhenPetDoesNotExist() {
        final var command = UpdatePetCommand.with(petId.getValue(), "Max", null, null, null, null);
        when(petGateway.findById(petId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> useCase.execute(command));

        verify(petGateway).findById(petId);
        verify(petGateway, never()).save(any());
    }

    @Test
    @DisplayName("Should throw NotificationException when name is blank")
    void shouldThrowNotificationExceptionWhenNameIsBlank() {
        final var command = UpdatePetCommand.with(petId.getValue(), "  ", null, null, null, null);
        when(petGateway.findById(petId)).thenReturn(Optional.of(existingPet));
        assertThrows(NotificationException.class, () -> useCase.execute(command));
        verify(petGateway, never()).save(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when command is null")
    void shouldThrowNullPointerExceptionWhenCommandIsNull() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verify(petGateway, never()).save(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when gateway is null")
    void shouldThrowNullPointerExceptionWhenGatewayIsNull() {
        assertThrows(NullPointerException.class, () -> new DefaultUpdatePetUseCase(null));
    }
}
