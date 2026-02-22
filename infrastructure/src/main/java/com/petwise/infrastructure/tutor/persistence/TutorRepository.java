package com.petwise.infrastructure.tutor.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for {@link TutorJpaEntity}.
 *
 * <p>Provides built-in CRUD and pagination methods via {@link JpaRepository}, plus a custom
 * full-text search query.
 */
public interface TutorRepository extends JpaRepository<TutorJpaEntity, String> {

    /**
     * Finds tutors whose name or email contains the given terms (case-insensitive).
     *
     * @param terms the search string; must not be {@code null}
     * @param pageable pagination parameters
     * @return a page of matching entities
     */
    @Query(
            """
            SELECT t FROM TutorJpaEntity t
            WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :terms, '%'))
               OR LOWER(t.email) LIKE LOWER(CONCAT('%', :terms, '%'))
            """)
    Page<TutorJpaEntity> findBySearchTerms(@Param("terms") String terms, Pageable pageable);
}
