package com.petwise.application.appointment.retrieve.dailyagenda;

import com.petwise.domain.appointment.AppointmentStatus;
import com.petwise.domain.appointment.ServiceType;
import java.time.LocalDate;
import java.util.Objects;

/** Command for the View Daily Agenda use case. */
public record ViewDailyAgendaCommand(
        LocalDate date,
        AppointmentStatus status,
        ServiceType serviceType,
        int page,
        int perPage,
        String sort,
        String direction) {

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
