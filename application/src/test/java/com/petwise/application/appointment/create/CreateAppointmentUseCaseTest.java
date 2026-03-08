package com.petwise.application.appointment.create;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import com.petwise.application.UseCaseTest;
import com.petwise.domain.appointment.Appointment;
import com.petwise.domain.appointment.AppointmentGateway;
import com.petwise.domain.appointment.ServiceType;
import com.petwise.domain.exceptions.NotificationException;
import com.petwise.domain.pet.PetID;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Unit tests for {@link DefaultCreateAppointmentUseCase}. */
@ExtendWith(MockitoExtension.class)
@DisplayName("CreateAppointmentUseCase Tests")
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals"
})
class CreateAppointmentUseCaseTest extends UseCaseTest {

    /** The mocked appointment gateway. */
    @Mock private AppointmentGateway appointmentGateway;

    /** The use case under test. */
    @InjectMocks private DefaultCreateAppointmentUseCase useCase;

    /** Default constructor. */
    CreateAppointmentUseCaseTest() {}

    @Override
    protected List<Object> getMocks() {
        return List.of(appointmentGateway);
    }

    @Test
    @DisplayName("Should create appointment successfully")
    void givenValidCommand_whenExecute_thenCreateAppointment() {
        // given
        final var petId = PetID.unique().getValue();
        final var serviceType = ServiceType.DAYCARE;
        final var startAt = Instant.parse("2025-11-28T08:00:00Z");
        final var endAt = Instant.parse("2025-11-28T18:00:00Z");
        final var notes = "First time at daycare";

        final var command =
                CreateAppointmentCommand.with(petId, serviceType, startAt, endAt, notes);

        when(appointmentGateway.save(any(Appointment.class))).thenAnswer(inv -> inv.getArgument(0));

        // when
        final var output = useCase.execute(command);

        // then
        assertNotNull(output);
        assertNotNull(output.id());

        verify(appointmentGateway, times(1))
                .save(
                        argThat(
                                a ->
                                        serviceType.equals(a.getServiceType())
                                                && startAt.equals(a.getStartAt())
                                                && endAt.equals(a.getEndAt())
                                                && notes.equals(a.getNotes())));
    }

    @Test
    @DisplayName("Should create appointment without notes")
    void givenCommandWithoutNotes_whenExecute_thenCreateAppointment() {
        // given
        final var petId = PetID.unique().getValue();
        final var command =
                CreateAppointmentCommand.with(
                        petId,
                        ServiceType.HOTEL,
                        Instant.parse("2025-11-28T08:00:00Z"),
                        Instant.parse("2025-11-28T18:00:00Z"),
                        null);

        when(appointmentGateway.save(any(Appointment.class))).thenAnswer(inv -> inv.getArgument(0));

        // when
        final var output = useCase.execute(command);

        // then
        assertNotNull(output);
        verify(appointmentGateway, times(1)).save(any(Appointment.class));
    }

    @Test
    @DisplayName("Should throw NotificationException when startAt is after endAt")
    void givenInvalidDateRange_whenExecute_thenThrowNotificationException() {
        // given
        final var command =
                CreateAppointmentCommand.with(
                        PetID.unique().getValue(),
                        ServiceType.DAYCARE,
                        Instant.parse("2025-11-28T18:00:00Z"),
                        Instant.parse("2025-11-28T08:00:00Z"),
                        null);

        // when & then
        assertThrows(NotificationException.class, () -> useCase.execute(command));
        verify(appointmentGateway, never()).save(any());
    }

    @Test
    @DisplayName("Should throw NotificationException when petId is null")
    void givenNullPetId_whenExecute_thenThrowException() {
        // given
        final var command =
                CreateAppointmentCommand.with(
                        null,
                        ServiceType.DAYCARE,
                        Instant.parse("2025-11-28T08:00:00Z"),
                        Instant.parse("2025-11-28T18:00:00Z"),
                        null);

        // when & then
        assertThrows(Exception.class, () -> useCase.execute(command));
        verify(appointmentGateway, never()).save(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when command is null")
    void givenNullCommand_whenExecute_thenThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verify(appointmentGateway, never()).save(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when gateway is null")
    void givenNullGateway_whenConstruct_thenThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DefaultCreateAppointmentUseCase(null));
    }
}
