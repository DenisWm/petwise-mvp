package com.petwise.infrastructure.pet.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for {@link PetJpaEntity}.
 *
 * <p>Provides built-in CRUD and pagination methods via {@link JpaRepository}, plus a custom
 * full-text search query.
 */
public interface PetRepository extends JpaRepository<PetJpaEntity, String> {

    /**
     * Finds pets whose name or species contains the given terms (case-insensitive).
     *
     * @param terms the search string; must not be {@code null}
     * @param pageable pagination parameters
     * @return a page of matching entities
     */
    @Query(
            """
            SELECT p FROM PetJpaEntity p
            WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :terms, '%'))
               OR LOWER(p.species) LIKE LOWER(CONCAT('%', :terms, '%'))
            """)
    Page<PetJpaEntity> findBySearchTerms(@Param("terms") String terms, Pageable pageable);
}
