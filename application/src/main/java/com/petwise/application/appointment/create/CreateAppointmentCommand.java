package com.petwise.application.appointment.create;

import com.petwise.domain.appointment.ServiceType;
import java.time.Instant;

/** Command for CreateAppointmentUseCase. */
@SuppressWarnings("PMD.ShortVariable")
public record CreateAppointmentCommand(
        String petId, ServiceType serviceType, Instant startAt, Instant endAt, String notes) {
    public static CreateAppointmentCommand with(
            final String petId,
            final ServiceType serviceType,
            final Instant startAt,
            final Instant endAt,
            final String notes) {
        return new CreateAppointmentCommand(petId, serviceType, startAt, endAt, notes);
    }
}
