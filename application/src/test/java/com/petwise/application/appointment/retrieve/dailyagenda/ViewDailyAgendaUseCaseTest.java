package com.petwise.application.appointment.retrieve.dailyagenda;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.petwise.domain.appointment.Appointment;
import com.petwise.domain.appointment.AppointmentGateway;
import com.petwise.domain.appointment.AppointmentID;
import com.petwise.domain.appointment.AppointmentSearchQuery;
import com.petwise.domain.appointment.AppointmentStatus;
import com.petwise.domain.appointment.ServiceType;
import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pet.PetID;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Unit tests for {@link DefaultViewDailyAgendaUseCase}. */
@ExtendWith(MockitoExtension.class)
@DisplayName("ViewDailyAgendaUseCase Tests")
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.LongVariable"
})
class ViewDailyAgendaUseCaseTest {
    ViewDailyAgendaUseCaseTest() {}

    @Mock private AppointmentGateway appointmentGateway;
    @InjectMocks private DefaultViewDailyAgendaUseCase useCase;

    @Test
    @DisplayName("Should return paginated agenda for a given date")
    void givenValidDateCommand_whenExecute_thenShouldReturnPaginatedAgenda() {
        final var date = LocalDate.of(2026, 2, 26);
        final var command = ViewDailyAgendaCommand.with(date, null, null, 0, 20, "startAt", "asc");

        final var appointment =
                Appointment.with(
                        AppointmentID.unique(),
                        PetID.unique(),
                        ServiceType.DAYCARE,
                        AppointmentStatus.ACTIVE,
                        Instant.parse("2026-02-26T08:00:00Z"),
                        Instant.parse("2026-02-26T18:00:00Z"),
                        "Daycare session",
                        Instant.now(),
                        Instant.now());

        final var pagination = new Pagination<>(0, 20, 1, List.of(appointment));
        when(appointmentGateway.findDailyAgenda(any(AppointmentSearchQuery.class)))
                .thenReturn(pagination);
        final var result = useCase.execute(command);
        assertNotNull(result);
        assertEquals(1, result.total());
        assertEquals(1, result.items().size());
        assertEquals(ServiceType.DAYCARE, result.items().getFirst().serviceType());
        assertEquals(AppointmentStatus.ACTIVE, result.items().getFirst().status());
        assertEquals("Daycare session", result.items().getFirst().notes());

        verify(appointmentGateway).findDailyAgenda(any(AppointmentSearchQuery.class));
    }

    @Test
    @DisplayName("Should pass status and serviceType filters to gateway")
    void givenCommandWithFilters_whenExecute_thenShouldPassFiltersToGateway() {
        final var date = LocalDate.of(2026, 2, 26);
        final var command =
                ViewDailyAgendaCommand.with(
                        date,
                        AppointmentStatus.PENDING,
                        ServiceType.HOTEL,
                        0,
                        10,
                        "startAt",
                        "asc");

        final var pagination = new Pagination<Appointment>(0, 10, 0, List.of());
        when(appointmentGateway.findDailyAgenda(any(AppointmentSearchQuery.class)))
                .thenReturn(pagination);
        final var result = useCase.execute(command);
        assertNotNull(result);
        assertEquals(0, result.total());

        verify(appointmentGateway)
                .findDailyAgenda(
                        argThat(
                                q ->
                                        q.date().equals(date)
                                                && q.status() == AppointmentStatus.PENDING
                                                && q.serviceType() == ServiceType.HOTEL));
    }

    @Test
    @DisplayName("Should return empty page when no appointments for date")
    void givenNoAppointments_whenExecute_thenShouldReturnEmptyPage() {
        final var date = LocalDate.of(2026, 12, 25);
        final var command = ViewDailyAgendaCommand.with(date, null, null, 0, 20, "startAt", "asc");

        final var emptyPagination = new Pagination<Appointment>(0, 20, 0, List.of());
        when(appointmentGateway.findDailyAgenda(any(AppointmentSearchQuery.class)))
                .thenReturn(emptyPagination);
        final var result = useCase.execute(command);
        assertNotNull(result);
        assertEquals(0, result.total());
        assertTrue(result.items().isEmpty());
    }

    @Test
    @DisplayName("Should throw NullPointerException when command is null")
    void givenNullCommand_whenExecute_thenShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verify(appointmentGateway, never()).findDailyAgenda(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when gateway is null")
    void givenNullGateway_whenConstruct_thenShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DefaultViewDailyAgendaUseCase(null));
    }
}
