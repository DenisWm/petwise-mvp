package com.petwise.infrastructure.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petwise.ControllerTest;
import com.petwise.application.appointment.changestatus.ChangeAppointmentStatusOutput;
import com.petwise.application.appointment.changestatus.ChangeAppointmentStatusUseCase;
import com.petwise.application.appointment.create.CreateAppointmentOutput;
import com.petwise.application.appointment.create.CreateAppointmentUseCase;
import com.petwise.application.appointment.delete.DeleteAppointmentUseCase;
import com.petwise.application.appointment.retrieve.dailyagenda.ViewDailyAgendaOutput;
import com.petwise.application.appointment.retrieve.dailyagenda.ViewDailyAgendaUseCase;
import com.petwise.application.appointment.retrieve.getbyid.AppointmentOutput;
import com.petwise.application.appointment.retrieve.getbyid.GetAppointmentByIdUseCase;
import com.petwise.application.appointment.retrieve.list.ListAppointmentsOutput;
import com.petwise.application.appointment.retrieve.list.ListAppointmentsUseCase;
import com.petwise.domain.appointment.AppointmentStatus;
import com.petwise.domain.appointment.ServiceType;
import com.petwise.domain.pagination.Pagination;
import com.petwise.infrastructure.appointment.api.AppointmentController;
import com.petwise.infrastructure.appointment.models.ChangeAppointmentStatusRequest;
import com.petwise.infrastructure.appointment.models.CreateAppointmentRequest;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/** Integration tests for the Appointment REST API. */
@ControllerTest(controllers = AppointmentController.class)
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.TooManyFields",
    "PMD.AvoidDuplicateLiterals",
    "PMD.LongVariable",
    "PMD.TooManyStaticImports",
    "PMD.ExcessiveImports"
})
class AppointmentAPITest {
    AppointmentAPITest() {}

    /** MockMvc for performing HTTP requests. */
    @Autowired private MockMvc mockMvc;

    /** Object mapper for JSON serialization. */
    @Autowired private ObjectMapper objectMapper;

    /** Mocked create appointment use case. */
    @MockitoBean private CreateAppointmentUseCase createAppointmentUseCase;

    /** Mocked get appointment by id use case. */
    @MockitoBean private GetAppointmentByIdUseCase getAppointmentByIdUseCase;

    /** Mocked list appointments use case. */
    @MockitoBean private ListAppointmentsUseCase listAppointmentsUseCase;

    /** Mocked view daily agenda use case. */
    @MockitoBean private ViewDailyAgendaUseCase viewDailyAgendaUseCase;

    /** Mocked change appointment status use case. */
    @MockitoBean private ChangeAppointmentStatusUseCase changeAppointmentStatusUseCase;

    /** Mocked delete appointment use case. */
    @MockitoBean private DeleteAppointmentUseCase deleteAppointmentUseCase;

    @Test
    void givenValidRequest_whenCreateAppointment_thenShouldReturnCreatedWithLocation()
            throws Exception {
        final var expectedId = "appointment-123";
        final var request =
                new CreateAppointmentRequest(
                        "pet-id",
                        ServiceType.DAYCARE,
                        Instant.parse("2025-11-28T08:00:00Z"),
                        Instant.parse("2025-11-28T18:00:00Z"),
                        "Some notes");

        when(createAppointmentUseCase.execute(any()))
                .thenReturn(new CreateAppointmentOutput(expectedId));
        mockMvc.perform(
                        post("/appointments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/appointments/" + expectedId));

        verify(createAppointmentUseCase, times(1))
                .execute(argThat(cmd -> ServiceType.DAYCARE.equals(cmd.serviceType())));
    }

