package com.petwise.domain.appointment;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Query object for the daily agenda — date to view, optional status/service-type filters, and
 * pagination parameters.
 */
public record AppointmentSearchQuery(
        LocalDate date,
        AppointmentStatus status,
        ServiceType serviceType,
        int page,
        int perPage,
        String sort,
        String direction) {

    public AppointmentSearchQuery {
        Objects.requireNonNull(date, "'date' must not be null");
    }
}
