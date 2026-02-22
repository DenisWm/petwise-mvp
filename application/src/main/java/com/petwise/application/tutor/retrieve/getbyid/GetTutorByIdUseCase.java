package com.petwise.application.tutor.retrieve.getbyid;

import com.petwise.application.UseCase;

/** Abstract use case for retrieving a tutor by ID. */
@SuppressWarnings("PMD.AbstractClassWithoutAnyMethod")
public abstract class GetTutorByIdUseCase extends UseCase<String, TutorOutput> {

    /** Protected constructor for subclasses. */
    protected GetTutorByIdUseCase() {}
}