    @Test
    void givenValidId_whenGetAppointmentById_thenShouldReturnAppointment() throws Exception {
        final var expectedId = "appointment-123";
        final var startAt = Instant.parse("2025-11-28T08:00:00Z");
        final var endAt = Instant.parse("2025-11-28T18:00:00Z");

        final var output =
                new AppointmentOutput(
                        expectedId,
                        "pet-id",
                        ServiceType.DAYCARE,
                        AppointmentStatus.PENDING,
                        startAt,
                        endAt,
                        "Some notes",
                        Instant.now(),
                        Instant.now());

        when(getAppointmentByIdUseCase.execute(expectedId)).thenReturn(output);
        mockMvc.perform(get("/appointments/{id}", expectedId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.service_type", equalTo("DAYCARE")))
                .andExpect(jsonPath("$.status", equalTo("PENDING")));

        verify(getAppointmentByIdUseCase, times(1)).execute(expectedId);
    }

    @Test
    void givenValidParams_whenListAppointments_thenShouldReturnPaginatedList() throws Exception {
        final var appointment =
                new ListAppointmentsOutput(
                        "appointment-123",
                        "pet-id",
                        ServiceType.HOTEL,
                        AppointmentStatus.PENDING,
                        Instant.parse("2025-11-28T08:00:00Z"),
                        Instant.parse("2025-11-28T18:00:00Z"),
                        Instant.now(),
                        Instant.now());

        final var expectedPagination = new Pagination<>(0, 10, 1L, List.of(appointment));
        when(listAppointmentsUseCase.execute(any())).thenReturn(expectedPagination);
        mockMvc.perform(get("/appointments").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].service_type", equalTo("HOTEL")));

        verify(listAppointmentsUseCase, times(1)).execute(any());
    }

    @Test
    void givenValidDate_whenViewDailyAgenda_thenShouldReturnPaginatedList() throws Exception {
        final var output =
                new ViewDailyAgendaOutput(
                        "appointment-456",
                        "pet-id",
                        ServiceType.DAYCARE,
                        AppointmentStatus.ACTIVE,
                        Instant.parse("2026-02-26T08:00:00Z"),
                        Instant.parse("2026-02-26T18:00:00Z"),
                        "Daycare",
                        Instant.now(),
                        Instant.now());

        final var expectedPagination = new Pagination<>(0, 20, 1L, List.of(output));
        when(viewDailyAgendaUseCase.execute(any())).thenReturn(expectedPagination);
        mockMvc.perform(
                        get("/appointments/agenda")
                                .param("date", "2026-02-26")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].service_type", equalTo("DAYCARE")))
                .andExpect(jsonPath("$.items[0].status", equalTo("ACTIVE")))
                .andExpect(jsonPath("$.items[0].notes", equalTo("Daycare")));

        verify(viewDailyAgendaUseCase, times(1)).execute(any());
    }

    @Test
    void givenDateWithFilters_whenViewDailyAgenda_thenShouldPassFilters() throws Exception {
        final var expectedPagination = new Pagination<ViewDailyAgendaOutput>(0, 20, 0L, List.of());
        when(viewDailyAgendaUseCase.execute(any())).thenReturn(expectedPagination);
        mockMvc.perform(
                        get("/appointments/agenda")
                                .param("date", "2026-02-26")
                                .param("status", "PENDING")
                                .param("serviceType", "HOTEL")
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", equalTo(0)))
                .andExpect(jsonPath("$.items", hasSize(0)));

        verify(viewDailyAgendaUseCase, times(1))
                .execute(
                        argThat(
                                cmd ->
                                        cmd.status() == AppointmentStatus.PENDING
                                                && cmd.serviceType() == ServiceType.HOTEL));
    }

    @Test
    void givenValidRequest_whenChangeStatus_thenShouldReturnOkWithLocation() throws Exception {
        final var expectedId = "appointment-123";
        final var request = new ChangeAppointmentStatusRequest(AppointmentStatus.ACTIVE);

        when(changeAppointmentStatusUseCase.execute(any()))
                .thenReturn(new ChangeAppointmentStatusOutput(expectedId, "ACTIVE"));
        mockMvc.perform(
                        patch("/appointments/{id}/status", expectedId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(header().string("Location", "/appointments/" + expectedId));

        verify(changeAppointmentStatusUseCase, times(1))
                .execute(
                        argThat(
                                cmd ->
                                        expectedId.equals(cmd.id())
                                                && AppointmentStatus.ACTIVE.equals(cmd.status())));
    }

    @Test
    void givenValidId_whenDeleteAppointment_thenShouldReturnNoContent() throws Exception {
        final var expectedId = "appointment-123";
        doNothing().when(deleteAppointmentUseCase).execute(expectedId);
        mockMvc.perform(delete("/appointments/{id}", expectedId)).andExpect(status().isNoContent());

        verify(deleteAppointmentUseCase, times(1)).execute(expectedId);
    }
}
