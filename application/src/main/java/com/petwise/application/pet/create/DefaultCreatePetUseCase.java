package com.petwise.application.pet.create;

import com.petwise.domain.exceptions.NotificationException;
import com.petwise.domain.pet.Pet;
import com.petwise.domain.pet.PetGateway;
import com.petwise.domain.tutor.TutorID;
import com.petwise.domain.validation.handler.Notification;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link CreatePetUseCase}.
 *
 * <p>Validates the {@link CreatePetCommand} against domain invariants via a {@link Notification}
 * handler and persists the new {@link Pet} through the {@link PetGateway}. Throws {@link
 * NotificationException} if any constraint is violated.
 */
public final class DefaultCreatePetUseCase extends CreatePetUseCase {

    /** SLF4J logger for this class. */
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCreatePetUseCase.class);

    /** The gateway used to persist the pet. */
    private final PetGateway petGateway;

    /**
     * Constructs the use case with the required gateway.
     *
     * @param aPetGateway the pet persistence gateway; must not be {@code null}
     */
    public DefaultCreatePetUseCase(final PetGateway aPetGateway) {
        super();
        this.petGateway = Objects.requireNonNull(aPetGateway, "PetGateway cannot be null");
    }

    /**
     * {@inheritDoc}
     *
     * <p>Validates invariants and persists the new pet. Throws {@link NotificationException} if
     * validation fails.
     */
    @Override
    public CreatePetOutput execute(final CreatePetCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        if (LOG.isDebugEnabled()) {
            LOG.debug(
                    "Creating pet with name={}, species={}, tutorId={}",
                    command.name(),
                    command.species(),
                    command.tutorId());
        }

        final var tutorId = TutorID.from(command.tutorId());
        final var pet =
                Pet.newPet(
                        tutorId,
                        command.name(),
                        command.species(),
                        command.breed(),
                        command.birthDate(),
                        command.notes());

        final var notification = Notification.create();
        pet.validate(notification);

        if (notification.hasErrors()) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Pet creation validation failed: {}", notification.getErrors());
            }
            throw new NotificationException("Could not create Entity Pet", notification);
        }

        final var output = CreatePetOutput.from(this.petGateway.save(pet));
        if (LOG.isInfoEnabled()) {
            LOG.info("Pet created successfully with id={}", output.id());
        }
        return output;
    }
}
