package com.petwise.infrastructure.tutor.models;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest
public class TutorIdResponseTest {

    @Autowired private JacksonTester<TutorIdResponse> json;

    @Test
    void testSerialize() throws Exception {
        // given
        final var response = new TutorIdResponse("tutor-123");

        // when
        final var jsonContent = this.json.write(response);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.id").isEqualTo("tutor-123");
    }

    @Test
    void testDeserialize() throws Exception {
        // given
        final var jsonContent =
                """
                {
                    "id": "tutor-123"
                }
                """;

        // when
        final var response = this.json.parse(jsonContent).getObject();

        // then
        assertThat(response.id()).isEqualTo("tutor-123");
    }

    @Test
    void testSerializeWithLongId() throws Exception {
        // given
        final var response = new TutorIdResponse("tutor-abc123-def456-ghi789");

        // when
        final var jsonContent = this.json.write(response);

        // then
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.id")
                .isEqualTo("tutor-abc123-def456-ghi789");
    }

    @Test
    void testDeserializeWithLongId() throws Exception {
        // given
        final var jsonContent =
                """
                {
                    "id": "tutor-abc123-def456-ghi789"
                }
                """;

        // when
        final var response = this.json.parse(jsonContent).getObject();

        // then
        assertThat(response.id()).isEqualTo("tutor-abc123-def456-ghi789");
    }
}

