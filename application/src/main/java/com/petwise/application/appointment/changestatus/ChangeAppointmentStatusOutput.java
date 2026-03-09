package com.petwise.application.appointment.changestatus;

import com.petwise.domain.appointment.Appointment;

/** Output DTO for ChangeAppointmentStatusUseCase. */
@SuppressWarnings("PMD.ShortVariable")
public record ChangeAppointmentStatusOutput(String id, String status) {

    public static ChangeAppointmentStatusOutput from(final Appointment appointment) {
        return new ChangeAppointmentStatusOutput(
                appointment.getId().getValue(), appointment.getStatus().name());
    }
}
