package com.petwise.application.pet.retrieve.getbyid;

import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.pet.Pet;
import com.petwise.domain.pet.PetGateway;
import com.petwise.domain.pet.PetID;
import java.util.Objects;

/**
 * Default implementation of {@link GetPetByIdUseCase}.
 *
 * <p>Looks up the {@link Pet} by its string ID and maps it to a {@link PetOutput} DTO. Throws
 * {@link NotFoundException} when no pet with the given ID exists.
 */
public final class DefaultGetPetByIdUseCase extends GetPetByIdUseCase {

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

        final var petId = PetID.from(anId);
        final var pet =
                this.petGateway
                        .findById(petId)
                        .orElseThrow(() -> NotFoundException.with(Pet.class, petId));

        return PetOutput.from(pet);
    }
}
