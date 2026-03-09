package com.petwise.infrastructure.appointment.api;

import com.petwise.application.appointment.changestatus.ChangeAppointmentStatusCommand;
import com.petwise.application.appointment.changestatus.ChangeAppointmentStatusUseCase;
import com.petwise.application.appointment.create.CreateAppointmentCommand;
import com.petwise.application.appointment.create.CreateAppointmentUseCase;
import com.petwise.application.appointment.delete.DeleteAppointmentUseCase;
import com.petwise.application.appointment.retrieve.dailyagenda.ViewDailyAgendaCommand;
import com.petwise.application.appointment.retrieve.dailyagenda.ViewDailyAgendaUseCase;
import com.petwise.application.appointment.retrieve.getbyid.GetAppointmentByIdUseCase;
import com.petwise.application.appointment.retrieve.list.ListAppointmentsUseCase;
import com.petwise.domain.appointment.AppointmentStatus;
import com.petwise.domain.appointment.ServiceType;
import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import com.petwise.infrastructure.appointment.models.AppointmentResponse;
import com.petwise.infrastructure.appointment.models.ChangeAppointmentStatusRequest;
import com.petwise.infrastructure.appointment.models.CreateAppointmentRequest;
import com.petwise.infrastructure.appointment.presenters.AppointmentApiPresenter;
import java.net.URI;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that implements the {@link AppointmentAPI} contract.
 *
 * <p>Delegates every HTTP operation to the appropriate use case and converts the result via {@link
 * AppointmentApiPresenter}. All Spring MVC and OpenAPI annotations are declared on the interface.
 */
@RestController
@SuppressWarnings({"PMD.LongVariable", "PMD.ShortVariable"})
public class AppointmentController implements AppointmentAPI {
    private static final Logger LOG = LoggerFactory.getLogger(AppointmentController.class);
    private final CreateAppointmentUseCase createAppointmentUseCase;
    private final GetAppointmentByIdUseCase getAppointmentByIdUseCase;
    private final ListAppointmentsUseCase listAppointmentsUseCase;

    /** Use case for viewing the daily agenda. */
    private final ViewDailyAgendaUseCase viewDailyAgendaUseCase;

    /** Use case for changing appointment status. */
    private final ChangeAppointmentStatusUseCase changeAppointmentStatusUseCase;

    private final DeleteAppointmentUseCase deleteAppointmentUseCase;

    /**
     * Constructs the controller with all required use cases.
     *
     * @param aCreateUseCase use case for creating appointments
     * @param aGetByIdUseCase use case for retrieving an appointment by ID
     * @param aListUseCase use case for listing appointments
     * @param aViewAgendaUseCase use case for viewing the daily agenda
     * @param aChangeStatusUseCase use case for changing appointment status
     * @param aDeleteUseCase use case for deleting an appointment
     */
    public AppointmentController(
            final CreateAppointmentUseCase aCreateUseCase,
            final GetAppointmentByIdUseCase aGetByIdUseCase,
            final ListAppointmentsUseCase aListUseCase,
            final ViewDailyAgendaUseCase aViewAgendaUseCase,
            final ChangeAppointmentStatusUseCase aChangeStatusUseCase,
            final DeleteAppointmentUseCase aDeleteUseCase) {
        this.createAppointmentUseCase = aCreateUseCase;
        this.getAppointmentByIdUseCase = aGetByIdUseCase;
        this.listAppointmentsUseCase = aListUseCase;
        this.viewDailyAgendaUseCase = aViewAgendaUseCase;
        this.changeAppointmentStatusUseCase = aChangeStatusUseCase;
        this.deleteAppointmentUseCase = aDeleteUseCase;
    }

    /** {@inheritDoc} */
    @Override
    public ResponseEntity<Void> createAppointment(final CreateAppointmentRequest request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("POST /appointments");
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace("Create appointment request body: {}", request);
        }
        final var command =
                CreateAppointmentCommand.with(
                        request.petId(),
                        request.serviceType(),
                        request.startAt(),
                        request.endAt(),
                        request.notes());
        final var output = this.createAppointmentUseCase.execute(command);
        return ResponseEntity.created(URI.create("/appointments/" + output.id())).build();
    }

    /** {@inheritDoc} */
    @Override
    public AppointmentResponse getAppointmentById(final String appointmentId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("GET /appointments/{}", appointmentId);
        }
        return AppointmentApiPresenter.present(
                this.getAppointmentByIdUseCase.execute(appointmentId));
    }

    /** {@inheritDoc} */
    @Override
    public Pagination<AppointmentResponse> listAppointments(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("GET /appointments — page={}, perPage={}, search={}", page, perPage, search);
        }
        final var query = new SearchQuery(page, perPage, search, sort, direction);
        return this.listAppointmentsUseCase.execute(query).map(AppointmentApiPresenter::present);
    }

    /** {@inheritDoc} */
    @Override
    public Pagination<AppointmentResponse> viewDailyAgenda(
            final LocalDate date,
            final AppointmentStatus status,
            final ServiceType serviceType,
            final int page,
            final int perPage,
            final String sort,
            final String direction) {
        LOG.debug(
                "GET /appointments/daily-agenda — date={}, status={}, serviceType={}",
                date,
                status,
                serviceType);
        final var command =
                ViewDailyAgendaCommand.with(
                        date, status, serviceType, page, perPage, sort, direction);
        return this.viewDailyAgendaUseCase.execute(command).map(AppointmentApiPresenter::present);
    }

    /** {@inheritDoc} */
    @Override
    public ResponseEntity<Void> changeAppointmentStatus(
            final String appointmentId, final ChangeAppointmentStatusRequest request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(
                    "PATCH /appointments/{}/status — targetStatus={}",
                    appointmentId,
                    request.status());
        }
        final var command = ChangeAppointmentStatusCommand.with(appointmentId, request.status());
        final var output = this.changeAppointmentStatusUseCase.execute(command);
        return ResponseEntity.ok().location(URI.create("/appointments/" + output.id())).build();
    }

    /** {@inheritDoc} */
    @Override
    public void deleteAppointment(final String appointmentId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("DELETE /appointments/{}", appointmentId);
        }
        this.deleteAppointmentUseCase.execute(appointmentId);
    }
}
