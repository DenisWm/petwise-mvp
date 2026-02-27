package com.petwise.application.appointment.retrieve.dailyagenda;

import com.petwise.domain.appointment.AppointmentStatus;
import com.petwise.domain.appointment.ServiceType;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Command for the View Daily Agenda use case.
 *
 * @param date the date to query; must not be {@code null}
 * @param status optional status filter; {@code null} means all statuses
 * @param serviceType optional service type filter; {@code null} means all types
 * @param page zero-based page index
 * @param perPage number of items per page
 * @param sort field name to sort by
 * @param direction sort direction – {@code "asc"} or {@code "desc"}
 */
public record ViewDailyAgendaCommand(
        LocalDate date,
        AppointmentStatus status,
        ServiceType serviceType,
        int page,
        int perPage,
        String sort,
        String direction) {

    /**
     * Factory method that creates a command with all parameters.
     *
     * @param aDate the date to view
     * @param aStatus optional status filter
     * @param aServiceType optional service type filter
     * @param aPage the page number
     * @param aPerPage items per page
     * @param aSort sort field
     * @param aDirection sort direction
     * @return a new command instance
     */
    public static ViewDailyAgendaCommand with(
            final LocalDate aDate,
            final AppointmentStatus aStatus,
            final ServiceType aServiceType,
            final int aPage,
            final int aPerPage,
            final String aSort,
            final String aDirection) {
        Objects.requireNonNull(aDate, "'date' must not be null");
        return new ViewDailyAgendaCommand(
                aDate, aStatus, aServiceType, aPage, aPerPage, aSort, aDirection);
    }
}
