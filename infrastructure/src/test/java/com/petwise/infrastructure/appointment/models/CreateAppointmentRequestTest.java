package com.petwise.infrastructure.appointment.models;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.JacksonTest;
import com.petwise.domain.appointment.ServiceType;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

/** Jackson serialisation tests for {@link CreateAppointmentRequest}. */
@JacksonTest
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals"
})
class CreateAppointmentRequestTest {

    /** Default constructor. */
    CreateAppointmentRequestTest() {}

    /** The Jackson tester for CreateAppointmentRequest. */
    @Autowired private JacksonTester<CreateAppointmentRequest> json;

    @Test
    void testSerialize() throws Exception {
        // given
        final var startAt = Instant.parse("2025-11-28T08:00:00Z");
        final var endAt = Instant.parse("2025-11-28T18:00:00Z");
        final var request =
                new CreateAppointmentRequest(
                        "pet-123", ServiceType.CRECHE, startAt, endAt, "Test notes");

        // when
        final var jsonContent = this.json.write(request);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.pet_id").isEqualTo("pet-123");
        assertThat(jsonContent).extractingJsonPathStringValue("$.service_type").isEqualTo("CRECHE");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.start_at")
                .isEqualTo("2025-11-28T08:00:00Z");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.end_at")
                .isEqualTo("2025-11-28T18:00:00Z");
        assertThat(jsonContent).extractingJsonPathStringValue("$.notes").isEqualTo("Test notes");
    }

    @Test
    void testDeserialize() throws Exception {
        // given
        final var jsonContent =
                """
                {
                    "pet_id": "pet-123",
                    "service_type": "CRECHE",
                    "start_at": "2025-11-28T08:00:00Z",
                    "end_at": "2025-11-28T18:00:00Z",
                    "notes": "Test notes"
                }
                """;

        // when
        final var request = this.json.parse(jsonContent).getObject();

        // then
        assertThat(request.petId()).isEqualTo("pet-123");
        assertThat(request.serviceType()).isEqualTo(ServiceType.CRECHE);
        assertThat(request.startAt()).isEqualTo(Instant.parse("2025-11-28T08:00:00Z"));
        assertThat(request.endAt()).isEqualTo(Instant.parse("2025-11-28T18:00:00Z"));
        assertThat(request.notes()).isEqualTo("Test notes");
    }

    @Test
    void testSerializeWithHotelServiceType() throws Exception {
        // given
        final var startAt = Instant.parse("2025-12-01T10:00:00Z");
        final var endAt = Instant.parse("2025-12-03T10:00:00Z");
        final var request =
                new CreateAppointmentRequest("pet-456", ServiceType.HOTEL, startAt, endAt, null);

        // when
        final var jsonContent = this.json.write(request);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.pet_id").isEqualTo("pet-456");
        assertThat(jsonContent).extractingJsonPathStringValue("$.service_type").isEqualTo("HOTEL");
        assertThat(jsonContent).doesNotHaveJsonPath("$.notes");
    }

    @Test
    void testDeserializeWithNullNotes() throws Exception {
        // given
        final var jsonContent =
                """
                {
                    "pet_id": "pet-123",
                    "service_type": "HOTEL",
                    "start_at": "2025-12-01T10:00:00Z",
                    "end_at": "2025-12-03T10:00:00Z"
                }
                """;

        // when
        final var request = this.json.parse(jsonContent).getObject();

        // then
        assertThat(request.petId()).isEqualTo("pet-123");
        assertThat(request.serviceType()).isEqualTo(ServiceType.HOTEL);
        assertThat(request.notes()).isNull();
    }
}
