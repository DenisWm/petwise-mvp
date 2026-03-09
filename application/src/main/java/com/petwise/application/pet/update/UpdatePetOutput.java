package com.petwise.application.pet.update;

import com.petwise.domain.pet.Pet;

/** Output DTO for UpdatePetUseCase. */
@SuppressWarnings("PMD.ShortVariable")
public record UpdatePetOutput(String id) {

    public static UpdatePetOutput from(final Pet pet) {
        return new UpdatePetOutput(pet.getId().getValue());
    }
}
