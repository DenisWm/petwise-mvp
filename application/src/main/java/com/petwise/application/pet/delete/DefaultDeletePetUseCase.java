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
    private static final Logger LOG = LoggerFactory.getLogger(DefaultDeletePetUseCase.class);
    private final PetGateway petGateway;

    public DefaultDeletePetUseCase(final PetGateway aPetGateway) {
        super();
        this.petGateway = Objects.requireNonNull(aPetGateway, "PetGateway cannot be null");
    }

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
