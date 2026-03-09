package com.petwise.application.pet.create;

import java.time.LocalDate;

/** Command for CreatePetUseCase. */
@SuppressWarnings("PMD.ShortVariable")
public record CreatePetCommand(
        String tutorId,
        String name,
        String species,
        String breed,
        LocalDate birthDate,
        String notes) {
    public static CreatePetCommand with(
            final String tutorId,
            final String name,
            final String species,
            final String breed,
            final LocalDate birthDate,
            final String notes) {
        return new CreatePetCommand(tutorId, name, species, breed, birthDate, notes);
    }
}
