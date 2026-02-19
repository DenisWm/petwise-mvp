package com.petwise.infrastructure.tutor.models;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class UpdateTutorRequestTest {

    @Autowired private JacksonTester<UpdateTutorRequest> json;

    @Test
    void testSerialize() throws Exception {
        // given
        final var request =
                new UpdateTutorRequest("Jane Smith", "jane@example.com", "+9876543210");

        // when
        final var jsonContent = this.json.write(request);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Jane Smith");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.email")
                .isEqualTo("jane@example.com");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.phone")
                .isEqualTo("+9876543210");
    }

    @Test
    void testDeserialize() throws Exception {
        // given
        final var jsonContent =
                """
                {
                    "name": "Jane Smith",
                    "email": "jane@example.com",
                    "phone": "+9876543210"
                }
                """;

        // when
        final var request = this.json.parse(jsonContent).getObject();

        // then
        assertThat(request.name()).isEqualTo("Jane Smith");
        assertThat(request.email()).isEqualTo("jane@example.com");
        assertThat(request.phone()).isEqualTo("+9876543210");
    }

    @Test
    void testSerializeWithNullEmail() throws Exception {
        // given
        final var request = new UpdateTutorRequest("Jane Smith", null, "+9876543210");

        // when
        final var jsonContent = this.json.write(request);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Jane Smith");
        assertThat(jsonContent).doesNotHaveJsonPath("$.email"); // null should not be rendered
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.phone")
                .isEqualTo("+9876543210");
    }

    @Test
    void testDeserializeWithNullEmail() throws Exception {
        // given
        final var jsonContent =
                """
                {
                    "name": "Jane Smith",
                    "phone": "+9876543210"
                }
                """;

        // when
        final var request = this.json.parse(jsonContent).getObject();

        // then
        assertThat(request.name()).isEqualTo("Jane Smith");
        assertThat(request.email()).isNull();
        assertThat(request.phone()).isEqualTo("+9876543210");
    }

    @Test
    void testSerializeWithNullPhone() throws Exception {
        // given
        final var request = new UpdateTutorRequest("Jane Smith", "jane@example.com", null);

        // when
        final var jsonContent = this.json.write(request);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Jane Smith");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.email")
                .isEqualTo("jane@example.com");
        assertThat(jsonContent).doesNotHaveJsonPath("$.phone"); // null should not be rendered
    }

    @Test
    void testDeserializeWithNullPhone() throws Exception {
        // given
        final var jsonContent =
                """
                {
                    "name": "Jane Smith",
                    "email": "jane@example.com"
                }
                """;

        // when
        final var request = this.json.parse(jsonContent).getObject();

        // then
        assertThat(request.name()).isEqualTo("Jane Smith");
        assertThat(request.email()).isEqualTo("jane@example.com");
        assertThat(request.phone()).isNull();
    }

    @Test
    void testSerializeWithAllNullOptionalFields() throws Exception {
        // given
        final var request = new UpdateTutorRequest("Jane Smith", null, null);

        // when
        final var jsonContent = this.json.write(request);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Jane Smith");
        assertThat(jsonContent).doesNotHaveJsonPath("$.email");
        assertThat(jsonContent).doesNotHaveJsonPath("$.phone");
    }
}

