package com.petwise.application.pet.update;

import com.petwise.domain.pet.Pet;

/**
 * Output DTO for UpdatePetUseCase.
 *
 * @param id the ID of the updated pet
 */
@SuppressWarnings("PMD.ShortVariable")
public record UpdatePetOutput(String id) {

    /**
     * Creates an output from a persisted {@link Pet}.
     *
     * @param pet the updated pet
     * @return a new {@code UpdatePetOutput}
     */
    public static UpdatePetOutput from(final Pet pet) {
        return new UpdatePetOutput(pet.getId().getValue());
    }
}
