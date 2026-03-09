package com.petwise.application.tutor.update;

import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.exceptions.NotificationException;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorGateway;
import com.petwise.domain.tutor.TutorID;
import com.petwise.domain.validation.handler.Notification;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link UpdateTutorUseCase}.
 *
 * <p>Fetches the existing {@link Tutor} by ID (throwing {@link NotFoundException} if absent),
 * applies the new values from the {@link UpdateTutorCommand}, validates domain invariants, and
 * persists the result via the {@link TutorGateway}.
 */
public final class DefaultUpdateTutorUseCase extends UpdateTutorUseCase {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultUpdateTutorUseCase.class);
    private final TutorGateway tutorGateway;

    public DefaultUpdateTutorUseCase(final TutorGateway aTutorGateway) {
        super();
        this.tutorGateway = Objects.requireNonNull(aTutorGateway, "TutorGateway cannot be null");
    }

    @Override
    public UpdateTutorOutput execute(final UpdateTutorCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating tutor id={}", command.id());
        }

        final var tutorId = TutorID.from(command.id());
        final var tutor =
                this.tutorGateway
                        .findById(tutorId)
                        .orElseThrow(
                                () -> {
                                    if (LOG.isWarnEnabled()) {
                                        LOG.warn(
                                                "Tutor not found for update with id={}",
                                                command.id());
                                    }
                                    return NotFoundException.with(Tutor.class, tutorId);
                                });

        tutor.update(command.name(), command.email(), command.phone());

        final var notification = Notification.create();
        tutor.validate(notification);

        if (notification.hasErrors()) {
            if (LOG.isWarnEnabled()) {
                LOG.warn(
                        "Tutor update validation failed for id={}: {}",
                        command.id(),
                        notification.getErrors());
            }
            throw new NotificationException(
                    "Could not update Aggregate Tutor %s".formatted(command.id()), notification);
        }

        final var output = UpdateTutorOutput.from(this.tutorGateway.save(tutor));
        if (LOG.isInfoEnabled()) {
            LOG.info("Tutor updated successfully with id={}", output.id());
        }
        return output;
    }
}
