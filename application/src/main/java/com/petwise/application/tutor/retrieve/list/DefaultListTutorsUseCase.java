package com.petwise.application.tutor.retrieve.list;

import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import com.petwise.domain.tutor.TutorGateway;
import java.util.Objects;

/**
 * Default implementation of {@link ListTutorsUseCase}.
 *
 * <p>Delegates pagination and optional full-text search to the {@link TutorGateway} and maps each
 * result to a {@link ListTutorsOutput} DTO.
 */
public class DefaultListTutorsUseCase extends ListTutorsUseCase {

    private final TutorGateway tutorGateway;

    public DefaultListTutorsUseCase(final TutorGateway tutorGateway) {
        this.tutorGateway = Objects.requireNonNull(tutorGateway, "TutorGateway cannot be null");
    }

    @Override
    public Pagination<ListTutorsOutput> execute(final SearchQuery query) {
        Objects.requireNonNull(query, "SearchQuery cannot be null");

        return this.tutorGateway.findAll(query).map(ListTutorsOutput::from);
    }
}
