package com.petwise.application.pet.retrieve.list;

import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import com.petwise.domain.pet.PetGateway;
import java.util.Objects;

/**
 * Default implementation of {@link ListPetsUseCase}.
 *
 * <p>Delegates pagination and optional full-text search to the {@link PetGateway} and maps each
 * result to a {@link ListPetsOutput} DTO.
 */
public final class DefaultListPetsUseCase extends ListPetsUseCase {

    /** The gateway used to query pets. */
    private final PetGateway petGateway;

    /**
     * Constructs the use case with the required gateway.
     *
     * @param aPetGateway the pet persistence gateway; must not be {@code null}
     */
    public DefaultListPetsUseCase(final PetGateway aPetGateway) {
        super();
        this.petGateway = Objects.requireNonNull(aPetGateway, "PetGateway cannot be null");
    }

    /**
     * {@inheritDoc}
     *
     * <p>Subclasses may override to customise pagination behaviour.
     */
    @Override
    public Pagination<ListPetsOutput> execute(final SearchQuery query) {
        Objects.requireNonNull(query, "SearchQuery cannot be null");

        return this.petGateway.findAll(query).map(ListPetsOutput::from);
    }
}
