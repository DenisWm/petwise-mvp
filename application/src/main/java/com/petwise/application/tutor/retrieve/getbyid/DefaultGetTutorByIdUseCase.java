package com.petwise.application.tutor.retrieve.getbyid;

import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorGateway;
import com.petwise.domain.tutor.TutorID;
import java.util.Objects;

/**
 * Default implementation of {@link GetTutorByIdUseCase}.
 *
 * <p>Looks up the {@link Tutor} by its string ID and maps it to a {@link TutorOutput} DTO. Throws
 * {@link NotFoundException} when no tutor with the given ID exists.
 */
public final class DefaultGetTutorByIdUseCase extends GetTutorByIdUseCase {

    /** The gateway used to look up tutors. */
    private final TutorGateway tutorGateway;

    /**
     * Constructs the use case with the required gateway.
     *
     * @param aTutorGateway the tutor persistence gateway; must not be {@code null}
     */
    public DefaultGetTutorByIdUseCase(final TutorGateway aTutorGateway) {
        super();
        this.tutorGateway = Objects.requireNonNull(aTutorGateway, "TutorGateway cannot be null");
    }

    /**
     * {@inheritDoc}
     *
     * <p>Throws {@link NotFoundException} if no tutor with the given ID is found.
     */
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
