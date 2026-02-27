package com.petwise.infrastructure.appointment.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.petwise.domain.appointment.ServiceType;
import java.time.Instant;

/**
 * Request DTO for creating an appointment.
 *
 * @param petId the pet ID
 * @param serviceType the service type
 * @param startAt the start timestamp
 * @param endAt the end timestamp
 * @param notes optional notes
 */
public record CreateAppointmentRequest(
        @JsonProperty("pet_id") String petId,
        @JsonProperty("service_type") ServiceType serviceType,
        @JsonProperty("start_at") Instant startAt,
        @JsonProperty("end_at") Instant endAt,
        @JsonProperty("notes") String notes) {}
