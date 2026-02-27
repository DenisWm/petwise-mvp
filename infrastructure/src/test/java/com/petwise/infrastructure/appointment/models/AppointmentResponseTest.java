package com.petwise.infrastructure.appointment.models;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.JacksonTest;
import com.petwise.domain.appointment.AppointmentStatus;
import com.petwise.domain.appointment.ServiceType;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

/** Jackson serialisation tests for {@link AppointmentResponse}. */
@JacksonTest
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals"
})
class AppointmentResponseTest {

    /** Default constructor. */
    AppointmentResponseTest() {}

    /** The Jackson tester for AppointmentResponse. */
    @Autowired private JacksonTester<AppointmentResponse> json;

    @Test
    void testSerialize() throws Exception {
        // given
        final var startAt = Instant.parse("2025-11-28T08:00:00Z");
        final var endAt = Instant.parse("2025-11-28T18:00:00Z");
        final var createdAt = Instant.parse("2024-01-15T10:30:00Z");
        final var updatedAt = Instant.parse("2024-01-15T11:45:00Z");
        final var response =
                new AppointmentResponse(
                        "appt-123",
                        "pet-456",
                        ServiceType.CRECHE,
                        AppointmentStatus.PENDING,
                        startAt,
                        endAt,
                        "Test notes",
                        createdAt,
                        updatedAt);

        // when
        final var jsonContent = this.json.write(response);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.id").isEqualTo("appt-123");
        assertThat(jsonContent).extractingJsonPathStringValue("$.pet_id").isEqualTo("pet-456");
        assertThat(jsonContent).extractingJsonPathStringValue("$.service_type").isEqualTo("CRECHE");
        assertThat(jsonContent).extractingJsonPathStringValue("$.status").isEqualTo("PENDING");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.start_at")
                .isEqualTo("2025-11-28T08:00:00Z");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.end_at")
                .isEqualTo("2025-11-28T18:00:00Z");
        assertThat(jsonContent).extractingJsonPathStringValue("$.notes").isEqualTo("Test notes");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.created_at")
                .isEqualTo("2024-01-15T10:30:00Z");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.updated_at")
                .isEqualTo("2024-01-15T11:45:00Z");
    }

    @Test
    void testDeserialize() throws Exception {
        // given
        final var jsonContent =
                """
                {
                    "id": "appt-123",
                    "pet_id": "pet-456",
                    "service_type": "CRECHE",
                    "status": "PENDING",
                    "start_at": "2025-11-28T08:00:00Z",
                    "end_at": "2025-11-28T18:00:00Z",
                    "notes": "Test notes",
                    "created_at": "2024-01-15T10:30:00Z",
                    "updated_at": "2024-01-15T11:45:00Z"
                }
                """;

        // when
        final var response = this.json.parse(jsonContent).getObject();

        // then
        assertThat(response.id()).isEqualTo("appt-123");
        assertThat(response.petId()).isEqualTo("pet-456");
        assertThat(response.serviceType()).isEqualTo(ServiceType.CRECHE);
        assertThat(response.status()).isEqualTo(AppointmentStatus.PENDING);
        assertThat(response.startAt()).isEqualTo(Instant.parse("2025-11-28T08:00:00Z"));
        assertThat(response.endAt()).isEqualTo(Instant.parse("2025-11-28T18:00:00Z"));
        assertThat(response.notes()).isEqualTo("Test notes");
        assertThat(response.createdAt()).isEqualTo(Instant.parse("2024-01-15T10:30:00Z"));
        assertThat(response.updatedAt()).isEqualTo(Instant.parse("2024-01-15T11:45:00Z"));
    }

    @Test
    void testSerializeWithNullNotes() throws Exception {
        // given
        final var startAt = Instant.parse("2025-12-01T10:00:00Z");
        final var endAt = Instant.parse("2025-12-03T10:00:00Z");
        final var createdAt = Instant.parse("2024-01-15T10:30:00Z");
        final var updatedAt = Instant.parse("2024-01-15T11:45:00Z");
        final var response =
                new AppointmentResponse(
                        "appt-123",
                        "pet-456",
                        ServiceType.HOTEL,
                        AppointmentStatus.ACTIVE,
                        startAt,
                        endAt,
                        null,
                        createdAt,
                        updatedAt);

        // when
        final var jsonContent = this.json.write(response);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.service_type").isEqualTo("HOTEL");
        assertThat(jsonContent).extractingJsonPathStringValue("$.status").isEqualTo("ACTIVE");
        assertThat(jsonContent).doesNotHaveJsonPath("$.notes");
    }

    @Test
    void testDeserializeWithNullNotes() throws Exception {
        // given
        final var jsonContent =
                """
                {
                    "id": "appt-123",
                    "pet_id": "pet-456",
                    "service_type": "HOTEL",
                    "status": "COMPLETED",
                    "start_at": "2025-12-01T10:00:00Z",
                    "end_at": "2025-12-03T10:00:00Z",
                    "created_at": "2024-01-15T10:30:00Z",
                    "updated_at": "2024-01-15T11:45:00Z"
                }
                """;

        // when
        final var response = this.json.parse(jsonContent).getObject();

        // then
        assertThat(response.serviceType()).isEqualTo(ServiceType.HOTEL);
        assertThat(response.status()).isEqualTo(AppointmentStatus.COMPLETED);
        assertThat(response.notes()).isNull();
    }
}
