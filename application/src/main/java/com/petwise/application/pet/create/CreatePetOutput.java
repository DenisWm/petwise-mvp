package com.petwise.application.pet.create;

import com.petwise.domain.pet.Pet;

/** Output DTO for CreatePetUseCase. */
@SuppressWarnings("PMD.ShortVariable")
public record CreatePetOutput(String id) {

    public static CreatePetOutput from(final Pet pet) {
        return new CreatePetOutput(pet.getId().getValue());
    }
}
