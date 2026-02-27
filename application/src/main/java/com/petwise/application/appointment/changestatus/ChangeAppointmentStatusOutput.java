package com.petwise.application.appointment.changestatus;

import com.petwise.domain.appointment.Appointment;

/**
 * Output DTO for ChangeAppointmentStatusUseCase.
 *
 * @param id the appointment ID
 * @param status the updated status as a string
 */
@SuppressWarnings("PMD.ShortVariable")
public record ChangeAppointmentStatusOutput(String id, String status) {

    /**
     * Creates an output from a persisted {@link Appointment}.
     *
     * @param appointment the updated appointment
     * @return a new {@code ChangeAppointmentStatusOutput}
     */
    public static ChangeAppointmentStatusOutput from(final Appointment appointment) {
        return new ChangeAppointmentStatusOutput(
                appointment.getId().getValue(), appointment.getStatus().name());
    }
}
