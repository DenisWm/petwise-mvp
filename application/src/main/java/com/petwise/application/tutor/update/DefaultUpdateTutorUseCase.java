package com.petwise.application.tutor.update;

import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.exceptions.NotificationException;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorGateway;
import com.petwise.domain.tutor.TutorID;
import com.petwise.domain.validation.handler.Notification;
import java.util.Objects;

/**
 * Default implementation of {@link UpdateTutorUseCase}.
 *
 * <p>Fetches the existing {@link Tutor} by ID (throwing {@link NotFoundException} if absent),
 * applies the new values from the {@link UpdateTutorCommand}, validates domain invariants, and
 * persists the result via the {@link TutorGateway}.
 */
public class DefaultUpdateTutorUseCase extends UpdateTutorUseCase {

    private final TutorGateway tutorGateway;

    public DefaultUpdateTutorUseCase(final TutorGateway tutorGateway) {
        this.tutorGateway = Objects.requireNonNull(tutorGateway, "TutorGateway cannot be null");
    }

    @Override
    public UpdateTutorOutput execute(final UpdateTutorCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        final var tutorId = TutorID.from(command.id());
        final var tutor =
                this.tutorGateway
                        .findById(tutorId)
                        .orElseThrow(() -> NotFoundException.with(Tutor.class, tutorId));

        tutor.update(command.name(), command.email(), command.phone());

        final var notification = Notification.create();
        tutor.validate(notification);

        if (notification.hasErrors()) {
            throw new NotificationException(
                    "Could not update Aggregate Tutor %s".formatted(command.id()), notification);
        }

        return UpdateTutorOutput.from(this.tutorGateway.save(tutor));
    }
}
