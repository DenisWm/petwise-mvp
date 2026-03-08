package com.petwise.application.appointment.retrieve.list;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.petwise.domain.appointment.Appointment;
import com.petwise.domain.appointment.AppointmentGateway;
import com.petwise.domain.appointment.AppointmentID;
import com.petwise.domain.appointment.AppointmentStatus;
import com.petwise.domain.appointment.ServiceType;
import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import com.petwise.domain.pet.PetID;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Unit tests for {@link DefaultListAppointmentsUseCase}. */
@ExtendWith(MockitoExtension.class)
@DisplayName("ListAppointmentsUseCase Tests")
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.LongVariable"
})
class ListAppointmentsUseCaseTest {

    /** Default constructor. */
    ListAppointmentsUseCaseTest() {}

    /** The mocked appointment gateway. */
    @Mock private AppointmentGateway appointmentGateway;

    /** The use case under test. */
    @InjectMocks private DefaultListAppointmentsUseCase useCase;

    @Test
    @DisplayName("Should return paginated list of appointments")
    void givenExistingAppointments_whenExecute_thenShouldReturnPaginatedList() {
        // Given
        final var query = new SearchQuery(0, 10, "", "startAt", "asc");
        final var appointment =
                Appointment.with(
                        AppointmentID.unique(),
                        PetID.unique(),
                        ServiceType.DAYCARE,
                        AppointmentStatus.PENDING,
                        Instant.parse("2025-11-28T08:00:00Z"),
                        Instant.parse("2025-11-28T18:00:00Z"),
                        null,
                        Instant.now(),
                        Instant.now());

        final var pagination = new Pagination<>(0, 10, 1, List.of(appointment));
        when(appointmentGateway.findAll(query)).thenReturn(pagination);

        // When
        final var result = useCase.execute(query);

        // Then
        assertNotNull(result);
        assertEquals(1, result.total());
        assertEquals(ServiceType.DAYCARE, result.items().get(0).serviceType());

        verify(appointmentGateway).findAll(query);
    }

    @Test
    @DisplayName("Should throw NullPointerException when query is null")
    void givenNullQuery_whenExecute_thenShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verify(appointmentGateway, never()).findAll(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when gateway is null")
    void givenNullGateway_whenConstruct_thenShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DefaultListAppointmentsUseCase(null));
    }
}
