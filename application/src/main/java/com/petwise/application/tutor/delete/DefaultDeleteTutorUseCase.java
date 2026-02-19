package com.petwise.application.tutor.delete;

import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorGateway;
import com.petwise.domain.tutor.TutorID;
import java.util.Objects;

/**
 * Default implementation of DeleteTutorUseCase. Implements UC-06: Edit/Delete Records (Delete
 * Tutor).
 */
public class DefaultDeleteTutorUseCase extends DeleteTutorUseCase {

    private final TutorGateway tutorGateway;

    public DefaultDeleteTutorUseCase(final TutorGateway tutorGateway) {
        this.tutorGateway = Objects.requireNonNull(tutorGateway, "TutorGateway cannot be null");
    }

    @Override
    public void execute(final String anId) {
        Objects.requireNonNull(anId, "Tutor ID cannot be null");

        // 1. Parse the tutor ID
        final var tutorId = TutorID.from(anId);

        // 2. Verify the tutor exists before deleting
        this.tutorGateway
                .findById(tutorId)
                .orElseThrow(() -> NotFoundException.with(Tutor.class, tutorId));

        // 3. Delete the tutor
        this.tutorGateway.deleteById(tutorId);
    }
}
