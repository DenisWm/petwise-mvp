package com.petwise.application.tutor.retrieve.getbyid;

import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorGateway;
import com.petwise.domain.tutor.TutorID;
import java.util.Objects;

/**
 * Default implementation of {@link GetTutorByIdUseCase}.
 *
 * <p>Looks up the {@link Tutor} by its string ID and maps it to a {@link TutorOutput} DTO.
 * Throws {@link NotFoundException} when no tutor with the given ID exists.
 */
public class DefaultGetTutorByIdUseCase extends GetTutorByIdUseCase {

    private final TutorGateway tutorGateway;

    public DefaultGetTutorByIdUseCase(final TutorGateway tutorGateway) {
        this.tutorGateway = Objects.requireNonNull(tutorGateway, "TutorGateway cannot be null");
    }

    @Override
    public TutorOutput execute(final String anId) {
        Objects.requireNonNull(anId, "Tutor ID cannot be null");

        final var tutorId = TutorID.from(anId);
        final var tutor =
                this.tutorGateway
                        .findById(tutorId)
                        .orElseThrow(() -> NotFoundException.with(Tutor.class, tutorId));

        return TutorOutput.from(tutor);
    }
}
