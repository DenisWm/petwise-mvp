package com.petwise.domain.appointment;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Immutable query object for the daily agenda. Captures the date to view, optional filters for
 * status and service type, and standard pagination parameters.
 *
 * @param date the date to query; must not be {@code null}
 * @param status optional status filter; {@code null} means all statuses
 * @param serviceType optional service type filter; {@code null} means all types
 * @param page zero-based page index
 * @param perPage number of items per page
 * @param sort field name to sort by
 * @param direction sort direction – {@code "asc"} or {@code "desc"}
 */
public record AppointmentSearchQuery(
        LocalDate date,
        AppointmentStatus status,
        ServiceType serviceType,
        int page,
        int perPage,
        String sort,
        String direction) {

    /** Compact constructor that validates the required date parameter. */
    public AppointmentSearchQuery {
        Objects.requireNonNull(date, "'date' must not be null");
    }
}
