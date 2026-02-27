package com.petwise.application.pet.create;

import java.time.LocalDate;

/**
 * Command for CreatePetUseCase.
 *
 * @param tutorId the owning tutor ID (required)
 * @param name the pet name (required)
 * @param species the pet species (optional)
 * @param breed the pet breed (optional)
 * @param birthDate the pet birth date (optional)
 * @param notes additional notes (optional)
 */
@SuppressWarnings("PMD.ShortVariable")
public record CreatePetCommand(
        String tutorId,
        String name,
        String species,
        String breed,
        LocalDate birthDate,
        String notes) {

    /**
     * Factory method for creating a command.
     *
     * @param tutorId the tutor ID
     * @param name the pet name
     * @param species the pet species
     * @param breed the pet breed
     * @param birthDate the pet birth date
     * @param notes additional notes
     * @return a new {@code CreatePetCommand}
     */
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
