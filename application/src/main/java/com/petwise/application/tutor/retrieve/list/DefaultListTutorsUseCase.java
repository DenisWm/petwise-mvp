package com.petwise.application.tutor.retrieve.list;

import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import com.petwise.domain.tutor.TutorGateway;
import java.util.Objects;

/**
 * Default implementation of ListTutorsUseCase. Retrieves tutors with pagination and search support.
 */
public class DefaultListTutorsUseCase extends ListTutorsUseCase {

    private final TutorGateway tutorGateway;

    public DefaultListTutorsUseCase(final TutorGateway tutorGateway) {
        this.tutorGateway = Objects.requireNonNull(tutorGateway, "TutorGateway cannot be null");
    }

    @Override
    public Pagination<ListTutorsOutput> execute(final SearchQuery query) {
        Objects.requireNonNull(query, "SearchQuery cannot be null");

        // 1. Retrieve paginated tutors from gateway
        final var pagination = this.tutorGateway.findAll(query);

        // 2. Map Tutor entities to ListTutorsOutput DTOs
        return pagination.map(ListTutorsOutput::from);
    }
}
