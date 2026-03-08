package com.petwise.domain.appointment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.petwise.domain.UnitTest;
import com.petwise.domain.exceptions.DomainException;
import com.petwise.domain.pet.PetID;
import java.time.Instant;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link Appointment}. */
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals",
    "PMD.LongVariable",
    "PMD.ShortVariable",
    "PMD.TooManyMethods"
})
class AppointmentTest extends UnitTest {

    /** Default constructor. */
    AppointmentTest() {}

    @Test
    void givenValidParams_whenCallsNewAppointment_thenShouldInstantiateAppointment() {
        // given
        final var expectedPetId = PetID.unique();
        final var expectedServiceType = ServiceType.DAYCARE;
        final var expectedStartAt = Instant.parse("2025-11-28T08:00:00Z");
        final var expectedEndAt = Instant.parse("2025-11-28T18:00:00Z");
        final var expectedNotes = "First time at daycare";

        // when
        final var actualAppointment =
                Appointment.newAppointment(
                        expectedPetId,
                        expectedServiceType,
                        expectedStartAt,
                        expectedEndAt,
                        expectedNotes);

        // then
        assertThat(actualAppointment).isNotNull();
        assertThat(actualAppointment.getId()).isNotNull();
        assertThat(actualAppointment.getPetId()).isEqualTo(expectedPetId);
        assertThat(actualAppointment.getServiceType()).isEqualTo(expectedServiceType);
        assertThat(actualAppointment.getStatus()).isEqualTo(AppointmentStatus.PENDING);
        assertThat(actualAppointment.getStartAt()).isEqualTo(expectedStartAt);
        assertThat(actualAppointment.getEndAt()).isEqualTo(expectedEndAt);
        assertThat(actualAppointment.getNotes()).isEqualTo(expectedNotes);
        assertThat(actualAppointment.getCreatedAt()).isNotNull();
        assertThat(actualAppointment.getUpdatedAt()).isNotNull();
    }

    @Test
    void givenPendingAppointment_whenChangeStatusToActive_thenShouldUpdateStatus() {
        // given
        final var appointment =
                Appointment.newAppointment(
                        PetID.unique(),
                        ServiceType.HOTEL,
                        Instant.parse("2025-11-28T08:00:00Z"),
                        Instant.parse("2025-11-28T18:00:00Z"),
                        null);

        // when
        final var updated = appointment.changeStatus(AppointmentStatus.ACTIVE);

        // then
        assertThat(updated.getStatus()).isEqualTo(AppointmentStatus.ACTIVE);
    }

    @Test
    void givenPendingAppointment_whenChangeStatusToCanceled_thenShouldUpdateStatus() {
        // given
        final var appointment =
                Appointment.newAppointment(
                        PetID.unique(),
                        ServiceType.DAYCARE,
                        Instant.parse("2025-11-28T08:00:00Z"),
                        Instant.parse("2025-11-28T18:00:00Z"),
                        null);

        // when
        final var updated = appointment.changeStatus(AppointmentStatus.CANCELED);

        // then
        assertThat(updated.getStatus()).isEqualTo(AppointmentStatus.CANCELED);
    }

    @Test
    void givenActiveAppointment_whenChangeStatusToCompleted_thenShouldUpdateStatus() {
        // given
        final var appointment =
                Appointment.newAppointment(
                        PetID.unique(),
                        ServiceType.DAYCARE,
                        Instant.parse("2025-11-28T08:00:00Z"),
                        Instant.parse("2025-11-28T18:00:00Z"),
                        null);
        appointment.changeStatus(AppointmentStatus.ACTIVE);

        // when
        final var updated = appointment.changeStatus(AppointmentStatus.COMPLETED);

        // then
        assertThat(updated.getStatus()).isEqualTo(AppointmentStatus.COMPLETED);
    }

    @Test
    void givenCompletedAppointment_whenChangeStatus_thenShouldThrowDomainException() {
        // given
        final var appointment =
                Appointment.newAppointment(
                        PetID.unique(),
                        ServiceType.DAYCARE,
                        Instant.parse("2025-11-28T08:00:00Z"),
                        Instant.parse("2025-11-28T18:00:00Z"),
                        null);
        appointment.changeStatus(AppointmentStatus.ACTIVE);
        appointment.changeStatus(AppointmentStatus.COMPLETED);

        // when & then
        assertThatThrownBy(() -> appointment.changeStatus(AppointmentStatus.ACTIVE))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'status' cannot transition");
    }

