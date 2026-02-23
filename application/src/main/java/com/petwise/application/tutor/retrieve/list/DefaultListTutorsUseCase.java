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
public final class DefaultListTutorsUseCase extends ListTutorsUseCase {

    /** The gateway used to query tutors. */
    private final TutorGateway tutorGateway;

    /**
     * Constructs the use case with the required gateway.
     *
     * @param aTutorGateway the tutor persistence gateway; must not be {@code null}
     */
    public DefaultListTutorsUseCase(final TutorGateway aTutorGateway) {
        super();
        this.tutorGateway = Objects.requireNonNull(aTutorGateway, "TutorGateway cannot be null");
    }

    /**
     * {@inheritDoc}
     *
     * <p>Subclasses may override to customise pagination behaviour.
     */
    @Override
    public Pagination<ListTutorsOutput> execute(final SearchQuery query) {
        Objects.requireNonNull(query, "SearchQuery cannot be null");

        return this.tutorGateway.findAll(query).map(ListTutorsOutput::from);
    }
}
