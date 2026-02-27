package com.petwise.application.appointment.changestatus;

import com.petwise.domain.appointment.AppointmentStatus;

/**
 * Command for ChangeAppointmentStatusUseCase.
 *
 * @param id the appointment ID (required)
 * @param status the new appointment status (required)
 */
@SuppressWarnings("PMD.ShortVariable")
public record ChangeAppointmentStatusCommand(String id, AppointmentStatus status) {

    /**
     * Factory method for creating a command.
     *
     * @param id the appointment ID
     * @param status the new appointment status
     * @return a new {@code ChangeAppointmentStatusCommand}
     */
    public static ChangeAppointmentStatusCommand with(
            final String id, final AppointmentStatus status) {
        return new ChangeAppointmentStatusCommand(id, status);
    }
}
