package com.petwise.infrastructure.appointment.models;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.JacksonTest;
import com.petwise.domain.appointment.AppointmentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

/** Jackson serialisation tests for {@link ChangeAppointmentStatusRequest}. */
@JacksonTest
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals"
})
class ChangeAppointmentStatusRequestTest {

    /** Default constructor. */
    ChangeAppointmentStatusRequestTest() {}

    /** The Jackson tester for ChangeAppointmentStatusRequest. */
    @Autowired private JacksonTester<ChangeAppointmentStatusRequest> json;

    @Test
    void givenValidRequest_whenSerialize_thenShouldContainStatus() throws Exception {
        // given
        final var request = new ChangeAppointmentStatusRequest(AppointmentStatus.ACTIVE);

        // when
        final var jsonContent = this.json.write(request);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.status").isEqualTo("ACTIVE");
    }

    @Test
    void givenValidJson_whenDeserialize_thenShouldMapStatus() throws Exception {
        // given
        final var jsonContent =
                """
                {
                    "status": "ACTIVE"
                }
                """;

        // when
        final var request = this.json.parse(jsonContent).getObject();

        // then
        assertThat(request.status()).isEqualTo(AppointmentStatus.ACTIVE);
    }

    @Test
    void givenPendingStatus_whenSerialize_thenShouldContainPending() throws Exception {
        // given
        final var request = new ChangeAppointmentStatusRequest(AppointmentStatus.PENDING);

        // when
        final var jsonContent = this.json.write(request);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.status").isEqualTo("PENDING");
    }

    @Test
    void givenCompletedStatus_whenSerialize_thenShouldContainCompleted() throws Exception {
        // given
        final var request = new ChangeAppointmentStatusRequest(AppointmentStatus.COMPLETED);

        // when
        final var jsonContent = this.json.write(request);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.status").isEqualTo("COMPLETED");
    }

    @Test
    void givenAllStatuses_whenDeserialize_thenShouldMapEachCorrectly() throws Exception {
        for (final var status : AppointmentStatus.values()) {
            // given
            final var jsonContent = "{\"status\": \"" + status.name() + "\"}";

            // when
            final var request = this.json.parse(jsonContent).getObject();

            // then
            assertThat(request.status()).isEqualTo(status);
        }
    }
}
