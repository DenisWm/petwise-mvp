package com.petwise.application.appointment.retrieve.list;

import com.petwise.domain.appointment.Appointment;
import com.petwise.domain.appointment.AppointmentStatus;
import com.petwise.domain.appointment.ServiceType;
import java.time.Instant;

/**
 * Output DTO for a single appointment in list results.
 *
 * @param id the appointment ID
 * @param petId the pet ID
 * @param serviceType the service type
 * @param status the appointment status
 * @param startAt the start timestamp
 * @param endAt the end timestamp
 * @param createdAt the creation timestamp
 * @param updatedAt the last update timestamp
 */
@SuppressWarnings("PMD.ShortVariable")
public record ListAppointmentsOutput(
        String id,
        String petId,
        ServiceType serviceType,
        AppointmentStatus status,
        Instant startAt,
        Instant endAt,
        Instant createdAt,
        Instant updatedAt) {

    /**
     * Creates a {@code ListAppointmentsOutput} from a domain {@link Appointment}.
     *
     * @param appointment the appointment to map
     * @return a new {@code ListAppointmentsOutput}
     */
    public static ListAppointmentsOutput from(final Appointment appointment) {
        return new ListAppointmentsOutput(
                appointment.getId().getValue(),
                appointment.getPetId().getValue(),
                appointment.getServiceType(),
                appointment.getStatus(),
                appointment.getStartAt(),
                appointment.getEndAt(),
                appointment.getCreatedAt(),
                appointment.getUpdatedAt());
    }
}
