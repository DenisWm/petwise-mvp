package com.petwise.infrastructure.tutor.models;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.JacksonTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

/** Jackson serialisation tests for {@link UpdateTutorRequest}. */
@JacksonTest
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals"
})
class UpdateTutorRequestTest {

    /** Default constructor. */
    UpdateTutorRequestTest() {}

    /** The Jackson tester for UpdateTutorRequest. */
    @Autowired private JacksonTester<UpdateTutorRequest> json;

    @Test
    void givenValidRequest_whenSerialize_thenShouldContainAllFields() throws Exception {
        // given
        final var request = new UpdateTutorRequest("Jane Smith", "jane@example.com", "+9876543210");

        // when
        final var jsonContent = this.json.write(request);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Jane Smith");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.email")
                .isEqualTo("jane@example.com");
        assertThat(jsonContent).extractingJsonPathStringValue("$.phone").isEqualTo("+9876543210");
    }

    @Test
    void givenValidJson_whenDeserialize_thenShouldMapAllFields() throws Exception {
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
    void givenRequestWithNullEmail_whenSerialize_thenShouldOmitEmail() throws Exception {
        // given
        final var request = new UpdateTutorRequest("Jane Smith", null, "+9876543210");

        // when
        final var jsonContent = this.json.write(request);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Jane Smith");
        assertThat(jsonContent).doesNotHaveJsonPath("$.email"); // null should not be rendered
        assertThat(jsonContent).extractingJsonPathStringValue("$.phone").isEqualTo("+9876543210");
    }

    @Test
    void givenJsonWithoutEmail_whenDeserialize_thenShouldMapEmailAsNull() throws Exception {
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
    void givenRequestWithNullPhone_whenSerialize_thenShouldOmitPhone() throws Exception {
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
    void givenJsonWithoutPhone_whenDeserialize_thenShouldMapPhoneAsNull() throws Exception {
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
    void givenRequestWithAllNullOptionals_whenSerialize_thenShouldOmitAll() throws Exception {
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
