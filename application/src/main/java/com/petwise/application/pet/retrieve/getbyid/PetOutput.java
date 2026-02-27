package com.petwise.application.pet.retrieve.getbyid;

import com.petwise.domain.pet.Pet;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Output DTO for GetPetByIdUseCase.
 *
 * @param id the pet ID
 * @param tutorId the owning tutor ID
 * @param name the pet name
 * @param species the pet species
 * @param breed the pet breed
 * @param birthDate the pet birth date
 * @param notes additional notes
 * @param createdAt the creation timestamp
 * @param updatedAt the last update timestamp
 */
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

    /**
     * Creates a {@code PetOutput} from a domain {@link Pet}.
     *
     * @param pet the pet to map
     * @return a new {@code PetOutput}
     */
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
