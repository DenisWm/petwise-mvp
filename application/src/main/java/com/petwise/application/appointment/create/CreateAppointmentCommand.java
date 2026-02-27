package com.petwise.application.appointment.create;

import com.petwise.domain.appointment.ServiceType;
import java.time.Instant;

/**
 * Command for CreateAppointmentUseCase.
 *
 * @param petId the pet ID (required)
 * @param serviceType the service type (required)
 * @param startAt the start timestamp (required)
 * @param endAt the end timestamp (required)
 * @param notes optional notes
 */
@SuppressWarnings("PMD.ShortVariable")
public record CreateAppointmentCommand(
        String petId, ServiceType serviceType, Instant startAt, Instant endAt, String notes) {

    /**
     * Factory method for creating a command.
     *
     * @param petId the pet ID
     * @param serviceType the service type
     * @param startAt the start timestamp
     * @param endAt the end timestamp
     * @param notes optional notes
     * @return a new {@code CreateAppointmentCommand}
     */
    public static CreateAppointmentCommand with(
            final String petId,
            final ServiceType serviceType,
            final Instant startAt,
            final Instant endAt,
            final String notes) {
        return new CreateAppointmentCommand(petId, serviceType, startAt, endAt, notes);
    }
}
