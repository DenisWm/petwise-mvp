package com.petwise.domain.tutor;

import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import java.util.List;
import java.util.Optional;

/**
 * Gateway interface for Tutor persistence operations. To be implemented by the infrastructure
 * layer.
 */
public interface TutorGateway {

    /**
     * Saves a tutor (create or update).
     *
     * @param tutor the tutor to save
     * @return the saved tutor
     */
    Tutor save(Tutor tutor);

    /**
     * Finds a tutor by ID.
     *
     * @param id the tutor ID
     * @return an Optional containing the tutor if found, empty otherwise
     */
    Optional<Tutor> findById(TutorID id);

    /**
     * Finds all tutors.
     *
     * @return a list of all tutors
     */
    List<Tutor> findAll();

    /**
     * Finds all tutors with pagination and search.
     *
     * @param query the search query with pagination parameters
     * @return a paginated result of tutors
     */
    Pagination<Tutor> findAll(SearchQuery query);

    /**
     * Deletes a tutor by ID.
     *
     * @param id the tutor ID to delete
     */
    void deleteById(TutorID id);
}
