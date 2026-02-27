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

    /** Default constructor. */
    UpdatePetRequestTest() {}

    /** The Jackson tester for UpdatePetRequest. */
    @Autowired private JacksonTester<UpdatePetRequest> json;

    @Test
    void testSerialize() throws Exception {
        // given
        final var request =
                new UpdatePetRequest(
                        "Fluffy", "Cat", "Persian", LocalDate.of(2020, 3, 15), "Updated notes");

        // when
        final var jsonContent = this.json.write(request);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Fluffy");
        assertThat(jsonContent).extractingJsonPathStringValue("$.species").isEqualTo("Cat");
        assertThat(jsonContent).extractingJsonPathStringValue("$.breed").isEqualTo("Persian");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.birth_date")
                .isEqualTo("2020-03-15");
        assertThat(jsonContent).extractingJsonPathStringValue("$.notes").isEqualTo("Updated notes");
    }

    @Test
    void testDeserialize() throws Exception {
        // given
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

        // when
        final var request = this.json.parse(jsonContent).getObject();

        // then
        assertThat(request.name()).isEqualTo("Fluffy");
        assertThat(request.species()).isEqualTo("Cat");
        assertThat(request.breed()).isEqualTo("Persian");
        assertThat(request.birthDate()).isEqualTo(LocalDate.of(2020, 3, 15));
        assertThat(request.notes()).isEqualTo("Updated notes");
    }

    @Test
    void testSerializeWithNullOptionalFields() throws Exception {
        // given
        final var request = new UpdatePetRequest("Rocky", null, null, null, null);

        // when
        final var jsonContent = this.json.write(request);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Rocky");
        assertThat(jsonContent).doesNotHaveJsonPath("$.species");
        assertThat(jsonContent).doesNotHaveJsonPath("$.breed");
        assertThat(jsonContent).doesNotHaveJsonPath("$.birth_date");
        assertThat(jsonContent).doesNotHaveJsonPath("$.notes");
    }

    @Test
    void testDeserializeWithMissingOptionalFields() throws Exception {
        // given
        final var jsonContent =
                """
                {
                    "name": "Rocky"
                }
                """;

        // when
        final var request = this.json.parse(jsonContent).getObject();

        // then
        assertThat(request.name()).isEqualTo("Rocky");
        assertThat(request.species()).isNull();
        assertThat(request.breed()).isNull();
        assertThat(request.birthDate()).isNull();
        assertThat(request.notes()).isNull();
    }
}
