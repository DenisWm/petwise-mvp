package com.petwise.infrastructure.pet.persistence;

import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import com.petwise.domain.pet.Pet;
import com.petwise.domain.pet.PetGateway;
import com.petwise.domain.pet.PetID;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PostgreSQL implementation of {@link PetGateway} backed by Spring Data JPA.
 *
 * <p>All write operations are executed inside a transaction. Read operations use {@code readOnly =
 * true} transactions for performance.
 */
@Service
@SuppressWarnings({"PMD.ShortVariable", "PMD.LiteralsFirstInComparisons"})
public class PetPostgresGateway implements PetGateway {
    private static final Logger LOG = LoggerFactory.getLogger(PetPostgresGateway.class);
    private final PetRepository repository;

    /**
     * Constructs the gateway with the required repository.
     *
     * @param aRepository the JPA repository; must not be {@code null}
     */
    public PetPostgresGateway(final PetRepository aRepository) {
        this.repository = aRepository;
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public Pet save(final Pet pet) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Persisting pet with id={}", pet.getId().getValue());
        }
        return this.repository.save(PetJpaEntity.from(pet)).toAggregate();
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Optional<Pet> findById(final PetID anId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finding pet by id={}", anId.getValue());
        }
        return this.repository.findById(anId.getValue()).map(PetJpaEntity::toAggregate);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<Pet> findAll() {
        return this.repository.findAll().stream().map(PetJpaEntity::toAggregate).toList();
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Pagination<Pet> findAll(final SearchQuery query) {
        final var sortDirection =
                "desc".equalsIgnoreCase(query.direction())
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        final var pageable =
                PageRequest.of(query.page(), query.perPage(), Sort.by(sortDirection, query.sort()));

        final var page =
                (query.terms() == null || query.terms().isBlank())
                        ? this.repository.findAll(pageable)
                        : this.repository.findBySearchTerms(query.terms(), pageable);

        final var pets = page.getContent().stream().map(PetJpaEntity::toAggregate).toList();

        return new Pagination<>(page.getNumber(), page.getSize(), page.getTotalElements(), pets);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public void deleteById(final PetID anId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting pet by id={}", anId.getValue());
        }
        this.repository.deleteById(anId.getValue());
    }
}
