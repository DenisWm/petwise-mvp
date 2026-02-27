package com.petwise.application.appointment.retrieve.list;

import com.petwise.application.UseCase;
import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;

/** Abstract use case for listing all appointments with pagination and search. */
@SuppressWarnings("PMD.AbstractClassWithoutAnyMethod")
public abstract class ListAppointmentsUseCase
        extends UseCase<SearchQuery, Pagination<ListAppointmentsOutput>> {

    /** Protected constructor for subclasses. */
    protected ListAppointmentsUseCase() {}
}
