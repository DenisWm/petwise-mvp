package com.petwise.application.appointment.create;

import com.petwise.domain.appointment.Appointment;
import com.petwise.domain.appointment.AppointmentGateway;
import com.petwise.domain.exceptions.NotificationException;
import com.petwise.domain.pet.PetID;
import com.petwise.domain.validation.handler.Notification;
import java.util.Objects;

/**
 * Default implementation of {@link CreateAppointmentUseCase}.
 *
 * <p>Validates the {@link CreateAppointmentCommand} against domain invariants via a {@link
 * Notification} handler and persists the new {@link Appointment} through the {@link
 * AppointmentGateway}. Throws {@link NotificationException} if any constraint is violated.
 */
@SuppressWarnings("PMD.LongVariable")
public final class DefaultCreateAppointmentUseCase extends CreateAppointmentUseCase {

    /** The gateway used to persist the appointment. */
    private final AppointmentGateway appointmentGateway;

    /**
     * Constructs the use case with the required gateway.
     *
     * @param anAppointmentGateway the appointment persistence gateway; must not be {@code null}
     */
    public DefaultCreateAppointmentUseCase(final AppointmentGateway anAppointmentGateway) {
        super();
        this.appointmentGateway =
                Objects.requireNonNull(anAppointmentGateway, "AppointmentGateway cannot be null");
    }

    /**
     * {@inheritDoc}
     *
     * <p>Validates invariants and persists the new appointment. Throws {@link
     * NotificationException} if validation fails.
     */
    @Override
    public CreateAppointmentOutput execute(final CreateAppointmentCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        final var petId = PetID.from(command.petId());
        final var appointment =
                Appointment.newAppointment(
                        petId,
                        command.serviceType(),
                        command.startAt(),
                        command.endAt(),
                        command.notes());

        final var notification = Notification.create();
        appointment.validate(notification);

        if (notification.hasErrors()) {
            throw new NotificationException("Could not create Aggregate Appointment", notification);
        }

        return CreateAppointmentOutput.from(this.appointmentGateway.save(appointment));
    }
}
