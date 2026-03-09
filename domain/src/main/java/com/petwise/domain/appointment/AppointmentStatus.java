package com.petwise.domain.appointment;

/** Appointment lifecycle status with forward-only transitions. */
public enum AppointmentStatus {
    PENDING,
    ACTIVE,
    COMPLETED,
    CANCELED;

    public boolean canTransitionTo(final AppointmentStatus next) {
        return next != null
                && switch (this) {
                    case PENDING -> next == ACTIVE || next == CANCELED;
                    case ACTIVE -> next == COMPLETED;
                    case COMPLETED, CANCELED -> false;
                };
    }
}
