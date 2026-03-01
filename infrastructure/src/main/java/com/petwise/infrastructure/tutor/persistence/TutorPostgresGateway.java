package com.petwise.infrastructure.tutor.persistence;

import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorGateway;
import com.petwise.domain.tutor.TutorID;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PostgreSQL implementation of {@link TutorGateway} backed by Spring Data JPA.
 *
 * <p>All write operations are executed inside a transaction. Read operations use {@code readOnly =
 * true} transactions for performance.
 */
@Service
@SuppressWarnings({"PMD.ShortVariable", "PMD.LiteralsFirstInComparisons"})
public class TutorPostgresGateway implements TutorGateway {

    /** SLF4J logger for this class. */
    private static final Logger LOG = LoggerFactory.getLogger(TutorPostgresGateway.class);

    /** The underlying Spring Data JPA repository. */
    private final TutorRepository repository;

    /**
     * Constructs the gateway with the required repository.
     *
     * @param aRepository the JPA repository; must not be {@code null}
     */
    public TutorPostgresGateway(final TutorRepository aRepository) {
        this.repository = aRepository;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Subclasses may override for alternative persistence strategy.
     */
    @Override
    @Transactional
    public Tutor save(final Tutor tutor) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Persisting tutor with id={}", tutor.getId().getValue());
        }
        return this.repository.save(TutorJpaEntity.from(tutor)).toAggregate();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Subclasses may override for alternative lookup strategy.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Tutor> findById(final TutorID anId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Finding tutor by id={}", anId.getValue());
        }
        return this.repository.findById(anId.getValue()).map(TutorJpaEntity::toAggregate);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Subclasses may override for alternative retrieval strategy.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Tutor> findAll() {
        return this.repository.findAll().stream().map(TutorJpaEntity::toAggregate).toList();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Subclasses may override for alternative pagination strategy.
     */
    @Override
    @Transactional(readOnly = true)
    public Pagination<Tutor> findAll(final SearchQuery query) {
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

        final var tutors = page.getContent().stream().map(TutorJpaEntity::toAggregate).toList();

        return new Pagination<>(page.getNumber(), page.getSize(), page.getTotalElements(), tutors);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Subclasses may override for alternative deletion strategy.
     */
    @Override
    @Transactional
    public void deleteById(final TutorID anId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Deleting tutor by id={}", anId.getValue());
        }
        this.repository.deleteById(anId.getValue());
    }
}
