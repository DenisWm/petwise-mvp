package com.petwise.application.pet.retrieve.list;

import com.petwise.domain.pet.Pet;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Output DTO for a single pet in list results.
 *
 * @param id the pet ID
 * @param tutorId the owning tutor ID
 * @param name the pet name
 * @param species the pet species
 * @param breed the pet breed
 * @param birthDate the pet birth date
 * @param createdAt the creation timestamp
 * @param updatedAt the last update timestamp
 */
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

    /**
     * Creates a {@code ListPetsOutput} from a domain {@link Pet}.
     *
     * @param pet the pet to map
     * @return a new {@code ListPetsOutput}
     */
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
