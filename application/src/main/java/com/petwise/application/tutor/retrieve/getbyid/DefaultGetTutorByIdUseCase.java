package com.petwise.application.tutor.retrieve.getbyid;

import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorGateway;
import com.petwise.domain.tutor.TutorID;
import java.util.Objects;

/** Default implementation of GetTutorByIdUseCase. Retrieves a tutor by its ID. */
public class DefaultGetTutorByIdUseCase extends GetTutorByIdUseCase {

    private final TutorGateway tutorGateway;

    public DefaultGetTutorByIdUseCase(final TutorGateway tutorGateway) {
        this.tutorGateway = Objects.requireNonNull(tutorGateway, "TutorGateway cannot be null");
    }

    @Override
    public TutorOutput execute(final String anId) {
        Objects.requireNonNull(anId, "Command cannot be null");

        // 1. Retrieve the tutor by ID
        final var tutorId = TutorID.from(anId);
        final var tutor =
                this.tutorGateway
                        .findById(tutorId)
                        .orElseThrow(() -> NotFoundException.with(Tutor.class, tutorId));

        // 2. Return output DTO
        return TutorOutput.from(tutor);
    }
}
