package com.petwise.infrastructure.appointment.persistence;

import com.petwise.domain.appointment.Appointment;
import com.petwise.domain.appointment.AppointmentGateway;
import com.petwise.domain.appointment.AppointmentID;
import com.petwise.domain.appointment.AppointmentSearchQuery;
import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * PostgreSQL implementation of {@link AppointmentGateway} backed by Spring Data JPA.
 *
 * <p>All write operations are executed inside a transaction. Read operations use {@code readOnly =
 * true} transactions for performance.
 */
@Service
@SuppressWarnings({"PMD.ShortVariable", "PMD.LiteralsFirstInComparisons"})
public class AppointmentPostgresGateway implements AppointmentGateway {

    /** The underlying Spring Data JPA repository. */
    private final AppointmentRepository repository;

    /**
     * Constructs the gateway with the required repository.
     *
     * @param aRepository the JPA repository; must not be {@code null}
     */
    public AppointmentPostgresGateway(final AppointmentRepository aRepository) {
        this.repository = aRepository;
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public Appointment save(final Appointment appointment) {
        return this.repository.save(AppointmentJpaEntity.from(appointment)).toAggregate();
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Optional<Appointment> findById(final AppointmentID anId) {
        return this.repository.findById(anId.getValue()).map(AppointmentJpaEntity::toAggregate);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<Appointment> findAll() {
        return this.repository.findAll().stream().map(AppointmentJpaEntity::toAggregate).toList();
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Pagination<Appointment> findAll(final SearchQuery query) {
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

        return toPagination(page);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Pagination<Appointment> findDailyAgenda(final AppointmentSearchQuery query) {
        final var startOfDay = query.date().atStartOfDay(ZoneOffset.UTC).toInstant();
        final var endOfDay = query.date().plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        final var sortDirection =
                "desc".equalsIgnoreCase(query.direction())
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        final var pageable =
                PageRequest.of(query.page(), query.perPage(), Sort.by(sortDirection, query.sort()));

        final var hasStatus = query.status() != null;
        final var hasServiceType = query.serviceType() != null;

        final Page<AppointmentJpaEntity> page;
        if (hasStatus && hasServiceType) {
            page =
                    this.repository
                            .findByStartAtGreaterThanEqualAndStartAtLessThanAndStatusAndServiceType(
                                    startOfDay,
                                    endOfDay,
                                    query.status(),
                                    query.serviceType(),
                                    pageable);
        } else if (hasStatus) {
            page =
                    this.repository.findByStartAtGreaterThanEqualAndStartAtLessThanAndStatus(
                            startOfDay, endOfDay, query.status(), pageable);
        } else if (hasServiceType) {
            page =
                    this.repository.findByStartAtGreaterThanEqualAndStartAtLessThanAndServiceType(
                            startOfDay, endOfDay, query.serviceType(), pageable);
        } else {
            page =
                    this.repository.findByStartAtGreaterThanEqualAndStartAtLessThan(
                            startOfDay, endOfDay, pageable);
        }

        return toPagination(page);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public void deleteById(final AppointmentID anId) {
        this.repository.deleteById(anId.getValue());
    }

    private static Pagination<Appointment> toPagination(final Page<AppointmentJpaEntity> page) {
        final var appointments =
                page.getContent().stream().map(AppointmentJpaEntity::toAggregate).toList();
        return new Pagination<>(
                page.getNumber(), page.getSize(), page.getTotalElements(), appointments);
    }
}
