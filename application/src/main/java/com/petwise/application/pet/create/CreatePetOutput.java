package com.petwise.application.pet.create;

import com.petwise.domain.pet.Pet;

/**
 * Output DTO for CreatePetUseCase.
 *
 * @param id the ID of the newly created pet
 */
@SuppressWarnings("PMD.ShortVariable")
public record CreatePetOutput(String id) {

    /**
     * Creates an output from a persisted {@link Pet}.
     *
     * @param pet the saved pet
     * @return a new {@code CreatePetOutput}
     */
    public static CreatePetOutput from(final Pet pet) {
        return new CreatePetOutput(pet.getId().getValue());
    }
}
