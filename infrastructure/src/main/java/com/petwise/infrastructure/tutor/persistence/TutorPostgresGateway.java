package com.petwise.infrastructure.tutor.persistence;

import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorGateway;
import com.petwise.domain.tutor.TutorID;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PostgreSQL implementation of {@link TutorGateway} backed by Spring Data JPA.
 *
 * <p>All write operations are executed inside a transaction. Read operations use
 * {@code readOnly = true} transactions for performance.
 */
@Service
public class TutorPostgresGateway implements TutorGateway {

    private final TutorRepository repository;

    public TutorPostgresGateway(final TutorRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Tutor save(final Tutor tutor) {
        return this.repository.save(TutorJpaEntity.from(tutor)).toAggregate();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Tutor> findById(final TutorID id) {
        return this.repository.findById(id.getValue()).map(TutorJpaEntity::toAggregate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tutor> findAll() {
        return this.repository.findAll().stream().map(TutorJpaEntity::toAggregate).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Pagination<Tutor> findAll(final SearchQuery query) {
        final var sortDirection =
                query.direction().equalsIgnoreCase("desc")
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

    @Override
    @Transactional
    public void deleteById(final TutorID id) {
        this.repository.deleteById(id.getValue());
    }
}
