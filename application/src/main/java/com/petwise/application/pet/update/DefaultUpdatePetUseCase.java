package com.petwise.application.pet.update;

import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.exceptions.NotificationException;
import com.petwise.domain.pet.Pet;
import com.petwise.domain.pet.PetGateway;
import com.petwise.domain.pet.PetID;
import com.petwise.domain.validation.handler.Notification;
import java.util.Objects;

/**
 * Default implementation of {@link UpdatePetUseCase}.
 *
 * <p>Fetches the existing {@link Pet} by ID (throwing {@link NotFoundException} if absent), applies
 * the new values from the {@link UpdatePetCommand}, validates domain invariants, and persists the
 * result via the {@link PetGateway}.
 */
public final class DefaultUpdatePetUseCase extends UpdatePetUseCase {

    /** The gateway used to find and persist pets. */
    private final PetGateway petGateway;

    /**
     * Constructs the use case with the required gateway.
     *
     * @param aPetGateway the pet persistence gateway; must not be {@code null}
     */
    public DefaultUpdatePetUseCase(final PetGateway aPetGateway) {
        super();
        this.petGateway = Objects.requireNonNull(aPetGateway, "PetGateway cannot be null");
    }

    /**
     * {@inheritDoc}
     *
     * <p>Loads the pet, applies updates, validates, and persists. Throws {@link
     * NotificationException} if validation fails.
     */
    @Override
    public UpdatePetOutput execute(final UpdatePetCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        final var petId = PetID.from(command.id());
        final var pet =
                this.petGateway
                        .findById(petId)
                        .orElseThrow(() -> NotFoundException.with(Pet.class, petId));

        pet.update(
                command.name(),
                command.species(),
                command.breed(),
                command.birthDate(),
                command.notes());

        final var notification = Notification.create();
        pet.validate(notification);

        if (notification.hasErrors()) {
            throw new NotificationException(
                    "Could not update Entity Pet %s".formatted(command.id()), notification);
        }

        return UpdatePetOutput.from(this.petGateway.save(pet));
    }
}
