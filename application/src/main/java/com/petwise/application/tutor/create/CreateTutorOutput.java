package com.petwise.application.tutor.create;

import com.petwise.domain.tutor.Tutor;

/** Output DTO for CreateTutorUseCase. */
public record CreateTutorOutput(String id) {

    public static CreateTutorOutput from(final Tutor tutor) {
        return new CreateTutorOutput(tutor.getId().getValue());
    }
}
