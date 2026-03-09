package com.petwise.application.appointment.retrieve.dailyagenda;

import com.petwise.domain.appointment.AppointmentGateway;
import com.petwise.domain.appointment.AppointmentSearchQuery;
import com.petwise.domain.pagination.Pagination;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link ViewDailyAgendaUseCase}.
 *
 * <p>Builds an {@link AppointmentSearchQuery} from the command and delegates to the {@link
 * AppointmentGateway} for date-filtered, paginated retrieval.
 */
@SuppressWarnings("PMD.LongVariable")
public final class DefaultViewDailyAgendaUseCase extends ViewDailyAgendaUseCase {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultViewDailyAgendaUseCase.class);
    private final AppointmentGateway appointmentGateway;

    public DefaultViewDailyAgendaUseCase(final AppointmentGateway anAppointmentGateway) {
        super();
        this.appointmentGateway =
                Objects.requireNonNull(anAppointmentGateway, "AppointmentGateway cannot be null");
    }

    /** {@inheritDoc} */
    @Override
    public Pagination<ViewDailyAgendaOutput> execute(final ViewDailyAgendaCommand aCommand) {
        Objects.requireNonNull(aCommand, "ViewDailyAgendaCommand cannot be null");
        if (LOG.isDebugEnabled()) {
            LOG.debug(
                    "Viewing daily agenda: date={}, status={}, serviceType={}",
                    aCommand.date(),
                    aCommand.status(),
                    aCommand.serviceType());
        }

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
