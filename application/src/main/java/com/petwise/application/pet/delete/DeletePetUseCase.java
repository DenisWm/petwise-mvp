package com.petwise.application.pet.delete;

import com.petwise.application.UnitUseCase;

/** Abstract use case for deleting a pet. Implements UC-06: Edit/Delete Records (Delete Pet). */
@SuppressWarnings("PMD.AbstractClassWithoutAnyMethod")
public abstract class DeletePetUseCase extends UnitUseCase<String> {

    /** Protected constructor for subclasses. */
    protected DeletePetUseCase() {}
}
