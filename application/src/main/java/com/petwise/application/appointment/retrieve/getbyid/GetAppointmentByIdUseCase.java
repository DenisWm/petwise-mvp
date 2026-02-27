package com.petwise.application.appointment.retrieve.getbyid;

import com.petwise.application.UseCase;

/** Abstract use case for retrieving an appointment by ID. */
@SuppressWarnings("PMD.AbstractClassWithoutAnyMethod")
public abstract class GetAppointmentByIdUseCase extends UseCase<String, AppointmentOutput> {

    /** Protected constructor for subclasses. */
    protected GetAppointmentByIdUseCase() {}
}
