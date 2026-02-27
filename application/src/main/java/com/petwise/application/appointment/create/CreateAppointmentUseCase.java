package com.petwise.application.appointment.create;

import com.petwise.application.UseCase;

/** Abstract use case for creating a new appointment. Implements UC-03: Create Appointment. */
@SuppressWarnings("PMD.AbstractClassWithoutAnyMethod")
public abstract class CreateAppointmentUseCase
        extends UseCase<CreateAppointmentCommand, CreateAppointmentOutput> {

    /** Protected constructor for subclasses. */
    protected CreateAppointmentUseCase() {}
}
