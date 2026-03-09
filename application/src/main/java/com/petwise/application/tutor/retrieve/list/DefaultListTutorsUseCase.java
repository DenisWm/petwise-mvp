package com.petwise.application.tutor.retrieve.list;

import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import com.petwise.domain.tutor.TutorGateway;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link ListTutorsUseCase}.
 *
 * <p>Delegates pagination and optional full-text search to the {@link TutorGateway} and maps each
 * result to a {@link ListTutorsOutput} DTO.
 */
public final class DefaultListTutorsUseCase extends ListTutorsUseCase {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultListTutorsUseCase.class);
    private final TutorGateway tutorGateway;

    public DefaultListTutorsUseCase(final TutorGateway aTutorGateway) {
        super();
        this.tutorGateway = Objects.requireNonNull(aTutorGateway, "TutorGateway cannot be null");
    }

    @Override
    public Pagination<ListTutorsOutput> execute(final SearchQuery query) {
        Objects.requireNonNull(query, "SearchQuery cannot be null");
        if (LOG.isDebugEnabled()) {
            LOG.debug(
                    "Listing tutors: page={}, perPage={}, terms={}",
                    query.page(),
                    query.perPage(),
                    query.terms());
        }

        return this.tutorGateway.findAll(query).map(ListTutorsOutput::from);
    }
}
