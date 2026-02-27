package com.petwise.application.appointment.retrieve.getbyid;

import com.petwise.domain.appointment.Appointment;
import com.petwise.domain.appointment.AppointmentStatus;
import com.petwise.domain.appointment.ServiceType;
import java.time.Instant;

/**
 * Output DTO for GetAppointmentByIdUseCase.
 *
 * @param id the appointment ID
 * @param petId the pet ID
 * @param serviceType the service type
 * @param status the appointment status
 * @param startAt the start timestamp
 * @param endAt the end timestamp
 * @param notes optional notes
 * @param createdAt the creation timestamp
 * @param updatedAt the last update timestamp
 */
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

    /**
     * Creates an {@code AppointmentOutput} from a domain {@link Appointment}.
     *
     * @param appointment the appointment to map
     * @return a new {@code AppointmentOutput}
     */
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
