package com.petwise.domain.appointment;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.domain.UnitTest;
import com.petwise.domain.pet.PetID;
import com.petwise.domain.validation.handler.Notification;
import java.time.Instant;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link AppointmentValidator}. */
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals"
})
class AppointmentValidatorTest extends UnitTest {
    AppointmentValidatorTest() {}

    @Test
    void givenValidAppointment_whenValidate_thenShouldNotHaveErrors() {
        final var appointment =
                Appointment.newAppointment(
                        PetID.unique(),
                        ServiceType.DAYCARE,
                        Instant.parse("2025-11-28T08:00:00Z"),
                        Instant.parse("2025-11-28T18:00:00Z"),
                        null);
        final var notification = Notification.create();
        final var validator = new AppointmentValidator(appointment, notification);
        validator.validate();
        assertThat(notification.hasErrors()).isFalse();
        assertThat(notification.getErrors()).isEmpty();
    }

    @Test
    void givenNullPetId_whenValidate_thenShouldHaveError() {
        final var appointment =
                Appointment.newAppointment(
                        PetID.unique(),
                        ServiceType.DAYCARE,
                        Instant.parse("2025-11-28T08:00:00Z"),
                        Instant.parse("2025-11-28T18:00:00Z"),
                        null);
        final var apptWithNullPetId =
                Appointment.with(
                        appointment.getId(),
                        null,
                        appointment.getServiceType(),
                        appointment.getStatus(),
                        appointment.getStartAt(),
                        appointment.getEndAt(),
                        appointment.getNotes(),
                        appointment.getCreatedAt(),
                        appointment.getUpdatedAt());
        final var notification = Notification.create();
        final var validator = new AppointmentValidator(apptWithNullPetId, notification);
        validator.validate();
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(1);
        assertThat(notification.firstError().message()).isEqualTo("'petId' should not be null");
    }

    @Test
    void givenInvalidDateRange_whenValidate_thenShouldHaveError() {
        final var appointment =
                Appointment.newAppointment(
                        PetID.unique(),
                        ServiceType.DAYCARE,
                        Instant.parse("2025-11-28T18:00:00Z"),
                        Instant.parse("2025-11-28T08:00:00Z"),
                        null);
        final var notification = Notification.create();
        final var validator = new AppointmentValidator(appointment, notification);
        validator.validate();
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(1);
        assertThat(notification.firstError().message())
                .isEqualTo("'startAt' must be before 'endAt'");
    }

    @Test
    void givenEqualStartAndEndTime_whenValidate_thenShouldHaveError() {
        final var time = Instant.parse("2025-11-28T08:00:00Z");
        final var appointment =
                Appointment.newAppointment(PetID.unique(), ServiceType.DAYCARE, time, time, null);
        final var notification = Notification.create();
        final var validator = new AppointmentValidator(appointment, notification);
        validator.validate();
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.firstError().message())
                .isEqualTo("'startAt' must be before 'endAt'");
    }

    @Test
    void givenNullServiceType_whenValidate_thenShouldHaveError() {
        final var base =
                Appointment.newAppointment(
                        PetID.unique(),
                        ServiceType.DAYCARE,
                        Instant.parse("2025-11-28T08:00:00Z"),
                        Instant.parse("2025-11-28T18:00:00Z"),
                        null);
        final var appointment =
                Appointment.with(
                        base.getId(),
                        base.getPetId(),
                        null,
                        base.getStatus(),
                        base.getStartAt(),
                        base.getEndAt(),
                        null,
                        base.getCreatedAt(),
                        base.getUpdatedAt());
        final var notification = Notification.create();
        new AppointmentValidator(appointment, notification).validate();
        assertThat(notification.hasErrors()).isTrue();
        assertThat(
                        notification.getErrors().stream()
                                .anyMatch(e -> e.message().contains("serviceType")))
                .isTrue();
    }

    @Test
    void givenNullStartAt_whenValidate_thenShouldHaveError() {
        final var base =
                Appointment.newAppointment(
                        PetID.unique(),
                        ServiceType.DAYCARE,
                        Instant.parse("2025-11-28T08:00:00Z"),
                        Instant.parse("2025-11-28T18:00:00Z"),
                        null);
        final var appointment =
                Appointment.with(
                        base.getId(),
                        base.getPetId(),
                        base.getServiceType(),
                        base.getStatus(),
                        null,
                        base.getEndAt(),
                        null,
                        base.getCreatedAt(),
                        base.getUpdatedAt());
        final var notification = Notification.create();
        new AppointmentValidator(appointment, notification).validate();
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors().stream().anyMatch(e -> e.message().contains("startAt")))
                .isTrue();
    }

    @Test
    void givenNullEndAt_whenValidate_thenShouldHaveError() {
        final var base =
                Appointment.newAppointment(
                        PetID.unique(),
                        ServiceType.DAYCARE,
                        Instant.parse("2025-11-28T08:00:00Z"),
                        Instant.parse("2025-11-28T18:00:00Z"),
                        null);
        final var appointment =
                Appointment.with(
                        base.getId(),
                        base.getPetId(),
                        base.getServiceType(),
                        base.getStatus(),
                        base.getStartAt(),
                        null,
                        null,
                        base.getCreatedAt(),
                        base.getUpdatedAt());
        final var notification = Notification.create();
        new AppointmentValidator(appointment, notification).validate();
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors().stream().anyMatch(e -> e.message().contains("endAt")))
                .isTrue();
    }

    @Test
    void givenBothDatesNull_whenValidate_thenShouldSkipDateRangeCheck() {
        // given — startAt and endAt both null: dateRange check returns early
        final var base =
                Appointment.newAppointment(
                        PetID.unique(),
                        ServiceType.DAYCARE,
                        Instant.parse("2025-11-28T08:00:00Z"),
                        Instant.parse("2025-11-28T18:00:00Z"),
                        null);
        final var appointment =
                Appointment.with(
                        base.getId(),
                        base.getPetId(),
                        base.getServiceType(),
                        base.getStatus(),
                        null,
                        null,
                        null,
                        base.getCreatedAt(),
                        base.getUpdatedAt());
        final var notification = Notification.create();
        new AppointmentValidator(appointment, notification).validate();

        // then — only startAt and endAt null errors, no date-range error
        assertThat(
                        notification.getErrors().stream()
                                .noneMatch(
                                        e ->
                                                "'startAt' must be before 'endAt'"
                                                        .equals(e.message())))
                .isTrue();
    }
}
