package com.petwise.infrastructure.pet.models;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.JacksonTest;
import java.time.Instant;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

/** Jackson serialisation tests for {@link PetResponse}. */
@JacksonTest
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals"
})
class PetResponseTest {

    /** Default constructor. */
    PetResponseTest() {}

    /** The Jackson tester for PetResponse. */
    @Autowired private JacksonTester<PetResponse> json;

    @Test
    void givenValidResponse_whenSerialize_thenShouldContainAllFields() throws Exception {
        // given
        final var createdAt = Instant.parse("2024-01-15T10:30:00Z");
        final var updatedAt = Instant.parse("2024-01-15T11:45:00Z");
        final var response =
                new PetResponse(
                        "pet-123",
                        "tutor-456",
                        "Fluffy",
                        "Cat",
                        "Persian",
                        LocalDate.of(2020, 3, 15),
                        "Some notes",
                        createdAt,
                        updatedAt);

        // when
        final var jsonContent = this.json.write(response);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.id").isEqualTo("pet-123");
        assertThat(jsonContent).extractingJsonPathStringValue("$.tutor_id").isEqualTo("tutor-456");
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Fluffy");
        assertThat(jsonContent).extractingJsonPathStringValue("$.species").isEqualTo("Cat");
        assertThat(jsonContent).extractingJsonPathStringValue("$.breed").isEqualTo("Persian");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.birth_date")
                .isEqualTo("2020-03-15");
        assertThat(jsonContent).extractingJsonPathStringValue("$.notes").isEqualTo("Some notes");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.created_at")
                .isEqualTo("2024-01-15T10:30:00Z");
        assertThat(jsonContent)
                .extractingJsonPathStringValue("$.updated_at")
                .isEqualTo("2024-01-15T11:45:00Z");
    }

    @Test
    void givenValidJson_whenDeserialize_thenShouldMapAllFields() throws Exception {
        // given
        final var jsonContent =
                """
                {
                    "id": "pet-123",
                    "tutor_id": "tutor-456",
                    "name": "Fluffy",
                    "species": "Cat",
                    "breed": "Persian",
                    "birth_date": "2020-03-15",
                    "notes": "Some notes",
                    "created_at": "2024-01-15T10:30:00Z",
                    "updated_at": "2024-01-15T11:45:00Z"
                }
                """;

        // when
        final var response = this.json.parse(jsonContent).getObject();

        // then
        assertThat(response.id()).isEqualTo("pet-123");
        assertThat(response.tutorId()).isEqualTo("tutor-456");
        assertThat(response.name()).isEqualTo("Fluffy");
        assertThat(response.species()).isEqualTo("Cat");
        assertThat(response.breed()).isEqualTo("Persian");
        assertThat(response.birthDate()).isEqualTo(LocalDate.of(2020, 3, 15));
        assertThat(response.notes()).isEqualTo("Some notes");
        assertThat(response.createdAt()).isEqualTo(Instant.parse("2024-01-15T10:30:00Z"));
        assertThat(response.updatedAt()).isEqualTo(Instant.parse("2024-01-15T11:45:00Z"));
    }

    @Test
    void givenResponseWithNullOptionals_whenSerialize_thenShouldOmitOptionals() throws Exception {
        // given
        final var createdAt = Instant.parse("2024-01-15T10:30:00Z");
        final var updatedAt = Instant.parse("2024-01-15T11:45:00Z");
        final var response =
                new PetResponse(
                        "pet-123",
                        "tutor-456",
                        "Buddy",
                        null,
                        null,
                        null,
                        null,
                        createdAt,
                        updatedAt);

        // when
        final var jsonContent = this.json.write(response);

        // then
        assertThat(jsonContent).extractingJsonPathStringValue("$.id").isEqualTo("pet-123");
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Buddy");
        assertThat(jsonContent).doesNotHaveJsonPath("$.species");
        assertThat(jsonContent).doesNotHaveJsonPath("$.breed");
        assertThat(jsonContent).doesNotHaveJsonPath("$.birth_date");
        assertThat(jsonContent).doesNotHaveJsonPath("$.notes");
    }

    @Test
    void givenJsonWithoutOptionals_whenDeserialize_thenShouldMapOptionalsAsNull() throws Exception {
        // given
        final var jsonContent =
                """
                {
                    "id": "pet-123",
                    "tutor_id": "tutor-456",
                    "name": "Buddy",
                    "created_at": "2024-01-15T10:30:00Z",
                    "updated_at": "2024-01-15T11:45:00Z"
                }
                """;

        // when
        final var response = this.json.parse(jsonContent).getObject();

        // then
        assertThat(response.id()).isEqualTo("pet-123");
        assertThat(response.name()).isEqualTo("Buddy");
        assertThat(response.species()).isNull();
        assertThat(response.breed()).isNull();
        assertThat(response.birthDate()).isNull();
        assertThat(response.notes()).isNull();
    }
}
