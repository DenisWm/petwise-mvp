package com.petwise.application.tutor.update;

import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.exceptions.NotificationException;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorGateway;
import com.petwise.domain.tutor.TutorID;
import com.petwise.domain.validation.handler.Notification;
import java.util.Objects;

/**
 * Default implementation of UpdateTutorUseCase. Implements UC-06: Edit/Delete Records (Update
 * Tutor).
 */
public class DefaultUpdateTutorUseCase extends UpdateTutorUseCase {

    private final TutorGateway tutorGateway;

    public DefaultUpdateTutorUseCase(final TutorGateway tutorGateway) {
        this.tutorGateway = Objects.requireNonNull(tutorGateway, "TutorGateway cannot be null");
    }

    @Override
    public UpdateTutorOutput execute(final UpdateTutorCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        // 1. Retrieve the existing tutor
        final var tutorId = TutorID.from(command.id());
        final var tutor =
                this.tutorGateway
                        .findById(tutorId)
                        .orElseThrow(() -> NotFoundException.with(Tutor.class, tutorId));

        // 2. Update the tutor with new values
        tutor.update(command.name(), command.email(), command.phone());

        // 3. Validate aggregate-level business rules
        final var notification = Notification.create();
        tutor.validate(notification);

        // 4. Check for validation errors
        if (notification.hasErrors()) {
            throw new NotificationException(
                    "Could not update Aggregate Tutor %s".formatted(command.id()), notification);
        }

        // 5. Persist via gateway
        final var updatedTutor = this.tutorGateway.save(tutor);

        // 6. Return output DTO
        return UpdateTutorOutput.from(updatedTutor);
    }
}
