package com.petwise.infrastructure.appointment.presenters;

import com.petwise.application.appointment.retrieve.dailyagenda.ViewDailyAgendaOutput;
import com.petwise.application.appointment.retrieve.getbyid.AppointmentOutput;
import com.petwise.application.appointment.retrieve.list.ListAppointmentsOutput;
import com.petwise.infrastructure.appointment.models.AppointmentResponse;

/** Converts appointment use-case outputs to API responses. */
public final class AppointmentApiPresenter {

    private AppointmentApiPresenter() {}

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
