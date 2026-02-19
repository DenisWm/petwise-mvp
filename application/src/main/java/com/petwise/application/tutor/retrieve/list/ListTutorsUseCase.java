package com.petwise.application.tutor.retrieve.list;

import com.petwise.application.UseCase;
import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;

/** Abstract use case for listing all tutors with pagination and search. */
public abstract class ListTutorsUseCase
        extends UseCase<SearchQuery, Pagination<ListTutorsOutput>> {}
