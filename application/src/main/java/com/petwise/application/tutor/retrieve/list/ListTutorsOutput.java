package com.petwise.application.tutor.retrieve.list;

import com.petwise.domain.tutor.Tutor;
import java.time.Instant;

/**
 * Output DTO for a single tutor in list results.
 *
 * @param id the tutor ID
 * @param name the tutor name
 * @param email the tutor email
 * @param phone the tutor phone
 * @param createdAt the creation timestamp
 * @param updatedAt the last update timestamp
 */
@SuppressWarnings("PMD.ShortVariable")
public record ListTutorsOutput(
        String id, String name, String email, String phone, Instant createdAt, Instant updatedAt) {

    /**
     * Creates a {@code ListTutorsOutput} from a domain {@link Tutor}.
     *
     * @param tutor the tutor to map
     * @return a new {@code ListTutorsOutput}
     */
    public static ListTutorsOutput from(final Tutor tutor) {
        return new ListTutorsOutput(
                tutor.getId().getValue(),
                tutor.getName(),
                tutor.getEmail() != null ? tutor.getEmail().getValue() : null,
                tutor.getPhone() != null ? tutor.getPhone().getValue() : null,
                tutor.getCreatedAt(),
                tutor.getUpdatedAt());
    }
}
