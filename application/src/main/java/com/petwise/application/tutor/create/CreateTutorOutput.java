package com.petwise.application.tutor.create;

import com.petwise.domain.tutor.Tutor;

/**
 * Output DTO for CreateTutorUseCase.
 *
 * @param id the ID of the newly created tutor
 */
@SuppressWarnings("PMD.ShortVariable")
public record CreateTutorOutput(String id) {

    /**
     * Creates an output from a persisted {@link Tutor}.
     *
     * @param tutor the saved tutor
     * @return a new {@code CreateTutorOutput}
     */
    public static CreateTutorOutput from(final Tutor tutor) {
        return new CreateTutorOutput(tutor.getId().getValue());
    }
}
