package com.petwise.application.appointment.delete;

import com.petwise.application.UnitUseCase;

/**
 * Abstract use case for deleting an appointment. Implements UC-06: Edit/Delete Records (Delete
 * Appointment).
 */
@SuppressWarnings("PMD.AbstractClassWithoutAnyMethod")
public abstract class DeleteAppointmentUseCase extends UnitUseCase<String> {
    protected DeleteAppointmentUseCase() {}
}
