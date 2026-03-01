package com.petwise.application.appointment.delete;

import com.petwise.domain.appointment.Appointment;
import com.petwise.domain.appointment.AppointmentGateway;
import com.petwise.domain.appointment.AppointmentID;
import com.petwise.domain.exceptions.NotFoundException;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link DeleteAppointmentUseCase}.
 *
 * <p>Verifies the {@link Appointment} exists before deletion, throwing {@link NotFoundException} if
 * it cannot be found.
 */
@SuppressWarnings("PMD.LongVariable")
public final class DefaultDeleteAppointmentUseCase extends DeleteAppointmentUseCase {

    /** SLF4J logger for this class. */
    private static final Logger LOG =
            LoggerFactory.getLogger(DefaultDeleteAppointmentUseCase.class);

    /** The gateway used to find and delete appointments. */
    private final AppointmentGateway appointmentGateway;

    /**
     * Constructs the use case with the required gateway.
     *
     * @param anAppointmentGateway the appointment persistence gateway; must not be {@code null}
     */
    public DefaultDeleteAppointmentUseCase(final AppointmentGateway anAppointmentGateway) {
        super();
        this.appointmentGateway =
                Objects.requireNonNull(anAppointmentGateway, "AppointmentGateway cannot be null");
    }

    /**
     * {@inheritDoc}
     *
     * <p>Finds the appointment by ID, throws {@link NotFoundException} if absent, then deletes it.
     */
    @Override
    public void execute(final String anId) {
        Objects.requireNonNull(anId, "Appointment ID cannot be null");
        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting appointment with id={}", anId);
        }

        final var appointmentId = AppointmentID.from(anId);

        this.appointmentGateway
                .findById(appointmentId)
                .orElseThrow(
                        () -> {
                            if (LOG.isWarnEnabled()) {
                                LOG.warn("Appointment not found for deletion with id={}", anId);
                            }
                            return NotFoundException.with(Appointment.class, appointmentId);
                        });

        this.appointmentGateway.deleteById(appointmentId);
        if (LOG.isInfoEnabled()) {
            LOG.info("Appointment deleted successfully with id={}", anId);
        }
    }
}
