package com.petwise.infrastructure.tutor.presenters;

import com.petwise.application.tutor.retrieve.getbyid.TutorOutput;
import com.petwise.application.tutor.retrieve.list.ListTutorsOutput;
import com.petwise.infrastructure.tutor.models.TutorResponse;

/** Presenter for converting use case outputs to API responses. */
public final class TutorApiPresenter {

    private TutorApiPresenter() {
        // Utility class
    }

    /**
     * Converts a {@link TutorOutput} to a {@link TutorResponse}.
     *
     * @param output the use case output
     * @return the API response
     */
    public static TutorResponse present(final TutorOutput output) {
        return new TutorResponse(
                output.id(),
                output.name(),
                output.email(),
                output.phone(),
                output.createdAt(),
                output.updatedAt());
    }

    /**
     * Converts a {@link ListTutorsOutput} to a {@link TutorResponse}.
     *
     * @param output the use case output
     * @return the API response
     */
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
