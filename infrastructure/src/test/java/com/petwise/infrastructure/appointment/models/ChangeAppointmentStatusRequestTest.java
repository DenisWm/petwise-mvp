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
    ChangeAppointmentStatusRequestTest() {}

    @Autowired private JacksonTester<ChangeAppointmentStatusRequest> json;

    @Test
    void givenValidRequest_whenSerialize_thenShouldContainStatus() throws Exception {
        final var request = new ChangeAppointmentStatusRequest(AppointmentStatus.ACTIVE);
        final var jsonContent = this.json.write(request);
        assertThat(jsonContent).extractingJsonPathStringValue("$.status").isEqualTo("ACTIVE");
    }

    @Test
    void givenValidJson_whenDeserialize_thenShouldMapStatus() throws Exception {
        final var jsonContent =
                """
                {
                    "status": "ACTIVE"
                }
                """;
        final var request = this.json.parse(jsonContent).getObject();
        assertThat(request.status()).isEqualTo(AppointmentStatus.ACTIVE);
    }

    @Test
    void givenPendingStatus_whenSerialize_thenShouldContainPending() throws Exception {
        final var request = new ChangeAppointmentStatusRequest(AppointmentStatus.PENDING);
        final var jsonContent = this.json.write(request);
        assertThat(jsonContent).extractingJsonPathStringValue("$.status").isEqualTo("PENDING");
    }

    @Test
    void givenCompletedStatus_whenSerialize_thenShouldContainCompleted() throws Exception {
        final var request = new ChangeAppointmentStatusRequest(AppointmentStatus.COMPLETED);
        final var jsonContent = this.json.write(request);
        assertThat(jsonContent).extractingJsonPathStringValue("$.status").isEqualTo("COMPLETED");
    }

    @Test
    void givenAllStatuses_whenDeserialize_thenShouldMapEachCorrectly() throws Exception {
        for (final var status : AppointmentStatus.values()) {
            final var jsonContent = "{\"status\": \"" + status.name() + "\"}";
            final var request = this.json.parse(jsonContent).getObject();
            assertThat(request.status()).isEqualTo(status);
        }
    }
}
