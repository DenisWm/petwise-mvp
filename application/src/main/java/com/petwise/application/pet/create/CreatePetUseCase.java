package com.petwise.application.pet.create;

import com.petwise.application.UseCase;

/** Abstract use case for creating a new pet. Implements UC-02: Register Pet. */
@SuppressWarnings("PMD.AbstractClassWithoutAnyMethod")
public abstract class CreatePetUseCase extends UseCase<CreatePetCommand, CreatePetOutput> {
    protected CreatePetUseCase() {}
}
