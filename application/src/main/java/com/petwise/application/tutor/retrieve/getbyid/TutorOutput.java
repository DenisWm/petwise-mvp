package com.petwise.application.tutor.retrieve.getbyid;

import com.petwise.domain.tutor.Tutor;
import java.time.Instant;

/** Output DTO for GetTutorByIdUseCase. */
public record TutorOutput(
        String id, String name, String email, String phone, Instant createdAt, Instant updatedAt) {

    public static TutorOutput from(final Tutor tutor) {
        return new TutorOutput(
                tutor.getId().getValue(),
                tutor.getName(),
                tutor.getEmail() != null ? tutor.getEmail().getValue() : null,
                tutor.getPhone() != null ? tutor.getPhone().getValue() : null,
                tutor.getCreatedAt(),
                tutor.getUpdatedAt());
    }
}
