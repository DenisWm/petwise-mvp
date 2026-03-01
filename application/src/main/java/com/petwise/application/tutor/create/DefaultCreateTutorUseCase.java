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

    /** SLF4J logger for this class. */
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCreateTutorUseCase.class);

    /** The gateway used to persist the tutor. */
    private final TutorGateway tutorGateway;

    /**
     * Constructs the use case with the required gateway.
     *
     * @param aTutorGateway the tutor persistence gateway; must not be {@code null}
     */
    public DefaultCreateTutorUseCase(final TutorGateway aTutorGateway) {
        super();
        this.tutorGateway = Objects.requireNonNull(aTutorGateway, "TutorGateway cannot be null");
    }

    /**
     * {@inheritDoc}
     *
     * <p>Validates invariants and persists the new tutor. Throws {@link NotificationException} if
     * validation fails.
     */
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
