package com.petwise.application.pet.update;

import java.time.LocalDate;

/** Command for UpdatePetUseCase. */
@SuppressWarnings("PMD.ShortVariable")
public record UpdatePetCommand(
        String id, String name, String species, String breed, LocalDate birthDate, String notes) {
    public static UpdatePetCommand with(
            final String id,
            final String name,
            final String species,
            final String breed,
            final LocalDate birthDate,
            final String notes) {
        return new UpdatePetCommand(id, name, species, breed, birthDate, notes);
    }
}
