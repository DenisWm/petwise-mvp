package com.petwise.application.pet.update;

import java.time.LocalDate;

/**
 * Command for UpdatePetUseCase.
 *
 * @param id the pet ID (required)
 * @param name the pet name (required)
 * @param species the pet species (optional)
 * @param breed the pet breed (optional)
 * @param birthDate the pet birth date (optional)
 * @param notes additional notes (optional)
 */
@SuppressWarnings("PMD.ShortVariable")
public record UpdatePetCommand(
        String id, String name, String species, String breed, LocalDate birthDate, String notes) {

    /**
     * Factory method for creating an update command.
     *
     * @param id the pet ID
     * @param name the pet name
     * @param species the pet species
     * @param breed the pet breed
     * @param birthDate the pet birth date
     * @param notes additional notes
     * @return a new {@code UpdatePetCommand}
     */
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
