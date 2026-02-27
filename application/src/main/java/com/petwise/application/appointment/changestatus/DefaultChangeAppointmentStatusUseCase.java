package com.petwise.application.appointment.changestatus;

import com.petwise.domain.appointment.Appointment;
import com.petwise.domain.appointment.AppointmentGateway;
import com.petwise.domain.appointment.AppointmentID;
import com.petwise.domain.exceptions.NotFoundException;
import java.util.Objects;

/**
 * Default implementation of {@link ChangeAppointmentStatusUseCase}.
 *
 * <p>Fetches the existing {@link Appointment} by ID (throwing {@link NotFoundException} if absent),
 * applies the status transition via {@link Appointment#changeStatus}, and persists the result via
 * the {@link AppointmentGateway}. The domain itself guards invalid transitions.
 */
@SuppressWarnings("PMD.LongVariable")
public final class DefaultChangeAppointmentStatusUseCase extends ChangeAppointmentStatusUseCase {

    /** The gateway used to find and persist appointments. */
    private final AppointmentGateway appointmentGateway;

    /**
     * Constructs the use case with the required gateway.
     *
     * @param anAppointmentGateway the appointment persistence gateway; must not be {@code null}
     */
    public DefaultChangeAppointmentStatusUseCase(final AppointmentGateway anAppointmentGateway) {
        super();
        this.appointmentGateway =
                Objects.requireNonNull(anAppointmentGateway, "AppointmentGateway cannot be null");
    }

    /**
     * {@inheritDoc}
     *
     * <p>Loads the appointment, delegates the status transition to the domain, and persists the
     * result. Throws {@link NotFoundException} if the appointment is not found, or a {@link
     * com.petwise.domain.exceptions.DomainException} if the transition is invalid.
     */
    @Override
    public ChangeAppointmentStatusOutput execute(final ChangeAppointmentStatusCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        final var appointmentId = AppointmentID.from(command.id());
        final var appointment =
                this.appointmentGateway
                        .findById(appointmentId)
                        .orElseThrow(
                                () -> NotFoundException.with(Appointment.class, appointmentId));

        appointment.changeStatus(command.status());

        return ChangeAppointmentStatusOutput.from(this.appointmentGateway.save(appointment));
    }
}
