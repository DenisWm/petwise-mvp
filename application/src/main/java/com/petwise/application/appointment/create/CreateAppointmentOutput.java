package com.petwise.application.appointment.create;

import com.petwise.domain.appointment.Appointment;

/**
 * Output DTO for CreateAppointmentUseCase.
 *
 * @param id the ID of the newly created appointment
 */
@SuppressWarnings("PMD.ShortVariable")
public record CreateAppointmentOutput(String id) {

    /**
     * Creates an output from a persisted {@link Appointment}.
     *
     * @param appointment the saved appointment
     * @return a new {@code CreateAppointmentOutput}
     */
    public static CreateAppointmentOutput from(final Appointment appointment) {
        return new CreateAppointmentOutput(appointment.getId().getValue());
    }
}
