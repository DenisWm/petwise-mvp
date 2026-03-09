package com.petwise.application.pet.retrieve.list;

import com.petwise.domain.pet.Pet;
import java.time.Instant;
import java.time.LocalDate;

/** Output DTO for a single pet in list results. */
@SuppressWarnings("PMD.ShortVariable")
public record ListPetsOutput(
        String id,
        String tutorId,
        String name,
        String species,
        String breed,
        LocalDate birthDate,
        Instant createdAt,
        Instant updatedAt) {

    public static ListPetsOutput from(final Pet pet) {
        return new ListPetsOutput(
                pet.getId().getValue(),
                pet.getTutorId().getValue(),
                pet.getName(),
                pet.getSpecies(),
                pet.getBreed(),
                pet.getBirthDate(),
                pet.getCreatedAt(),
                pet.getUpdatedAt());
    }
}
