package com.petwise.application.appointment.changestatus;

import com.petwise.domain.appointment.AppointmentStatus;

/** Command for ChangeAppointmentStatusUseCase. */
@SuppressWarnings("PMD.ShortVariable")
public record ChangeAppointmentStatusCommand(String id, AppointmentStatus status) {
    public static ChangeAppointmentStatusCommand with(
            final String id, final AppointmentStatus status) {
        return new ChangeAppointmentStatusCommand(id, status);
    }
}
