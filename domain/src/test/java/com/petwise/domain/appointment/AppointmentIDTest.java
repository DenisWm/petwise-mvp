package com.petwise.domain.appointment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.petwise.domain.UnitTest;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link AppointmentID}. */
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.ShortVariable",
    "PMD.AvoidDuplicateLiterals"
})
class AppointmentIDTest extends UnitTest {

    /** Default constructor. */
    AppointmentIDTest() {}

    @Test
    void givenNoParameters_whenCallsUnique_thenShouldGenerateNewId() {
        // when
        final var id1 = AppointmentID.unique();
        final var id2 = AppointmentID.unique();

        // then
        assertThat(id1).isNotNull();
        assertThat(id1.getValue()).isNotNull();
        assertThat(id1.getValue()).isNotBlank();
        assertThat(id1).isNotEqualTo(id2);
        assertThat(id1.getValue()).isNotEqualTo(id2.getValue());
    }

    @Test
    void givenValidId_whenCallsFrom_thenShouldCreateAppointmentID() {
        // given
        final var expectedId = "123e4567-e89b-12d3-a456-426614174000";

        // when
        final var actualId = AppointmentID.from(expectedId);

        // then
        assertThat(actualId).isNotNull();
        assertThat(actualId.getValue()).isEqualTo(expectedId);
    }

    @Test
    void givenNullId_whenCallsFrom_thenShouldThrowException() {
        // when & then
        assertThatThrownBy(() -> AppointmentID.from(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenSameIdValue_whenCompare_thenShouldBeEqual() {
        // given
        final var id = "test-id-123";
        final var appointmentId1 = AppointmentID.from(id);
        final var appointmentId2 = AppointmentID.from(id);

        // when & then
        assertThat(appointmentId1).isEqualTo(appointmentId2);
        assertThat(appointmentId1.hashCode()).isEqualTo(appointmentId2.hashCode());
    }

    @Test
    void givenDifferentIdValues_whenCompare_thenShouldNotBeEqual() {
        // given
        final var appointmentId1 = AppointmentID.from("id-1");
        final var appointmentId2 = AppointmentID.from("id-2");

        // when & then
        assertThat(appointmentId1).isNotEqualTo(appointmentId2);
        assertThat(appointmentId1.hashCode()).isNotEqualTo(appointmentId2.hashCode());
    }

    @Test
    void givenAppointmentID_whenCallsToString_thenShouldReturnValue() {
        // given
        final var expectedId = "test-id";
        final var appointmentId = AppointmentID.from(expectedId);

        // when
        final var result = appointmentId.toString();

        // then
        assertThat(result).isEqualTo(expectedId);
    }

    @Test
    void givenAppointmentID_whenCallsGetValue_thenShouldReturnCorrectValue() {
        // given
        final var expectedValue = "my-appointment-id";
        final var appointmentId = AppointmentID.from(expectedValue);

        // when
        final var actualValue = appointmentId.getValue();

        // then
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void givenAppointmentID_whenCompareWithNull_thenShouldNotBeEqual() {
        final var appointmentId = AppointmentID.from("id-1");
        assertThat(appointmentId).isNotEqualTo(null);
    }

    @Test
    void givenAppointmentID_whenCompareWithSelf_thenShouldBeEqual() {
        final var appointmentId = AppointmentID.from("id-1");
        assertThat(appointmentId).isEqualTo(appointmentId);
    }

    @Test
    void givenAppointmentID_whenCompareWithDifferentType_thenShouldNotBeEqual() {
        final var appointmentId = AppointmentID.from("id-1");
        assertThat(appointmentId).isNotEqualTo("id-1");
    }
}
