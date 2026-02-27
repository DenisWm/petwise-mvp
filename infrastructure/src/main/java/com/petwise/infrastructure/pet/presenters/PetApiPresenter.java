package com.petwise.infrastructure.pet.presenters;

import com.petwise.application.pet.retrieve.getbyid.PetOutput;
import com.petwise.application.pet.retrieve.list.ListPetsOutput;
import com.petwise.infrastructure.pet.models.PetResponse;

/** Presenter for converting pet use-case outputs to API responses. */
public final class PetApiPresenter {

    private PetApiPresenter() {
        // Utility class
    }

    /**
     * Converts a {@link PetOutput} to a {@link PetResponse}.
     *
     * @param output the use case output
     * @return the API response
     */
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

    /**
     * Converts a {@link ListPetsOutput} to a {@link PetResponse}.
     *
     * @param output the use case output
     * @return the API response
     */
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
