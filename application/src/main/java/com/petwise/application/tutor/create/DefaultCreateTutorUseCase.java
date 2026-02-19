package com.petwise.application.tutor.create;

import com.petwise.domain.exceptions.NotificationException;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorGateway;
import com.petwise.domain.validation.handler.Notification;
import java.util.Objects;

/** Default implementation of CreateTutorUseCase. Implements UC-01: Register Tutor. */
public class DefaultCreateTutorUseCase extends CreateTutorUseCase {

    private final TutorGateway tutorGateway;

    public DefaultCreateTutorUseCase(final TutorGateway tutorGateway) {
        this.tutorGateway = Objects.requireNonNull(tutorGateway, "TutorGateway cannot be null");
    }

    @Override
    public CreateTutorOutput execute(final CreateTutorCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        // 1. Create domain aggregate (value objects validate themselves)
        final var tutor = Tutor.newTutor(command.name(), command.email(), command.phone());

        // 2. Validate aggregate-level business rules
        final var notification = Notification.create();
        tutor.validate(notification);

        // 3. Check for validation errors
        if (notification.hasErrors()) {
            throw new NotificationException("Could not create Aggregate Tutor", notification);
        }

        // 4. Persist via gateway
        final var createdTutor = this.tutorGateway.save(tutor);

        // 5. Return output DTO
        return CreateTutorOutput.from(createdTutor);
    }
}