    @Test
    void givenValidParams_whenCallsWith_thenShouldInstantiateAppointment() {
        // given
        final var expectedId = "123456789";
        final var expectedPetId = PetID.unique();
        final var expectedServiceType = ServiceType.DAYCARE;
        final var expectedStatus = AppointmentStatus.PENDING;
        final var expectedStartAt = Instant.parse("2025-11-28T08:00:00Z");
        final var expectedEndAt = Instant.parse("2025-11-28T18:00:00Z");
        final var expectedNotes = "Notes";
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();

        // when
        final var actualAppointment =
                Appointment.with(
                        expectedId,
                        expectedPetId.getValue(),
                        expectedServiceType,
                        expectedStatus,
                        expectedStartAt,
                        expectedEndAt,
                        expectedNotes,
                        expectedCreatedAt,
                        expectedUpdatedAt);

        // then
        assertThat(actualAppointment).isNotNull();
        assertThat(actualAppointment.getId().getValue()).isEqualTo(expectedId);
        assertThat(actualAppointment.getPetId()).isEqualTo(expectedPetId);
        assertThat(actualAppointment.getServiceType()).isEqualTo(expectedServiceType);
        assertThat(actualAppointment.getStatus()).isEqualTo(expectedStatus);
        assertThat(actualAppointment.getStartAt()).isEqualTo(expectedStartAt);
        assertThat(actualAppointment.getEndAt()).isEqualTo(expectedEndAt);
        assertThat(actualAppointment.getNotes()).isEqualTo(expectedNotes);
        assertThat(actualAppointment.getCreatedAt()).isEqualTo(expectedCreatedAt);
        assertThat(actualAppointment.getUpdatedAt()).isEqualTo(expectedUpdatedAt);
    }

    @Test
    void givenValidParams_whenCallsWithUsingAppointmentID_thenShouldInstantiateAppointment() {
        // given
        final var expectedId = AppointmentID.unique();
        final var expectedPetId = PetID.unique();
        final var expectedServiceType = ServiceType.HOTEL;
        final var expectedStatus = AppointmentStatus.ACTIVE;
        final var expectedStartAt = Instant.parse("2025-12-01T10:00:00Z");
        final var expectedEndAt = Instant.parse("2025-12-03T10:00:00Z");
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();

        // when
        final var actualAppointment =
                Appointment.with(
                        expectedId,
                        expectedPetId,
                        expectedServiceType,
                        expectedStatus,
                        expectedStartAt,
                        expectedEndAt,
                        null,
                        expectedCreatedAt,
                        expectedUpdatedAt);

        // then
        assertThat(actualAppointment).isNotNull();
        assertThat(actualAppointment.getId()).isEqualTo(expectedId);
        assertThat(actualAppointment.getPetId()).isEqualTo(expectedPetId);
        assertThat(actualAppointment.getNotes()).isNull();
    }

    @Test
    void givenAppointment_whenChangeStatusToNull_thenShouldThrowDomainException() {
        // given
        final var appointment =
                Appointment.newAppointment(
                        PetID.unique(),
                        ServiceType.DAYCARE,
                        Instant.parse("2025-11-28T08:00:00Z"),
                        Instant.parse("2025-11-28T18:00:00Z"),
                        null);

        // when & then
        assertThatThrownBy(() -> appointment.changeStatus(null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'status' should not be null");
    }

    @Test
    void givenValidAppointment_whenValidate_thenShouldNotHaveErrors() {
        // given
        final var appointment =
                Appointment.newAppointment(
                        PetID.unique(),
                        ServiceType.DAYCARE,
                        Instant.parse("2025-11-28T08:00:00Z"),
                        Instant.parse("2025-11-28T18:00:00Z"),
                        "Notes");
        final var notification = com.petwise.domain.validation.handler.Notification.create();

        // when
        appointment.validate(notification);

        // then
        assertThat(notification.hasErrors()).isFalse();
    }

    @Test
    void givenCanceledAppointment_whenChangeStatus_thenShouldThrowDomainException() {
        // given
        final var appointment =
                Appointment.newAppointment(
                        PetID.unique(),
                        ServiceType.DAYCARE,
                        Instant.parse("2025-11-28T08:00:00Z"),
                        Instant.parse("2025-11-28T18:00:00Z"),
                        null);
        appointment.changeStatus(AppointmentStatus.CANCELED);

        // when & then
        assertThatThrownBy(() -> appointment.changeStatus(AppointmentStatus.ACTIVE))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'status' cannot transition");
    }
}
