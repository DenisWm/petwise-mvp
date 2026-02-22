package com.petwise.infrastructure.tutor.models;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.JacksonTest;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

/** Jackson serialisation tests for {@link TutorResponse}. */
@JacksonTest
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals"
})
class TutorResponseTest {

    /** Default constructor. */
    TutorResponseTest() {}

    /** The Jackson tester for TutorResponse. */
    @Autowired private JacksonTester<TutorResponse> json;

    @Test
    void testSerialize() throws Exception {
        // given
        final var createdAt = Instant.parse("2024-01-15T10:30:00Z");
        final var updatedAt = Instant.parse("2024-01-15T11:45:00Z");
        final var response =
                new TutorResponse(
                        "tutor-123",
                        "John Doe",
                        "john@example.com",
                        "+1234567890",
                        createdAt,
                        updatedAt);

        // when
        final var jsonContent = this.json.write(response);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.id").isEqualTo("tutor-123");
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.email")
                .isEqualTo("john@example.com");
        assertThat(jsonContent).extractingJsonPathStringValue("$.phone").isEqualTo("+1234567890");
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
                    "id": "tutor-123",
                    "name": "John Doe",
                    "email": "john@example.com",
                    "phone": "+1234567890",
                    "created_at": "2024-01-15T10:30:00Z",
                    "updated_at": "2024-01-15T11:45:00Z"
                }
                """;

        // when
        final var response = this.json.parse(jsonContent).getObject();

        // then
        assertThat(response.id()).isEqualTo("tutor-123");
        assertThat(response.name()).isEqualTo("John Doe");
        assertThat(response.email()).isEqualTo("john@example.com");
        assertThat(response.phone()).isEqualTo("+1234567890");
        assertThat(response.createdAt()).isEqualTo(Instant.parse("2024-01-15T10:30:00Z"));
        assertThat(response.updatedAt()).isEqualTo(Instant.parse("2024-01-15T11:45:00Z"));
    }

    @Test
    void testSerializeWithNullEmail() throws Exception {
        // given
        final var createdAt = Instant.parse("2024-01-15T10:30:00Z");
        final var updatedAt = Instant.parse("2024-01-15T11:45:00Z");
        final var response =
                new TutorResponse(
                        "tutor-123", "John Doe", null, "+1234567890", createdAt, updatedAt);

        // when
        final var jsonContent = this.json.write(response);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.id").isEqualTo("tutor-123");
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
        assertThat(jsonContent).doesNotHaveJsonPath("$.email"); // null should not be rendered
        assertThat(jsonContent).extractingJsonPathStringValue("$.phone").isEqualTo("+1234567890");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.created_at")
                .isEqualTo("2024-01-15T10:30:00Z");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.updated_at")
                .isEqualTo("2024-01-15T11:45:00Z");
    }

    @Test
    void testDeserializeWithNullEmail() throws Exception {
        // given
        final var jsonContent =
                """
                {
                    "id": "tutor-123",
                    "name": "John Doe",
                    "phone": "+1234567890",
                    "created_at": "2024-01-15T10:30:00Z",
                    "updated_at": "2024-01-15T11:45:00Z"
                }
                """;

        // when
        final var response = this.json.parse(jsonContent).getObject();

        // then
        assertThat(response.id()).isEqualTo("tutor-123");
        assertThat(response.name()).isEqualTo("John Doe");
        assertThat(response.email()).isNull();
        assertThat(response.phone()).isEqualTo("+1234567890");
        assertThat(response.createdAt()).isEqualTo(Instant.parse("2024-01-15T10:30:00Z"));
        assertThat(response.updatedAt()).isEqualTo(Instant.parse("2024-01-15T11:45:00Z"));
    }

    @Test
    void testSerializeWithNullPhone() throws Exception {
        // given
        final var createdAt = Instant.parse("2024-01-15T10:30:00Z");
        final var updatedAt = Instant.parse("2024-01-15T11:45:00Z");
        final var response =
                new TutorResponse(
                        "tutor-123", "John Doe", "john@example.com", null, createdAt, updatedAt);

        // when
        final var jsonContent = this.json.write(response);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.id").isEqualTo("tutor-123");
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.email")
                .isEqualTo("john@example.com");
        assertThat(jsonContent).doesNotHaveJsonPath("$.phone"); // null should not be rendered
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.created_at")
                .isEqualTo("2024-01-15T10:30:00Z");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.updated_at")
                .isEqualTo("2024-01-15T11:45:00Z");
    }

    @Test
    void testDeserializeWithNullPhone() throws Exception {
        // given
        final var jsonContent =
                """
                {
                    "id": "tutor-123",
                    "name": "John Doe",
                    "email": "john@example.com",
                    "created_at": "2024-01-15T10:30:00Z",
                    "updated_at": "2024-01-15T11:45:00Z"
                }
                """;

        // when
        final var response = this.json.parse(jsonContent).getObject();

        // then
        assertThat(response.id()).isEqualTo("tutor-123");
        assertThat(response.name()).isEqualTo("John Doe");
        assertThat(response.email()).isEqualTo("john@example.com");
        assertThat(response.phone()).isNull();
        assertThat(response.createdAt()).isEqualTo(Instant.parse("2024-01-15T10:30:00Z"));
        assertThat(response.updatedAt()).isEqualTo(Instant.parse("2024-01-15T11:45:00Z"));
    }

    @Test
    void testSerializeWithBothNullEmailAndPhone() throws Exception {
        // given
        final var createdAt = Instant.parse("2024-01-15T10:30:00Z");
        final var updatedAt = Instant.parse("2024-01-15T11:45:00Z");
        final var response =
                new TutorResponse("tutor-123", "John Doe", null, null, createdAt, updatedAt);

        // when
        final var jsonContent = this.json.write(response);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.id").isEqualTo("tutor-123");
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
        assertThat(jsonContent).doesNotHaveJsonPath("$.email");
        assertThat(jsonContent).doesNotHaveJsonPath("$.phone");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.created_at")
                .isEqualTo("2024-01-15T10:30:00Z");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.updated_at")
                .isEqualTo("2024-01-15T11:45:00Z");
    }
}
