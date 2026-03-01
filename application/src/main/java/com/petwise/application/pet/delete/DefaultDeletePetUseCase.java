package com.petwise.application.pet.delete;

import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.pet.Pet;
import com.petwise.domain.pet.PetGateway;
import com.petwise.domain.pet.PetID;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link DeletePetUseCase}.
 *
 * <p>Verifies the {@link Pet} exists before deletion, throwing {@link NotFoundException} if it
 * cannot be found. This guarantees the caller receives a meaningful error rather than a silent
 * no-op.
 */
public final class DefaultDeletePetUseCase extends DeletePetUseCase {

    /** SLF4J logger for this class. */
    private static final Logger LOG = LoggerFactory.getLogger(DefaultDeletePetUseCase.class);

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
        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting pet with id={}", anId);
        }

        final var petId = PetID.from(anId);

        this.petGateway
                .findById(petId)
                .orElseThrow(
                        () -> {
                            if (LOG.isWarnEnabled()) {
                                LOG.warn("Pet not found for deletion with id={}", anId);
                            }
                            return NotFoundException.with(Pet.class, petId);
                        });

        this.petGateway.deleteById(petId);
        if (LOG.isInfoEnabled()) {
            LOG.info("Pet deleted successfully with id={}", anId);
        }
    }
}
