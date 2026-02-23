package com.petwise.application.tutor.create;

import com.petwise.application.UseCase;

/** Abstract use case for creating a new tutor. Implements UC-01: Register Tutor. */
@SuppressWarnings("PMD.AbstractClassWithoutAnyMethod")
public abstract class CreateTutorUseCase extends UseCase<CreateTutorCommand, CreateTutorOutput> {

    /** Protected constructor for subclasses. */
    protected CreateTutorUseCase() {}
}
