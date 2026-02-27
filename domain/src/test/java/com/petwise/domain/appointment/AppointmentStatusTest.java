package com.petwise.domain.appointment;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.domain.UnitTest;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link AppointmentStatus}. */
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.UnitTestContainsTooManyAsserts"
})
class AppointmentStatusTest extends UnitTest {

    /** Default constructor. */
    AppointmentStatusTest() {}

    @Test
    void givenPendingStatus_whenTransitionToActiveOrCanceled_thenShouldBeAllowed() {
        assertThat(AppointmentStatus.PENDING.canTransitionTo(AppointmentStatus.ACTIVE)).isTrue();
        assertThat(AppointmentStatus.PENDING.canTransitionTo(AppointmentStatus.CANCELED)).isTrue();
    }

    @Test
    void givenActiveStatus_whenTransitionToCompleted_thenShouldBeAllowed() {
        assertThat(AppointmentStatus.ACTIVE.canTransitionTo(AppointmentStatus.COMPLETED)).isTrue();
    }

    @Test
    void givenCompletedOrCanceled_whenTransition_thenShouldBeRejected() {
        assertThat(AppointmentStatus.COMPLETED.canTransitionTo(AppointmentStatus.ACTIVE)).isFalse();
        assertThat(AppointmentStatus.CANCELED.canTransitionTo(AppointmentStatus.ACTIVE)).isFalse();
    }

    @Test
    void givenAnyStatus_whenTransitionToNull_thenShouldReturnFalse() {
        for (final var status : AppointmentStatus.values()) {
            assertThat(status.canTransitionTo(null)).isFalse();
        }
    }

    @Test
    void givenPendingStatus_whenTransitionToCompleted_thenShouldBeRejected() {
        assertThat(AppointmentStatus.PENDING.canTransitionTo(AppointmentStatus.COMPLETED))
                .isFalse();
    }

    @Test
    void givenActiveStatus_whenTransitionToActive_thenShouldBeRejected() {
        assertThat(AppointmentStatus.ACTIVE.canTransitionTo(AppointmentStatus.ACTIVE)).isFalse();
    }

    @Test
    void givenCanceledStatus_whenTransitionToCompleted_thenShouldBeRejected() {
        assertThat(AppointmentStatus.CANCELED.canTransitionTo(AppointmentStatus.COMPLETED))
                .isFalse();
    }
}
