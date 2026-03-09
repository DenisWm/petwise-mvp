package com.petwise.application.pet.create;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import com.petwise.application.UseCaseTest;
import com.petwise.domain.exceptions.NotificationException;
import com.petwise.domain.pet.Pet;
import com.petwise.domain.pet.PetGateway;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Unit tests for {@link DefaultCreatePetUseCase}. */
@ExtendWith(MockitoExtension.class)
@DisplayName("CreatePetUseCase Tests")
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals"
})
class CreatePetUseCaseTest extends UseCaseTest {
    @Mock private PetGateway petGateway;
    @InjectMocks private DefaultCreatePetUseCase useCase;

    CreatePetUseCaseTest() {}

    @Override
    protected List<Object> getMocks() {
        return List.of(petGateway);
    }

    @Test
    @DisplayName("Should create pet successfully with all fields")
    void givenValidCommand_whenExecute_thenCreatePet() {
        final var tutorId = "tutor-id-123";
        final var name = "Fluffy";
        final var species = "Cat";
        final var breed = "Persian";
        final var birthDate = LocalDate.of(2020, 3, 15);
        final var notes = "Allergic to chicken";

        final var command = CreatePetCommand.with(tutorId, name, species, breed, birthDate, notes);

        when(petGateway.save(any(Pet.class))).thenAnswer(inv -> inv.getArgument(0));
        final var output = useCase.execute(command);
        assertNotNull(output);
        assertNotNull(output.id());

        verify(petGateway, times(1))
                .save(
                        argThat(
                                pet ->
                                        name.equals(pet.getName())
                                                && species.equals(pet.getSpecies())
                                                && breed.equals(pet.getBreed())
                                                && birthDate.equals(pet.getBirthDate())
                                                && notes.equals(pet.getNotes())));
    }

    @Test
    @DisplayName("Should create pet successfully without optional fields")
    void givenCommandWithoutOptionals_whenExecute_thenCreatePet() {
        final var command = CreatePetCommand.with("tutor-id-123", "Buddy", null, null, null, null);

        when(petGateway.save(any(Pet.class))).thenAnswer(inv -> inv.getArgument(0));
        final var output = useCase.execute(command);
        assertNotNull(output);
        assertNotNull(output.id());
        verify(petGateway, times(1)).save(any(Pet.class));
    }

    @Test
    @DisplayName("Should throw NotificationException when name is null")
    void givenNullName_whenExecute_thenThrowException() {
        final var command = CreatePetCommand.with("tutor-id-123", null, null, null, null, null);
        assertThrows(NotificationException.class, () -> useCase.execute(command));
        verify(petGateway, never()).save(any());
    }

    @Test
    @DisplayName("Should throw NotificationException when name is blank")
    void givenBlankName_whenExecute_thenThrowException() {
        final var command = CreatePetCommand.with("tutor-id-123", "  ", null, null, null, null);
        assertThrows(NotificationException.class, () -> useCase.execute(command));
        verify(petGateway, never()).save(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when command is null")
    void givenNullCommand_whenExecute_thenThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verify(petGateway, never()).save(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when gateway is null")
    void givenNullGateway_whenConstruct_thenThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DefaultCreatePetUseCase(null));
    }
}
