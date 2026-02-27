package com.petwise.application.pet.update;

import com.petwise.application.UseCase;

/**
 * Abstract use case for updating an existing pet. Implements UC-06: Edit/Delete Records (Update
 * Pet).
 */
@SuppressWarnings("PMD.AbstractClassWithoutAnyMethod")
public abstract class UpdatePetUseCase extends UseCase<UpdatePetCommand, UpdatePetOutput> {

    /** Protected constructor for subclasses. */
    protected UpdatePetUseCase() {}
}
