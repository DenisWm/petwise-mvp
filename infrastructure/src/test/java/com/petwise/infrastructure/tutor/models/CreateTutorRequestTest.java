package com.petwise.infrastructure.tutor.models;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

/** Jackson serialisation tests for {@link CreateTutorRequest}. */
@JacksonTest
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals"
})
class CreateTutorRequestTest {
    CreateTutorRequestTest() {}

    @Autowired private JacksonTester<CreateTutorRequest> json;

    @Test
    void givenValidRequest_whenSerialize_thenShouldContainAllFields() throws Exception {
        final var request = new CreateTutorRequest("John Doe", "john@example.com", "+1234567890");
        final var jsonContent = this.json.write(request);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.email")
                .isEqualTo("john@example.com");
        assertThat(jsonContent).extractingJsonPathStringValue("$.phone").isEqualTo("+1234567890");
    }

    @Test
    void givenValidJson_whenDeserialize_thenShouldMapAllFields() throws Exception {
        final var jsonContent =
                """
                {
                    "name": "John Doe",
                    "email": "john@example.com",
                    "phone": "+1234567890"
                }
                """;
        final var request = this.json.parse(jsonContent).getObject();
        assertThat(request.name()).isEqualTo("John Doe");
        assertThat(request.email()).isEqualTo("john@example.com");
        assertThat(request.phone()).isEqualTo("+1234567890");
    }

    @Test
    void givenRequestWithNullEmail_whenSerialize_thenShouldOmitEmail() throws Exception {
        final var request = new CreateTutorRequest("John Doe", null, "+1234567890");
        final var jsonContent = this.json.write(request);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
        assertThat(jsonContent).doesNotHaveJsonPath("$.email"); // null should not be rendered
        assertThat(jsonContent).extractingJsonPathStringValue("$.phone").isEqualTo("+1234567890");
    }

    @Test
    void givenJsonWithoutEmail_whenDeserialize_thenShouldMapEmailAsNull() throws Exception {
        final var jsonContent =
                """
                {
                    "name": "John Doe",
                    "phone": "+1234567890"
                }
                """;
        final var request = this.json.parse(jsonContent).getObject();
        assertThat(request.name()).isEqualTo("John Doe");
        assertThat(request.email()).isNull();
        assertThat(request.phone()).isEqualTo("+1234567890");
    }

    @Test
    void givenJsonWithExplicitNullEmail_whenDeserialize_thenShouldMapEmailAsNull()
            throws Exception {
        final var jsonContent =
                """
                {
                    "name": "John Doe",
                    "email": null,
                    "phone": "+1234567890"
                }
                """;
        final var request = this.json.parse(jsonContent).getObject();
        assertThat(request.name()).isEqualTo("John Doe");
        assertThat(request.email()).isNull();
        assertThat(request.phone()).isEqualTo("+1234567890");
    }

    @Test
    void givenRequestWithNullPhone_whenSerialize_thenShouldOmitPhone() throws Exception {
        final var request = new CreateTutorRequest("John Doe", "john@example.com", null);
        final var jsonContent = this.json.write(request);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.email")
                .isEqualTo("john@example.com");
        assertThat(jsonContent).doesNotHaveJsonPath("$.phone"); // null should not be rendered
    }

    @Test
    void givenJsonWithoutPhone_whenDeserialize_thenShouldMapPhoneAsNull() throws Exception {
        final var jsonContent =
                """
                {
                    "name": "John Doe",
                    "email": "john@example.com"
                }
                """;
        final var request = this.json.parse(jsonContent).getObject();
        assertThat(request.name()).isEqualTo("John Doe");
        assertThat(request.email()).isEqualTo("john@example.com");
        assertThat(request.phone()).isNull();
    }
}
