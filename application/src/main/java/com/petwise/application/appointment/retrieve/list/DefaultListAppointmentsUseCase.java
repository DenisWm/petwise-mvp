package com.petwise.application.appointment.retrieve.list;

import com.petwise.domain.appointment.AppointmentGateway;
import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import java.util.Objects;

/**
 * Default implementation of {@link ListAppointmentsUseCase}.
 *
 * <p>Delegates pagination and optional full-text search to the {@link AppointmentGateway} and maps
 * each result to a {@link ListAppointmentsOutput} DTO.
 */
@SuppressWarnings("PMD.LongVariable")
public final class DefaultListAppointmentsUseCase extends ListAppointmentsUseCase {

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

        return this.appointmentGateway.findAll(query).map(ListAppointmentsOutput::from);
    }
}
