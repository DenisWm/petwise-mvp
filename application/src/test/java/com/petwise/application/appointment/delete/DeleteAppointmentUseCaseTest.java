package com.petwise.application.appointment.delete;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

/** Unit tests for {@link DefaultDeleteAppointmentUseCase}. */
@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteAppointmentUseCase Tests")
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate"
})
class DeleteAppointmentUseCaseTest {
    DeleteAppointmentUseCaseTest() {}

    @Mock private AppointmentGateway appointmentGateway;
    @InjectMocks private DefaultDeleteAppointmentUseCase useCase;
    private AppointmentID appointmentId;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        appointmentId = AppointmentID.unique();
        appointment =
                Appointment.with(
                        appointmentId,
                        PetID.unique(),
                        ServiceType.DAYCARE,
                        AppointmentStatus.PENDING,
                        Instant.parse("2025-11-28T08:00:00Z"),
                        Instant.parse("2025-11-28T18:00:00Z"),
                        null,
                        Instant.now().minusSeconds(3600),
                        Instant.now().minusSeconds(1800));
    }

    @Test
    @DisplayName("Should delete appointment successfully")
    void shouldDeleteAppointmentSuccessfully() {
        final var appointmentIdValue = appointmentId.getValue();
        when(appointmentGateway.findById(appointmentId)).thenReturn(Optional.of(appointment));
        doNothing().when(appointmentGateway).deleteById(appointmentId);
        assertDoesNotThrow(() -> useCase.execute(appointmentIdValue));
        verify(appointmentGateway).findById(appointmentId);
        verify(appointmentGateway).deleteById(appointmentId);
    }

    @Test
    @DisplayName("Should throw NotFoundException when appointment does not exist")
    void shouldThrowNotFoundExceptionWhenAppointmentDoesNotExist() {
        final var appointmentIdValue = appointmentId.getValue();
        when(appointmentGateway.findById(appointmentId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> useCase.execute(appointmentIdValue));

        verify(appointmentGateway).findById(appointmentId);
        verify(appointmentGateway, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when ID is null")
    void shouldThrowNullPointerExceptionWhenIdIsNull() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verify(appointmentGateway, never()).findById(any());
        verify(appointmentGateway, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when gateway is null")
    void shouldThrowNullPointerExceptionWhenGatewayIsNull() {
        assertThrows(NullPointerException.class, () -> new DefaultDeleteAppointmentUseCase(null));
    }
}
