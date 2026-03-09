package com.petwise.infrastructure.pet.presenters;

import com.petwise.application.pet.retrieve.getbyid.PetOutput;
import com.petwise.application.pet.retrieve.list.ListPetsOutput;
import com.petwise.infrastructure.pet.models.PetResponse;

/** Converts pet use-case outputs to API responses. */
public final class PetApiPresenter {

    private PetApiPresenter() {}

    public static PetResponse present(final PetOutput output) {
        return new PetResponse(
                output.id(),
                output.tutorId(),
                output.name(),
                output.species(),
                output.breed(),
                output.birthDate(),
                output.notes(),
                output.createdAt(),
                output.updatedAt());
    }

    public static PetResponse present(final ListPetsOutput output) {
        return new PetResponse(
                output.id(),
                output.tutorId(),
                output.name(),
                output.species(),
                output.breed(),
                output.birthDate(),
                null,
                output.createdAt(),
                output.updatedAt());
    }
}
