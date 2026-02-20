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
public class DefaultDeleteTutorUseCase extends DeleteTutorUseCase {

    private final TutorGateway tutorGateway;

    public DefaultDeleteTutorUseCase(final TutorGateway tutorGateway) {
        this.tutorGateway = Objects.requireNonNull(tutorGateway, "TutorGateway cannot be null");
    }

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
