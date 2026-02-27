package com.petwise.application.appointment.retrieve.getbyid;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.petwise.domain.appointment.Appointment;
import com.petwise.domain.appointment.AppointmentGateway;
import com.petwise.domain.appointment.AppointmentID;
import com.petwise.domain.appointment.AppointmentStatus;
import com.petwise.domain.appointment.ServiceType;
import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.pet.PetID;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Unit tests for {@link DefaultGetAppointmentByIdUseCase}. */
@ExtendWith(MockitoExtension.class)
@DisplayName("GetAppointmentByIdUseCase Tests")
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.LongVariable"
})
class GetAppointmentByIdUseCaseTest {

    /** Default constructor. */
    GetAppointmentByIdUseCaseTest() {}

    /** The mocked appointment gateway. */
    @Mock private AppointmentGateway appointmentGateway;

    /** The use case under test. */
    @InjectMocks private DefaultGetAppointmentByIdUseCase useCase;

    /** The appointment ID used across tests. */
    private AppointmentID appointmentId;

    /** The appointment used across tests. */
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        appointmentId = AppointmentID.unique();
        appointment =
                Appointment.with(
                        appointmentId,
                        PetID.unique(),
                        ServiceType.HOTEL,
                        AppointmentStatus.PENDING,
                        Instant.parse("2025-11-28T08:00:00Z"),
                        Instant.parse("2025-11-28T18:00:00Z"),
                        "Some notes",
                        Instant.now().minusSeconds(3600),
                        Instant.now().minusSeconds(1800));
    }

    @Test
    @DisplayName("Should return appointment output when appointment exists")
    void shouldReturnAppointmentOutputWhenExists() {
        // Given
        when(appointmentGateway.findById(appointmentId)).thenReturn(Optional.of(appointment));

        // When
        final var output = useCase.execute(appointmentId.getValue());

        // Then
        assertNotNull(output);
        assertEquals(appointmentId.getValue(), output.id());
        assertEquals(ServiceType.HOTEL, output.serviceType());
        assertEquals(AppointmentStatus.PENDING, output.status());
        assertEquals("Some notes", output.notes());

        verify(appointmentGateway).findById(appointmentId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when appointment does not exist")
    void shouldThrowNotFoundExceptionWhenAppointmentDoesNotExist() {
        // Given
        when(appointmentGateway.findById(appointmentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotFoundException.class, () -> useCase.execute(appointmentId.getValue()));
        verify(appointmentGateway).findById(appointmentId);
    }

    @Test
    @DisplayName("Should throw NullPointerException when ID is null")
    void shouldThrowNullPointerExceptionWhenIdIsNull() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verify(appointmentGateway, never()).findById(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when gateway is null")
    void shouldThrowNullPointerExceptionWhenGatewayIsNull() {
        assertThrows(NullPointerException.class, () -> new DefaultGetAppointmentByIdUseCase(null));
    }
}
