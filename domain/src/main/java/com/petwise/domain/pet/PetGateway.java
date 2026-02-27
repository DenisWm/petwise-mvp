package com.petwise.domain.pet;

import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import java.util.List;
import java.util.Optional;

/**
 * Gateway interface for Pet persistence operations. To be implemented by the infrastructure layer.
 */
@SuppressWarnings("PMD.ShortVariable")
public interface PetGateway {

    /**
     * Saves a pet (create or update).
     *
     * @param pet the pet to save
     * @return the saved pet
     */
    Pet save(Pet pet);

    /**
     * Finds a pet by ID.
     *
     * @param anId the pet ID
     * @return an Optional containing the pet if found, empty otherwise
     */
    Optional<Pet> findById(PetID anId);

    /**
     * Finds all pets.
     *
     * @return a list of all pets
     */
    List<Pet> findAll();

    /**
     * Finds all pets with pagination and search.
     *
     * @param query the search query with pagination parameters
     * @return a paginated result of pets
     */
    Pagination<Pet> findAll(SearchQuery query);

    /**
     * Deletes a pet by ID.
     *
     * @param anId the pet ID to delete
     */
    void deleteById(PetID anId);
}
