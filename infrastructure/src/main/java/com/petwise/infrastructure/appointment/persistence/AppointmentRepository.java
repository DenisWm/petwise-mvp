package com.petwise.infrastructure.appointment.persistence;

import com.petwise.domain.appointment.AppointmentStatus;
import com.petwise.domain.appointment.ServiceType;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for {@link AppointmentJpaEntity}.
 *
 * <p>Provides built-in CRUD and pagination methods plus custom queries for filtering by status,
 * service type, search terms, and date range (daily agenda).
 */
public interface AppointmentRepository extends JpaRepository<AppointmentJpaEntity, String> {

    /**
     * Finds appointments whose pet ID or service type contains the given terms (case-insensitive).
     *
     * @param terms the search string; must not be {@code null}
     * @param pageable pagination parameters
     * @return a page of matching entities
     */
    @Query(
            """
            SELECT a FROM AppointmentJpaEntity a
            WHERE LOWER(CAST(a.serviceType AS string)) LIKE LOWER(CONCAT('%', :terms, '%'))
               OR LOWER(CAST(a.status AS string)) LIKE LOWER(CONCAT('%', :terms, '%'))
            """)
    Page<AppointmentJpaEntity> findBySearchTerms(@Param("terms") String terms, Pageable pageable);

    /**
     * Finds all appointments with a given status.
     *
     * @param status the appointment status filter
     * @param pageable pagination parameters
     * @return a page of matching entities
     */
    Page<AppointmentJpaEntity> findByStatus(AppointmentStatus status, Pageable pageable);

    /**
     * Finds appointments whose start time falls within the given range.
     *
     * @param startOfDay the start of the day (inclusive)
     * @param endOfDay the end of the day (exclusive)
     * @param pageable pagination parameters
     * @return a page of matching entities
     */
    Page<AppointmentJpaEntity> findByStartAtGreaterThanEqualAndStartAtLessThan(
            Instant startOfDay, Instant endOfDay, Pageable pageable);

    /**
     * Finds appointments by date range and status.
     *
     * @param startOfDay the start of the day (inclusive)
     * @param endOfDay the end of the day (exclusive)
     * @param status the status filter
     * @param pageable pagination parameters
     * @return a page of matching entities
     */
    Page<AppointmentJpaEntity> findByStartAtGreaterThanEqualAndStartAtLessThanAndStatus(
            Instant startOfDay, Instant endOfDay, AppointmentStatus status, Pageable pageable);

    /**
     * Finds appointments by date range and service type.
     *
     * @param startOfDay the start of the day (inclusive)
     * @param endOfDay the end of the day (exclusive)
     * @param serviceType the service type filter
     * @param pageable pagination parameters
     * @return a page of matching entities
     */
    Page<AppointmentJpaEntity> findByStartAtGreaterThanEqualAndStartAtLessThanAndServiceType(
            Instant startOfDay, Instant endOfDay, ServiceType serviceType, Pageable pageable);

    /**
     * Finds appointments by date range, status, and service type.
     *
     * @param startOfDay the start of the day (inclusive)
     * @param endOfDay the end of the day (exclusive)
     * @param status the status filter
     * @param serviceType the service type filter
     * @param pageable pagination parameters
     * @return a page of matching entities
     */
    Page<AppointmentJpaEntity>
            findByStartAtGreaterThanEqualAndStartAtLessThanAndStatusAndServiceType(
                    Instant startOfDay,
                    Instant endOfDay,
                    AppointmentStatus status,
                    ServiceType serviceType,
                    Pageable pageable);
}
