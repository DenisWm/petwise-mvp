package com.petwise.application.appointment.retrieve.list;

import com.petwise.domain.appointment.AppointmentGateway;
import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link ListAppointmentsUseCase}.
 *
 * <p>Delegates pagination and optional full-text search to the {@link AppointmentGateway} and maps
 * each result to a {@link ListAppointmentsOutput} DTO.
 */
@SuppressWarnings("PMD.LongVariable")
public final class DefaultListAppointmentsUseCase extends ListAppointmentsUseCase {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultListAppointmentsUseCase.class);
    private final AppointmentGateway appointmentGateway;

    public DefaultListAppointmentsUseCase(final AppointmentGateway anAppointmentGateway) {
        super();
        this.appointmentGateway =
                Objects.requireNonNull(anAppointmentGateway, "AppointmentGateway cannot be null");
    }

    @Override
    public Pagination<ListAppointmentsOutput> execute(final SearchQuery query) {
        Objects.requireNonNull(query, "SearchQuery cannot be null");
        if (LOG.isDebugEnabled()) {
            LOG.debug(
                    "Listing appointments: page={}, perPage={}, terms={}",
                    query.page(),
                    query.perPage(),
                    query.terms());
        }

        return this.appointmentGateway.findAll(query).map(ListAppointmentsOutput::from);
    }
}
