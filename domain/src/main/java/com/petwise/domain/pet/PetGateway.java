package com.petwise.domain.pet;

import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import java.util.List;
import java.util.Optional;

/** Gateway for Pet persistence operations. Implemented by the infrastructure layer. */
@SuppressWarnings("PMD.ShortVariable")
public interface PetGateway {
    Pet save(Pet pet);

    Optional<Pet> findById(PetID anId);

    List<Pet> findAll();

    Pagination<Pet> findAll(SearchQuery query);

    void deleteById(PetID anId);
}
