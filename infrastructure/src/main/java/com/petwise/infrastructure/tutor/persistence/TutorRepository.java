package com.petwise.infrastructure.tutor.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** Spring Data JPA repository for TutorJpaEntity. */
public interface TutorRepository extends JpaRepository<TutorJpaEntity, String> {

    @Query(
            """
            SELECT t FROM TutorJpaEntity t
            WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :terms, '%'))
               OR LOWER(t.email) LIKE LOWER(CONCAT('%', :terms, '%'))
            """)
    Page<TutorJpaEntity> findBySearchTerms(@Param("terms") String terms, Pageable pageable);
}
