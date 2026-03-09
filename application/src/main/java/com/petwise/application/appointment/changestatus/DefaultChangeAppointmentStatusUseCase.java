package com.petwise.application.appointment.changestatus;

import com.petwise.domain.appointment.Appointment;
import com.petwise.domain.appointment.AppointmentGateway;
import com.petwise.domain.appointment.AppointmentID;
import com.petwise.domain.exceptions.NotFoundException;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link ChangeAppointmentStatusUseCase}.
 *
 * <p>Fetches the existing {@link Appointment} by ID (throwing {@link NotFoundException} if absent),
 * applies the status transition via {@link Appointment#changeStatus}, and persists the result via
 * the {@link AppointmentGateway}. The domain itself guards invalid transitions.
 */
@SuppressWarnings("PMD.LongVariable")
public final class DefaultChangeAppointmentStatusUseCase extends ChangeAppointmentStatusUseCase {
    private static final Logger LOG =
            LoggerFactory.getLogger(DefaultChangeAppointmentStatusUseCase.class);
    private final AppointmentGateway appointmentGateway;

    public DefaultChangeAppointmentStatusUseCase(final AppointmentGateway anAppointmentGateway) {
        super();
        this.appointmentGateway =
                Objects.requireNonNull(anAppointmentGateway, "AppointmentGateway cannot be null");
    }

    @Override
    public ChangeAppointmentStatusOutput execute(final ChangeAppointmentStatusCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        if (LOG.isDebugEnabled()) {
            LOG.debug(
                    "Changing appointment status for id={}, targetStatus={}",
                    command.id(),
                    command.status());
        }

        final var appointmentId = AppointmentID.from(command.id());
        final var appointment =
                this.appointmentGateway
                        .findById(appointmentId)
                        .orElseThrow(
                                () -> {
                                    if (LOG.isWarnEnabled()) {
                                        LOG.warn(
                                                "Appointment not found for status change with id={}",
                                                command.id());
                                    }
                                    return NotFoundException.with(Appointment.class, appointmentId);
                                });

        final var previousStatus = appointment.getStatus();
        appointment.changeStatus(command.status());

        final var output =
                ChangeAppointmentStatusOutput.from(this.appointmentGateway.save(appointment));
        if (LOG.isInfoEnabled()) {
            LOG.info(
                    "Appointment status changed: id={}, from={}, to={}",
                    output.id(),
                    previousStatus,
                    command.status());
        }
        return output;
    }
}
