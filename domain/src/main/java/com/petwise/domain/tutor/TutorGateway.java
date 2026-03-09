package com.petwise.domain.tutor;

import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import java.util.List;
import java.util.Optional;

/** Gateway for Tutor persistence operations. Implemented by the infrastructure layer. */
@SuppressWarnings("PMD.ShortVariable")
public interface TutorGateway {
    Tutor save(Tutor tutor);

    Optional<Tutor> findById(TutorID anId);

    List<Tutor> findAll();

    Pagination<Tutor> findAll(SearchQuery query);

    void deleteById(TutorID anId);
}
