package com.petwise.application.pet.retrieve.list;

import com.petwise.application.UseCase;
import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;

/** Abstract use case for listing all pets with pagination and search. */
@SuppressWarnings("PMD.AbstractClassWithoutAnyMethod")
public abstract class ListPetsUseCase extends UseCase<SearchQuery, Pagination<ListPetsOutput>> {
    protected ListPetsUseCase() {}
}
