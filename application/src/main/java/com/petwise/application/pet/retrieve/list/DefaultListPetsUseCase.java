package com.petwise.application.pet.retrieve.list;

import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import com.petwise.domain.pet.PetGateway;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link ListPetsUseCase}.
 *
 * <p>Delegates pagination and optional full-text search to the {@link PetGateway} and maps each
 * result to a {@link ListPetsOutput} DTO.
 */
public final class DefaultListPetsUseCase extends ListPetsUseCase {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultListPetsUseCase.class);
    private final PetGateway petGateway;

    public DefaultListPetsUseCase(final PetGateway aPetGateway) {
        super();
        this.petGateway = Objects.requireNonNull(aPetGateway, "PetGateway cannot be null");
    }

    @Override
    public Pagination<ListPetsOutput> execute(final SearchQuery query) {
        Objects.requireNonNull(query, "SearchQuery cannot be null");
        if (LOG.isDebugEnabled()) {
            LOG.debug(
                    "Listing pets: page={}, perPage={}, terms={}",
                    query.page(),
                    query.perPage(),
                    query.terms());
        }

        return this.petGateway.findAll(query).map(ListPetsOutput::from);
    }
}
