package com.petwise.application.pet.retrieve.getbyid;

import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.pet.Pet;
import com.petwise.domain.pet.PetGateway;
import com.petwise.domain.pet.PetID;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link GetPetByIdUseCase}.
 *
 * <p>Looks up the {@link Pet} by its string ID and maps it to a {@link PetOutput} DTO. Throws
 * {@link NotFoundException} when no pet with the given ID exists.
 */
public final class DefaultGetPetByIdUseCase extends GetPetByIdUseCase {

    /** SLF4J logger for this class. */
    private static final Logger LOG = LoggerFactory.getLogger(DefaultGetPetByIdUseCase.class);

    /** The gateway used to look up pets. */
    private final PetGateway petGateway;

    /**
     * Constructs the use case with the required gateway.
     *
     * @param aPetGateway the pet persistence gateway; must not be {@code null}
     */
    public DefaultGetPetByIdUseCase(final PetGateway aPetGateway) {
        super();
        this.petGateway = Objects.requireNonNull(aPetGateway, "PetGateway cannot be null");
    }

    /**
     * {@inheritDoc}
     *
     * <p>Throws {@link NotFoundException} if no pet with the given ID is found.
     */
    @Override
    public PetOutput execute(final String anId) {
        Objects.requireNonNull(anId, "Pet ID cannot be null");
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving pet with id={}", anId);
        }

        final var petId = PetID.from(anId);
        final var pet =
                this.petGateway
                        .findById(petId)
                        .orElseThrow(
                                () -> {
                                    if (LOG.isWarnEnabled()) {
                                        LOG.warn("Pet not found with id={}", anId);
                                    }
                                    return NotFoundException.with(Pet.class, petId);
                                });

        return PetOutput.from(pet);
    }
}
