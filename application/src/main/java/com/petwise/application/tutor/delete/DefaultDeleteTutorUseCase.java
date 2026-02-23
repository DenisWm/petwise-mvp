package com.petwise.application.tutor.delete;

import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorGateway;
import com.petwise.domain.tutor.TutorID;
import java.util.Objects;

/**
 * Default implementation of {@link DeleteTutorUseCase}.
 *
 * <p>Verifies the {@link Tutor} exists before deletion, throwing {@link NotFoundException} if it
 * cannot be found. This guarantees the caller receives a meaningful error rather than a silent
 * no-op.
 */
public final class DefaultDeleteTutorUseCase extends DeleteTutorUseCase {

    /** The gateway used to find and delete tutors. */
    private final TutorGateway tutorGateway;

    /**
     * Constructs the use case with the required gateway.
     *
     * @param aTutorGateway the tutor persistence gateway; must not be {@code null}
     */
    public DefaultDeleteTutorUseCase(final TutorGateway aTutorGateway) {
        super();
        this.tutorGateway = Objects.requireNonNull(aTutorGateway, "TutorGateway cannot be null");
    }

    /**
     * {@inheritDoc}
     *
     * <p>Finds the tutor by ID, throws {@link NotFoundException} if absent, then deletes it.
     */
    @Override
    public void execute(final String anId) {
        Objects.requireNonNull(anId, "Tutor ID cannot be null");

        final var tutorId = TutorID.from(anId);

        this.tutorGateway
                .findById(tutorId)
                .orElseThrow(() -> NotFoundException.with(Tutor.class, tutorId));

        this.tutorGateway.deleteById(tutorId);
    }
}
