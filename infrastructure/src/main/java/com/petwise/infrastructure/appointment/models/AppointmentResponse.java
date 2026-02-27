package com.petwise.infrastructure.appointment.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.petwise.domain.appointment.AppointmentStatus;
import com.petwise.domain.appointment.ServiceType;
import java.time.Instant;

/**
 * Response DTO for appointment details.
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
public record AppointmentResponse(
        @JsonProperty("id") String id,
        @JsonProperty("pet_id") String petId,
        @JsonProperty("service_type") ServiceType serviceType,
        @JsonProperty("status") AppointmentStatus status,
        @JsonProperty("start_at") Instant startAt,
        @JsonProperty("end_at") Instant endAt,
        @JsonProperty("notes") String notes,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt) {}
