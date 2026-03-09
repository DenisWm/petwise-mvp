package com.petwise.application.appointment.changestatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.petwise.domain.appointment.Appointment;
import com.petwise.domain.appointment.AppointmentGateway;
import com.petwise.domain.appointment.AppointmentID;
import com.petwise.domain.appointment.AppointmentStatus;
import com.petwise.domain.appointment.ServiceType;
import com.petwise.domain.exceptions.DomainException;
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

/** Unit tests for {@link DefaultChangeAppointmentStatusUseCase}. */
@ExtendWith(MockitoExtension.class)
@DisplayName("ChangeAppointmentStatusUseCase Tests")
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.LongVariable"
})
class ChangeAppointmentStatusUseCaseTest {
    ChangeAppointmentStatusUseCaseTest() {}

    @Mock private AppointmentGateway appointmentGateway;
    @InjectMocks private DefaultChangeAppointmentStatusUseCase useCase;
    private AppointmentID appointmentId;
    private Appointment existingAppointment;

    @BeforeEach
    void setUp() {
        appointmentId = AppointmentID.unique();
        existingAppointment =
                Appointment.with(
                        appointmentId,
                        PetID.unique(),
                        ServiceType.DAYCARE,
                        AppointmentStatus.PENDING,
                        Instant.parse("2025-11-28T08:00:00Z"),
                        Instant.parse("2025-11-28T18:00:00Z"),
                        null,
                        Instant.now().minusSeconds(3600),
                        Instant.now().minusSeconds(3600));
    }

    @Test
    @DisplayName("Should change status from PENDING to ACTIVE successfully")
    void givenPendingAppointment_whenChangeToActive_thenShouldSucceed() {
        final var command =
                ChangeAppointmentStatusCommand.with(
                        appointmentId.getValue(), AppointmentStatus.ACTIVE);
        when(appointmentGateway.findById(appointmentId))
                .thenReturn(Optional.of(existingAppointment));
        when(appointmentGateway.save(any(Appointment.class))).thenAnswer(inv -> inv.getArgument(0));
        final var output = useCase.execute(command);
        assertNotNull(output);
        assertEquals(appointmentId.getValue(), output.id());
        assertEquals(AppointmentStatus.ACTIVE.name(), output.status());
        verify(appointmentGateway).findById(appointmentId);
        verify(appointmentGateway).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Should change status from PENDING to CANCELED successfully")
    void givenPendingAppointment_whenChangeToCanceled_thenShouldSucceed() {
        final var command =
                ChangeAppointmentStatusCommand.with(
                        appointmentId.getValue(), AppointmentStatus.CANCELED);
        when(appointmentGateway.findById(appointmentId))
                .thenReturn(Optional.of(existingAppointment));
        when(appointmentGateway.save(any(Appointment.class))).thenAnswer(inv -> inv.getArgument(0));
        final var output = useCase.execute(command);
        assertNotNull(output);
        assertEquals(AppointmentStatus.CANCELED.name(), output.status());
    }

    @Test
    @DisplayName("Should throw DomainException on invalid status transition")
    void givenPendingAppointment_whenChangeToCompleted_thenShouldThrowDomainException() {
        // Given — PENDING cannot go to COMPLETED directly
        final var command =
                ChangeAppointmentStatusCommand.with(
                        appointmentId.getValue(), AppointmentStatus.COMPLETED);
        when(appointmentGateway.findById(appointmentId))
                .thenReturn(Optional.of(existingAppointment));
        assertThrows(DomainException.class, () -> useCase.execute(command));
        verify(appointmentGateway, never()).save(any());
    }

    @Test
    @DisplayName("Should throw NotFoundException when appointment does not exist")
    void givenNonExistingAppointment_whenExecute_thenShouldThrowNotFoundException() {
        final var command =
                ChangeAppointmentStatusCommand.with(
                        appointmentId.getValue(), AppointmentStatus.ACTIVE);
        when(appointmentGateway.findById(appointmentId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> useCase.execute(command));
        verify(appointmentGateway, never()).save(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when command is null")
    void givenNullCommand_whenExecute_thenShouldThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verify(appointmentGateway, never()).save(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when gateway is null")
    void givenNullGateway_whenConstruct_thenShouldThrowNullPointerException() {
        assertThrows(
                NullPointerException.class, () -> new DefaultChangeAppointmentStatusUseCase(null));
    }
}
