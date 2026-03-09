package com.petwise.application.appointment.retrieve.dailyagenda;

import com.petwise.domain.appointment.Appointment;
import com.petwise.domain.appointment.AppointmentStatus;
import com.petwise.domain.appointment.ServiceType;
import java.time.Instant;

/** Output DTO for a single appointment in the daily agenda results. */
@SuppressWarnings("PMD.ShortVariable")
public record ViewDailyAgendaOutput(
        String id,
        String petId,
        ServiceType serviceType,
        AppointmentStatus status,
        Instant startAt,
        Instant endAt,
        String notes,
        Instant createdAt,
        Instant updatedAt) {

    public static ViewDailyAgendaOutput from(final Appointment appointment) {
        return new ViewDailyAgendaOutput(
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
