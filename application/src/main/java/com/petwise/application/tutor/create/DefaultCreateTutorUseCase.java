package com.petwise.application.tutor.create;

import com.petwise.domain.exceptions.NotificationException;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorGateway;
import com.petwise.domain.validation.handler.Notification;
import java.util.Objects;

/**
 * Default implementation of {@link CreateTutorUseCase}.
 *
 * <p>Validates the {@link CreateTutorCommand} against domain invariants via a {@link Notification}
 * handler and persists the new {@link Tutor} through the {@link TutorGateway}. Throws {@link
 * NotificationException} if any constraint is violated.
 */
public class DefaultCreateTutorUseCase extends CreateTutorUseCase {

    private final TutorGateway tutorGateway;

    public DefaultCreateTutorUseCase(final TutorGateway tutorGateway) {
        this.tutorGateway = Objects.requireNonNull(tutorGateway, "TutorGateway cannot be null");
    }

    @Override
    public CreateTutorOutput execute(final CreateTutorCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        final var tutor = Tutor.newTutor(command.name(), command.email(), command.phone());

        final var notification = Notification.create();
        tutor.validate(notification);

        if (notification.hasErrors()) {
            throw new NotificationException("Could not create Aggregate Tutor", notification);
        }

        return CreateTutorOutput.from(this.tutorGateway.save(tutor));
    }
}
