package com.petwise.application.appointment.changestatus;

import com.petwise.application.UseCase;

/**
 * Abstract use case for changing the status of an appointment. Implements UC-04: Change Appointment
 * Status.
 */
@SuppressWarnings("PMD.AbstractClassWithoutAnyMethod")
public abstract class ChangeAppointmentStatusUseCase
        extends UseCase<ChangeAppointmentStatusCommand, ChangeAppointmentStatusOutput> {

    /** Protected constructor for subclasses. */
    protected ChangeAppointmentStatusUseCase() {}
}
