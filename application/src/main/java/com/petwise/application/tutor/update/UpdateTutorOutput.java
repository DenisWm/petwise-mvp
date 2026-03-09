package com.petwise.application.tutor.update;

import com.petwise.domain.tutor.Tutor;

/** Output DTO for UpdateTutorUseCase. */
@SuppressWarnings("PMD.ShortVariable")
public record UpdateTutorOutput(String id) {

    public static UpdateTutorOutput from(final Tutor tutor) {
        return new UpdateTutorOutput(tutor.getId().getValue());
    }
}
