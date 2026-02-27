package com.petwise.infrastructure.appointment.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.petwise.domain.appointment.AppointmentStatus;

/**
 * Request DTO for changing an appointment's status.
 *
 * @param status the new appointment status
 */
public record ChangeAppointmentStatusRequest(@JsonProperty("status") AppointmentStatus status) {}
