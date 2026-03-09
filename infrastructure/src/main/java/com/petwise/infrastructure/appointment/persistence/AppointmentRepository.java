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
    @Query(
            """
            SELECT a FROM AppointmentJpaEntity a
            WHERE LOWER(CAST(a.serviceType AS string)) LIKE LOWER(CONCAT('%', :terms, '%'))
               OR LOWER(CAST(a.status AS string)) LIKE LOWER(CONCAT('%', :terms, '%'))
            """)
    Page<AppointmentJpaEntity> findBySearchTerms(@Param("terms") String terms, Pageable pageable);

    Page<AppointmentJpaEntity> findByStatus(AppointmentStatus status, Pageable pageable);

    Page<AppointmentJpaEntity> findByStartAtGreaterThanEqualAndStartAtLessThan(
            Instant startOfDay, Instant endOfDay, Pageable pageable);

    Page<AppointmentJpaEntity> findByStartAtGreaterThanEqualAndStartAtLessThanAndStatus(
            Instant startOfDay, Instant endOfDay, AppointmentStatus status, Pageable pageable);

    Page<AppointmentJpaEntity> findByStartAtGreaterThanEqualAndStartAtLessThanAndServiceType(
            Instant startOfDay, Instant endOfDay, ServiceType serviceType, Pageable pageable);

    Page<AppointmentJpaEntity>
            findByStartAtGreaterThanEqualAndStartAtLessThanAndStatusAndServiceType(
                    Instant startOfDay,
                    Instant endOfDay,
                    AppointmentStatus status,
                    ServiceType serviceType,
                    Pageable pageable);
}
