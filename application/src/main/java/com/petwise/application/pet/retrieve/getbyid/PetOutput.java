package com.petwise.application.pet.retrieve.getbyid;

import com.petwise.domain.pet.Pet;
import java.time.Instant;
import java.time.LocalDate;

/** Output DTO for GetPetByIdUseCase. */
@SuppressWarnings("PMD.ShortVariable")
public record PetOutput(
        String id,
        String tutorId,
        String name,
        String species,
        String breed,
        LocalDate birthDate,
        String notes,
        Instant createdAt,
        Instant updatedAt) {

    public static PetOutput from(final Pet pet) {
        return new PetOutput(
                pet.getId().getValue(),
                pet.getTutorId().getValue(),
                pet.getName(),
                pet.getSpecies(),
                pet.getBreed(),
                pet.getBirthDate(),
                pet.getNotes(),
                pet.getCreatedAt(),
                pet.getUpdatedAt());
    }
}
