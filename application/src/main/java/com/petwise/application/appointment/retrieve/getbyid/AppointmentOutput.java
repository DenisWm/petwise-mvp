package com.petwise.application.appointment.retrieve.getbyid;

import com.petwise.domain.appointment.Appointment;
import com.petwise.domain.appointment.AppointmentStatus;
import com.petwise.domain.appointment.ServiceType;
import java.time.Instant;

/** Output DTO for GetAppointmentByIdUseCase. */
@SuppressWarnings("PMD.ShortVariable")
public record AppointmentOutput(
        String id,
        String petId,
        ServiceType serviceType,
        AppointmentStatus status,
        Instant startAt,
        Instant endAt,
        String notes,
        Instant createdAt,
        Instant updatedAt) {

    public static AppointmentOutput from(final Appointment appointment) {
        return new AppointmentOutput(
                appointment.getId().getValue(),
                appointment.getPetId().getValue(),
                appointment.getServiceType(),
                appointment.getStatus(),
                appointment.getStartAt(),
                appointment.getEndAt(),
                appointment.getNotes(),
                appointment.getCreatedAt(),
                appointment.getUpdatedAt());
    }
}
