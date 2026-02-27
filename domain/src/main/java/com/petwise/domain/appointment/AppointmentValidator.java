package com.petwise.domain.appointment;

import com.petwise.domain.validation.Error;
import com.petwise.domain.validation.ValidationHandler;
import com.petwise.domain.validation.Validator;
import java.time.Instant;

/** Validator for Appointment aggregate. */
@SuppressWarnings("PMD.OnlyOneReturn")
public final class AppointmentValidator extends Validator {

    /** The appointment being validated. */
    private final Appointment appointment;

    /**
     * Constructs an AppointmentValidator for the given appointment and handler.
     *
     * @param anAppointment the appointment to validate
     * @param handler the handler to collect errors
     */
    public AppointmentValidator(final Appointment anAppointment, final ValidationHandler handler) {
        super(handler);
        this.appointment = anAppointment;
    }

    /**
     * Validates the appointment aggregate invariants.
     *
     * <p>Subclasses may override to add additional validation rules.
     */
    @Override
    public void validate() {
        checkRequiredFields();
        checkDateRange();
    }

    private void checkRequiredFields() {
        if (appointment.getPetId() == null) {
            this.getValidationHandler().append(new Error("'petId' should not be null"));
        }
        if (appointment.getServiceType() == null) {
            this.getValidationHandler().append(new Error("'serviceType' should not be null"));
        }
        if (appointment.getStatus() == null) {
            this.getValidationHandler().append(new Error("'status' should not be null"));
        }
        if (appointment.getStartAt() == null) {
            this.getValidationHandler().append(new Error("'startAt' should not be null"));
        }
        if (appointment.getEndAt() == null) {
            this.getValidationHandler().append(new Error("'endAt' should not be null"));
        }
    }

    private void checkDateRange() {
        final Instant startAt = appointment.getStartAt();
        final Instant endAt = appointment.getEndAt();
        if (startAt == null || endAt == null) {
            return;
        }
        if (!startAt.isBefore(endAt)) {
            this.getValidationHandler().append(new Error("'startAt' must be before 'endAt'"));
        }
    }
}
