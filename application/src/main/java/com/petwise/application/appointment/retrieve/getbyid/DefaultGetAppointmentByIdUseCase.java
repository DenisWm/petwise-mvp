package com.petwise.application.appointment.retrieve.getbyid;

import com.petwise.domain.appointment.Appointment;
import com.petwise.domain.appointment.AppointmentGateway;
import com.petwise.domain.appointment.AppointmentID;
import com.petwise.domain.exceptions.NotFoundException;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link GetAppointmentByIdUseCase}.
 *
 * <p>Looks up the {@link Appointment} by its string ID and maps it to an {@link AppointmentOutput}
 * DTO. Throws {@link NotFoundException} when no appointment with the given ID exists.
 */
@SuppressWarnings("PMD.LongVariable")
public final class DefaultGetAppointmentByIdUseCase extends GetAppointmentByIdUseCase {
    private static final Logger LOG =
            LoggerFactory.getLogger(DefaultGetAppointmentByIdUseCase.class);
    private final AppointmentGateway appointmentGateway;

    public DefaultGetAppointmentByIdUseCase(final AppointmentGateway anAppointmentGateway) {
        super();
        this.appointmentGateway =
                Objects.requireNonNull(anAppointmentGateway, "AppointmentGateway cannot be null");
    }

    @Override
    public AppointmentOutput execute(final String anId) {
        Objects.requireNonNull(anId, "Appointment ID cannot be null");
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving appointment with id={}", anId);
        }

        final var appointmentId = AppointmentID.from(anId);
        final var appointment =
                this.appointmentGateway
                        .findById(appointmentId)
                        .orElseThrow(
                                () -> {
                                    if (LOG.isWarnEnabled()) {
                                        LOG.warn("Appointment not found with id={}", anId);
                                    }
                                    return NotFoundException.with(Appointment.class, appointmentId);
                                });

        return AppointmentOutput.from(appointment);
    }
}
