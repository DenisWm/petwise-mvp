package com.petwise.application.appointment.retrieve.list;

import com.petwise.domain.appointment.Appointment;
import com.petwise.domain.appointment.AppointmentStatus;
import com.petwise.domain.appointment.ServiceType;
import java.time.Instant;

/** Output DTO for a single appointment in list results. */
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
