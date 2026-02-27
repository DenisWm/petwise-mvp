package com.petwise.infrastructure.appointment.presenters;

import com.petwise.application.appointment.retrieve.dailyagenda.ViewDailyAgendaOutput;
import com.petwise.application.appointment.retrieve.getbyid.AppointmentOutput;
import com.petwise.application.appointment.retrieve.list.ListAppointmentsOutput;
import com.petwise.infrastructure.appointment.models.AppointmentResponse;

/** Presenter for converting appointment use-case outputs to API responses. */
public final class AppointmentApiPresenter {

    private AppointmentApiPresenter() {
        // Utility class
    }

    /**
     * Converts an {@link AppointmentOutput} to an {@link AppointmentResponse}.
     *
     * @param output the use case output
     * @return the API response
     */
    public static AppointmentResponse present(final AppointmentOutput output) {
        return new AppointmentResponse(
                output.id(),
                output.petId(),
                output.serviceType(),
                output.status(),
                output.startAt(),
                output.endAt(),
                output.notes(),
                output.createdAt(),
                output.updatedAt());
    }

    /**
     * Converts a {@link ListAppointmentsOutput} to an {@link AppointmentResponse}.
     *
     * @param output the use case output
     * @return the API response
     */
    public static AppointmentResponse present(final ListAppointmentsOutput output) {
        return new AppointmentResponse(
                output.id(),
                output.petId(),
                output.serviceType(),
                output.status(),
                output.startAt(),
                output.endAt(),
                null,
                output.createdAt(),
                output.updatedAt());
    }

    /**
     * Converts a {@link ViewDailyAgendaOutput} to an {@link AppointmentResponse}.
     *
     * @param output the use case output
     * @return the API response
     */
    public static AppointmentResponse present(final ViewDailyAgendaOutput output) {
        return new AppointmentResponse(
                output.id(),
                output.petId(),
                output.serviceType(),
                output.status(),
                output.startAt(),
                output.endAt(),
                output.notes(),
                output.createdAt(),
                output.updatedAt());
    }
}
