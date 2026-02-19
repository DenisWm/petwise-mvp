package com.petwise.infrastructure.tutor.models;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class CreateTutorRequestTest {

    @Autowired private JacksonTester<CreateTutorRequest> json;

    @Test
    void testSerialize() throws Exception {
        // given
        final var request = new CreateTutorRequest("John Doe", "john@example.com", "+1234567890");

        // when
        final var jsonContent = this.json.write(request);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.email")
                .isEqualTo("john@example.com");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.phone")
                .isEqualTo("+1234567890");
    }

    @Test
    void testDeserialize() throws Exception {
        // given
        final var jsonContent =
                """
                {
                    "name": "John Doe",
                    "email": "john@example.com",
                    "phone": "+1234567890"
                }
                """;

        // when
        final var request = this.json.parse(jsonContent).getObject();

        // then
        assertThat(request.name()).isEqualTo("John Doe");
        assertThat(request.email()).isEqualTo("john@example.com");
        assertThat(request.phone()).isEqualTo("+1234567890");
    }

    @Test
    void testSerializeWithNullEmail() throws Exception {
        // given
        final var request = new CreateTutorRequest("John Doe", null, "+1234567890");

        // when
        final var jsonContent = this.json.write(request);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
        assertThat(jsonContent).doesNotHaveJsonPath("$.email"); // null should not be rendered
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.phone")
                .isEqualTo("+1234567890");
    }

    @Test
    void testDeserializeWithNullEmail() throws Exception {
        // given
        final var jsonContent =
                """
                {
                    "name": "John Doe",
                    "phone": "+1234567890"
                }
                """;

        // when
        final var request = this.json.parse(jsonContent).getObject();

        // then
        assertThat(request.name()).isEqualTo("John Doe");
        assertThat(request.email()).isNull();
        assertThat(request.phone()).isEqualTo("+1234567890");
    }

    @Test
    void testDeserializeWithExplicitNullEmail() throws Exception {
        // given
        final var jsonContent =
                """
                {
                    "name": "John Doe",
                    "email": null,
                    "phone": "+1234567890"
                }
                """;

        // when
        final var request = this.json.parse(jsonContent).getObject();

        // then
        assertThat(request.name()).isEqualTo("John Doe");
        assertThat(request.email()).isNull();
        assertThat(request.phone()).isEqualTo("+1234567890");
    }

    @Test
    void testSerializeWithNullPhone() throws Exception {
        // given
        final var request = new CreateTutorRequest("John Doe", "john@example.com", null);

        // when
        final var jsonContent = this.json.write(request);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.email")
                .isEqualTo("john@example.com");
        assertThat(jsonContent).doesNotHaveJsonPath("$.phone"); // null should not be rendered
    }

    @Test
    void testDeserializeWithNullPhone() throws Exception {
        // given
        final var jsonContent =
                """
                {
                    "name": "John Doe",
                    "email": "john@example.com"
                }
                """;

        // when
        final var request = this.json.parse(jsonContent).getObject();

        // then
        assertThat(request.name()).isEqualTo("John Doe");
        assertThat(request.email()).isEqualTo("john@example.com");
        assertThat(request.phone()).isNull();
    }
}

