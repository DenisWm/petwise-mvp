package com.petwise.application.pet.delete;

import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.pet.Pet;
import com.petwise.domain.pet.PetGateway;
import com.petwise.domain.pet.PetID;
import java.util.Objects;

/**
 * Default implementation of {@link DeletePetUseCase}.
 *
 * <p>Verifies the {@link Pet} exists before deletion, throwing {@link NotFoundException} if it
 * cannot be found. This guarantees the caller receives a meaningful error rather than a silent
 * no-op.
 */
public final class DefaultDeletePetUseCase extends DeletePetUseCase {

    /** The gateway used to find and delete pets. */
    private final PetGateway petGateway;

    /**
     * Constructs the use case with the required gateway.
     *
     * @param aPetGateway the pet persistence gateway; must not be {@code null}
     */
    public DefaultDeletePetUseCase(final PetGateway aPetGateway) {
        super();
        this.petGateway = Objects.requireNonNull(aPetGateway, "PetGateway cannot be null");
    }

    /**
     * {@inheritDoc}
     *
     * <p>Finds the pet by ID, throws {@link NotFoundException} if absent, then deletes it.
     */
    @Override
    public void execute(final String anId) {
        Objects.requireNonNull(anId, "Pet ID cannot be null");

        final var petId = PetID.from(anId);

        this.petGateway.findById(petId).orElseThrow(() -> NotFoundException.with(Pet.class, petId));

        this.petGateway.deleteById(petId);
    }
}
