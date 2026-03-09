package com.petwise.infrastructure.tutor.presenters;

import com.petwise.application.tutor.retrieve.getbyid.TutorOutput;
import com.petwise.application.tutor.retrieve.list.ListTutorsOutput;
import com.petwise.infrastructure.tutor.models.TutorResponse;

/** Converts tutor use-case outputs to API responses. */
public final class TutorApiPresenter {

    private TutorApiPresenter() {}

    public static TutorResponse present(final TutorOutput output) {
        return new TutorResponse(
                output.id(),
                output.name(),
                output.email(),
                output.phone(),
                output.createdAt(),
                output.updatedAt());
    }

    public static TutorResponse present(final ListTutorsOutput output) {
        return new TutorResponse(
                output.id(),
                output.name(),
                output.email(),
                output.phone(),
                output.createdAt(),
                output.updatedAt());
    }
}
