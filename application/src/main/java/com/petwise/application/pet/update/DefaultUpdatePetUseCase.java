package com.petwise.application.pet.update;

import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.exceptions.NotificationException;
import com.petwise.domain.pet.Pet;
import com.petwise.domain.pet.PetGateway;
import com.petwise.domain.pet.PetID;
import com.petwise.domain.validation.handler.Notification;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link UpdatePetUseCase}.
 *
 * <p>Fetches the existing {@link Pet} by ID (throwing {@link NotFoundException} if absent), applies
 * the new values from the {@link UpdatePetCommand}, validates domain invariants, and persists the
 * result via the {@link PetGateway}.
 */
public final class DefaultUpdatePetUseCase extends UpdatePetUseCase {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultUpdatePetUseCase.class);
    private final PetGateway petGateway;

    public DefaultUpdatePetUseCase(final PetGateway aPetGateway) {
        super();
        this.petGateway = Objects.requireNonNull(aPetGateway, "PetGateway cannot be null");
    }

    @Override
    public UpdatePetOutput execute(final UpdatePetCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");
        if (LOG.isDebugEnabled()) {
            LOG.debug("Updating pet id={}", command.id());
        }

        final var petId = PetID.from(command.id());
        final var pet =
                this.petGateway
                        .findById(petId)
                        .orElseThrow(
                                () -> {
                                    if (LOG.isWarnEnabled()) {
                                        LOG.warn(
                                                "Pet not found for update with id={}",
                                                command.id());
                                    }
                                    return NotFoundException.with(Pet.class, petId);
                                });

        pet.update(
                command.name(),
                command.species(),
                command.breed(),
                command.birthDate(),
                command.notes());

        final var notification = Notification.create();
        pet.validate(notification);

        if (notification.hasErrors()) {
            if (LOG.isWarnEnabled()) {
                LOG.warn(
                        "Pet update validation failed for id={}: {}",
                        command.id(),
                        notification.getErrors());
            }
            throw new NotificationException(
                    "Could not update Entity Pet %s".formatted(command.id()), notification);
        }

        final var output = UpdatePetOutput.from(this.petGateway.save(pet));
        if (LOG.isInfoEnabled()) {
            LOG.info("Pet updated successfully with id={}", output.id());
        }
        return output;
    }
}
