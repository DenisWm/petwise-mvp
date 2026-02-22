package com.petwise.application.tutor.update;

import com.petwise.domain.tutor.Tutor;

/**
 * Output DTO for UpdateTutorUseCase.
 *
 * @param id the ID of the updated tutor
 */
@SuppressWarnings("PMD.ShortVariable")
public record UpdateTutorOutput(String id) {

    /**
     * Creates an output from a persisted {@link Tutor}.
     *
     * @param tutor the updated tutor
     * @return a new {@code UpdateTutorOutput}
     */
    public static UpdateTutorOutput from(final Tutor tutor) {
        return new UpdateTutorOutput(tutor.getId().getValue());
    }
}
