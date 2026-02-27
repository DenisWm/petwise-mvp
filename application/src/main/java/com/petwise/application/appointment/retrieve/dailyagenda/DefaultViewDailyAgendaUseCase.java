package com.petwise.application.appointment.retrieve.dailyagenda;

import com.petwise.domain.appointment.AppointmentGateway;
import com.petwise.domain.appointment.AppointmentSearchQuery;
import com.petwise.domain.pagination.Pagination;
import java.util.Objects;

/**
 * Default implementation of {@link ViewDailyAgendaUseCase}.
 *
 * <p>Builds an {@link AppointmentSearchQuery} from the command and delegates to the {@link
 * AppointmentGateway} for date-filtered, paginated retrieval.
 */
@SuppressWarnings("PMD.LongVariable")
public final class DefaultViewDailyAgendaUseCase extends ViewDailyAgendaUseCase {

    /** The gateway used to query appointments. */
    private final AppointmentGateway appointmentGateway;

    /**
     * Constructs the use case with the required gateway.
     *
     * @param anAppointmentGateway the appointment persistence gateway; must not be {@code null}
     */
    public DefaultViewDailyAgendaUseCase(final AppointmentGateway anAppointmentGateway) {
        super();
        this.appointmentGateway =
                Objects.requireNonNull(anAppointmentGateway, "AppointmentGateway cannot be null");
    }

    /** {@inheritDoc} */
    @Override
    public Pagination<ViewDailyAgendaOutput> execute(final ViewDailyAgendaCommand aCommand) {
        Objects.requireNonNull(aCommand, "ViewDailyAgendaCommand cannot be null");

        final var query =
                new AppointmentSearchQuery(
                        aCommand.date(),
                        aCommand.status(),
                        aCommand.serviceType(),
                        aCommand.page(),
                        aCommand.perPage(),
                        aCommand.sort(),
                        aCommand.direction());

        return this.appointmentGateway.findDailyAgenda(query).map(ViewDailyAgendaOutput::from);
    }
}
