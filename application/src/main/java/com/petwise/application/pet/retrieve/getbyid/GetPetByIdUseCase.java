package com.petwise.application.pet.retrieve.getbyid;

import com.petwise.application.UseCase;

/** Abstract use case for retrieving a pet by ID. */
@SuppressWarnings("PMD.AbstractClassWithoutAnyMethod")
public abstract class GetPetByIdUseCase extends UseCase<String, PetOutput> {

    /** Protected constructor for subclasses. */
    protected GetPetByIdUseCase() {}
}
