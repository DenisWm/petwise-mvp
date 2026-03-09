package com.petwise.application.tutor.create;

import com.petwise.domain.exceptions.NotificationException;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorGateway;
import com.petwise.domain.validation.handler.Notification;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link CreateTutorUseCase}.
 *
 * <p>Validates the {@link CreateTutorCommand} against domain invariants via a {@link Notification}
 * handler and persists the new {@link Tutor} through the {@link TutorGateway}. Throws {@link
 * NotificationException} if any constraint is violated.
 */
public final class DefaultCreateTutorUseCase extends CreateTutorUseCase {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCreateTutorUseCase.class);
    private final TutorGateway tutorGateway;

    public DefaultCreateTutorUseCase(final TutorGateway aTutorGateway) {
        super();
        this.tutorGateway = Objects.requireNonNull(aTutorGateway, "TutorGateway cannot be null");
    }

    @Override
    public CreateTutorOutput execute(final CreateTutorCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating tutor with name={}, email={}", command.name(), command.email());
        }

        final var tutor = Tutor.newTutor(command.name(), command.email(), command.phone());

        final var notification = Notification.create();
        tutor.validate(notification);

        if (notification.hasErrors()) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Tutor creation validation failed: {}", notification.getErrors());
            }
            throw new NotificationException("Could not create Aggregate Tutor", notification);
        }

        final var output = CreateTutorOutput.from(this.tutorGateway.save(tutor));
        if (LOG.isInfoEnabled()) {
            LOG.info("Tutor created successfully with id={}", output.id());
        }
        return output;
    }
}
