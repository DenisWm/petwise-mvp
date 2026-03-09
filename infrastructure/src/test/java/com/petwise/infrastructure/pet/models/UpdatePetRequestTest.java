package com.petwise.infrastructure.pet.models;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.JacksonTest;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

/** Jackson serialisation tests for {@link UpdatePetRequest}. */
@JacksonTest
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals"
})
class UpdatePetRequestTest {
    UpdatePetRequestTest() {}

    @Autowired private JacksonTester<UpdatePetRequest> json;

    @Test
    void givenValidRequest_whenSerialize_thenShouldContainAllFields() throws Exception {
        final var request =
                new UpdatePetRequest(
                        "Fluffy", "Cat", "Persian", LocalDate.of(2020, 3, 15), "Updated notes");
        final var jsonContent = this.json.write(request);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Fluffy");
        assertThat(jsonContent).extractingJsonPathStringValue("$.species").isEqualTo("Cat");
        assertThat(jsonContent).extractingJsonPathStringValue("$.breed").isEqualTo("Persian");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.birth_date")
                .isEqualTo("2020-03-15");
        assertThat(jsonContent).extractingJsonPathStringValue("$.notes").isEqualTo("Updated notes");
    }

    @Test
    void givenValidJson_whenDeserialize_thenShouldMapAllFields() throws Exception {
        final var jsonContent =
                """
                {
                    "name": "Fluffy",
                    "species": "Cat",
                    "breed": "Persian",
                    "birth_date": "2020-03-15",
                    "notes": "Updated notes"
                }
                """;
        final var request = this.json.parse(jsonContent).getObject();
        assertThat(request.name()).isEqualTo("Fluffy");
        assertThat(request.species()).isEqualTo("Cat");
        assertThat(request.breed()).isEqualTo("Persian");
        assertThat(request.birthDate()).isEqualTo(LocalDate.of(2020, 3, 15));
        assertThat(request.notes()).isEqualTo("Updated notes");
    }

    @Test
    void givenRequestWithNullOptionals_whenSerialize_thenShouldOmitOptionals() throws Exception {
        final var request = new UpdatePetRequest("Rocky", null, null, null, null);
        final var jsonContent = this.json.write(request);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Rocky");
        assertThat(jsonContent).doesNotHaveJsonPath("$.species");
        assertThat(jsonContent).doesNotHaveJsonPath("$.breed");
        assertThat(jsonContent).doesNotHaveJsonPath("$.birth_date");
        assertThat(jsonContent).doesNotHaveJsonPath("$.notes");
    }

    @Test
    void givenJsonWithoutOptionals_whenDeserialize_thenShouldMapOptionalsAsNull() throws Exception {
        final var jsonContent =
                """
                {
                    "name": "Rocky"
                }
                """;
        final var request = this.json.parse(jsonContent).getObject();
        assertThat(request.name()).isEqualTo("Rocky");
        assertThat(request.species()).isNull();
        assertThat(request.breed()).isNull();
        assertThat(request.birthDate()).isNull();
        assertThat(request.notes()).isNull();
    }
}
