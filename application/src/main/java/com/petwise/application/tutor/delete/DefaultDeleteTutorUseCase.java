package com.petwise.application.tutor.delete;

import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorGateway;
import com.petwise.domain.tutor.TutorID;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link DeleteTutorUseCase}.
 *
 * <p>Verifies the {@link Tutor} exists before deletion, throwing {@link NotFoundException} if it
 * cannot be found. This guarantees the caller receives a meaningful error rather than a silent
 * no-op.
 */
public final class DefaultDeleteTutorUseCase extends DeleteTutorUseCase {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultDeleteTutorUseCase.class);
    private final TutorGateway tutorGateway;

    public DefaultDeleteTutorUseCase(final TutorGateway aTutorGateway) {
        super();
        this.tutorGateway = Objects.requireNonNull(aTutorGateway, "TutorGateway cannot be null");
    }

    @Override
    public void execute(final String anId) {
        Objects.requireNonNull(anId, "Tutor ID cannot be null");
        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting tutor with id={}", anId);
        }

        final var tutorId = TutorID.from(anId);

        this.tutorGateway
                .findById(tutorId)
                .orElseThrow(
                        () -> {
                            if (LOG.isWarnEnabled()) {
                                LOG.warn("Tutor not found for deletion with id={}", anId);
                            }
                            return NotFoundException.with(Tutor.class, tutorId);
                        });

        this.tutorGateway.deleteById(tutorId);
        if (LOG.isInfoEnabled()) {
            LOG.info("Tutor deleted successfully with id={}", anId);
        }
    }
}
