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

    /** SLF4J logger for this class. */
    private static final Logger LOG = LoggerFactory.getLogger(DefaultListAppointmentsUseCase.class);

    /** The gateway used to query appointments. */
    private final AppointmentGateway appointmentGateway;

    /**
     * Constructs the use case with the required gateway.
     *
     * @param anAppointmentGateway the appointment persistence gateway; must not be {@code null}
     */
    public DefaultListAppointmentsUseCase(final AppointmentGateway anAppointmentGateway) {
        super();
        this.appointmentGateway =
                Objects.requireNonNull(anAppointmentGateway, "AppointmentGateway cannot be null");
    }

    /**
     * {@inheritDoc}
     *
     * <p>Subclasses may override to customise pagination behaviour.
     */
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
