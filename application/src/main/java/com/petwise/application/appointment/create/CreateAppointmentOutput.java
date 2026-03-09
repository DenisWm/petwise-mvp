package com.petwise.application.appointment.create;

import com.petwise.domain.appointment.Appointment;

/** Output DTO for CreateAppointmentUseCase. */
@SuppressWarnings("PMD.ShortVariable")
public record CreateAppointmentOutput(String id) {

    public static CreateAppointmentOutput from(final Appointment appointment) {
        return new CreateAppointmentOutput(appointment.getId().getValue());
    }
}
