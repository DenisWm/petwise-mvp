package com.petwise.infrastructure.tutor.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.PostgresGatewayTest;
import com.petwise.domain.pagination.SearchQuery;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/** Integration tests for {@link TutorPostgresGateway}. */
@PostgresGatewayTest
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals",
    "PMD.TooManyMethods"
})
class TutorPostgresGatewayTest {
    TutorPostgresGatewayTest() {}

    @Autowired private TutorRepository tutorRepository;
    @Autowired private TutorPostgresGateway tutorGateway;

    @Test
    void givenValidTutor_whenSave_thenShouldPersistAndReturn() {
        final var tutor = Tutor.newTutor("John Doe", "john.doe@example.com", "+1234567890");
        final var result = tutorGateway.save(tutor);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isNotNull();
        assertThat(result.getEmail().getValue()).isEqualTo("john.doe@example.com");
        assertThat(result.getPhone()).isNotNull();
        assertThat(result.getPhone().getValue()).isEqualTo("+1234567890");
    }

    @Test
    void givenExistingTutor_whenFindById_thenShouldReturnTutor() {
        final var tutor = Tutor.newTutor("Jane Smith", "jane.smith@example.com", "+9876543210");
        final var saved = tutorGateway.save(tutor);
        final var result = tutorGateway.findById(saved.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(saved.getId());
        assertThat(result.get().getName()).isEqualTo("Jane Smith");
        assertThat(result.get().getEmail().getValue()).isEqualTo("jane.smith@example.com");
    }

    @Test
    void givenNonExistingTutor_whenFindById_thenShouldReturnEmpty() {
        final var result = tutorGateway.findById(TutorID.from("non-existing-id"));
        assertThat(result).isEmpty();
    }

    @Test
    void givenMultipleTutors_whenFindAll_thenShouldReturnAllTutors() {
        final var tutor1 = Tutor.newTutor("Alice Brown", "alice@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("Bob Green", "bob@example.com", "+2222222222");
        final var tutor3 = Tutor.newTutor("Charlie White", "charlie@example.com", "+3333333333");

        tutorGateway.save(tutor1);
        tutorGateway.save(tutor2);
        tutorGateway.save(tutor3);
        final var result = tutorGateway.findAll();
        assertThat(result).hasSize(3);
        assertThat(result)
                .extracting(Tutor::getName)
                .containsExactlyInAnyOrder("Alice Brown", "Bob Green", "Charlie White");
    }

    @Test
    void givenExistingTutor_whenDelete_thenShouldRemoveTutor() {
        final var tutor = Tutor.newTutor("Delete Me", "delete@example.com", "+9999999999");
        final var saved = tutorGateway.save(tutor);
        tutorGateway.deleteById(saved.getId());
        final var result = tutorGateway.findById(saved.getId());
        assertThat(result).isEmpty();
    }

    @Test
    void givenTutorsWithMatchingName_whenSearchWithTerms_thenShouldReturnMatchingTutors() {
        final var tutor1 = Tutor.newTutor("Maria Silva", "maria@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("João Silva", "joao@example.com", "+2222222222");
        final var tutor3 = Tutor.newTutor("Pedro Santos", "pedro@example.com", "+3333333333");

        tutorGateway.save(tutor1);
        tutorGateway.save(tutor2);
        tutorGateway.save(tutor3);

        final var query = new SearchQuery(0, 10, "Silva", "name", "asc");
        final var result = tutorGateway.findAll(query);
        assertThat(result.items()).hasSize(2);
        assertThat(result.items())
                .extracting(Tutor::getName)
                .containsExactlyInAnyOrder("Maria Silva", "João Silva");
        assertThat(result.total()).isEqualTo(2);
    }

    @Test
    void givenTutorsWithMatchingEmail_whenSearchWithTerms_thenShouldReturnMatchingTutors() {
        final var tutor1 = Tutor.newTutor("User One", "test@gmail.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("User Two", "test@yahoo.com", "+2222222222");
        final var tutor3 = Tutor.newTutor("User Three", "user@example.com", "+3333333333");

        tutorGateway.save(tutor1);
        tutorGateway.save(tutor2);
        tutorGateway.save(tutor3);

        final var query = new SearchQuery(0, 10, "test@", "name", "asc");
        final var result = tutorGateway.findAll(query);
        assertThat(result.items()).hasSize(2);
        assertThat(result.items())
                .extracting(t -> t.getEmail().getValue())
                .containsExactlyInAnyOrder("test@gmail.com", "test@yahoo.com");
    }

    @Test
    void givenTutors_whenSearchWithPagination_thenShouldRespectPagination() {
        for (int i = 0; i < 15; i++) {
            final var tutor =
                    Tutor.newTutor(
                            "Tutor " + i,
                            "tutor" + i + "@example.com",
                            "+555000" + String.format("%04d", i));
            tutorGateway.save(tutor);
        }

        final var query = new SearchQuery(1, 5, "Tutor", "name", "asc");
        final var result = tutorGateway.findAll(query);
        assertThat(result.items()).hasSize(5);
        assertThat(result.total()).isEqualTo(15);
        assertThat(result.currentPage()).isEqualTo(1);
        assertThat(result.perPage()).isEqualTo(5);
    }

    @Test
    void givenTutors_whenSearchCaseInsensitive_thenShouldMatchRegardlessOfCase() {
        final var tutor1 = Tutor.newTutor("UPPERCASE NAME", "uppercase@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("lowercase name", "lowercase@example.com", "+2222222222");
        final var tutor3 = Tutor.newTutor("MiXeD CaSe NaMe", "mixed@example.com", "+3333333333");

        tutorGateway.save(tutor1);
        tutorGateway.save(tutor2);
        tutorGateway.save(tutor3);

        final var query = new SearchQuery(0, 10, "name", "name", "asc");
        final var result = tutorGateway.findAll(query);
        assertThat(result.items()).hasSize(3);
        assertThat(result.items())
                .extracting(Tutor::getName)
                .containsExactlyInAnyOrder("UPPERCASE NAME", "lowercase name", "MiXeD CaSe NaMe");
    }

    @Test
    void givenTutors_whenSearchWithEmptyTerms_thenShouldReturnAll() {
        final var tutor1 = Tutor.newTutor("John Doe", "john@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("Jane Doe", "jane@example.com", "+2222222222");

        tutorGateway.save(tutor1);
        tutorGateway.save(tutor2);

        final var query = new SearchQuery(0, 10, "", "name", "asc");
        final var result = tutorGateway.findAll(query);
        assertThat(result.items()).hasSize(2);
    }

    @Test
    void givenTutors_whenSearchWithNullTerms_thenShouldReturnAll() {
        final var tutor1 = Tutor.newTutor("John Doe", "john@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("Jane Doe", "jane@example.com", "+2222222222");

        tutorGateway.save(tutor1);
        tutorGateway.save(tutor2);

        final var query = new SearchQuery(0, 10, null, "name", "asc");
        final var result = tutorGateway.findAll(query);
        assertThat(result.items()).hasSize(2);
    }

    @Test
    void givenTutors_whenSearchWithNoMatch_thenShouldReturnEmpty() {
        final var tutor1 = Tutor.newTutor("John Doe", "john@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("Jane Doe", "jane@example.com", "+2222222222");

        tutorGateway.save(tutor1);
        tutorGateway.save(tutor2);

        final var query = new SearchQuery(0, 10, "NonExistingTerm", "name", "asc");
        final var result = tutorGateway.findAll(query);
        assertThat(result.items()).isEmpty();
        assertThat(result.total()).isEqualTo(0);
    }

    @Test
    void givenTutorWithNullEmail_whenSave_thenShouldPersist() {
        final var tutor = Tutor.newTutor("No Email User", null, "+1234567890");
        final var result = tutorGateway.save(tutor);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("No Email User");
        assertThat(result.getEmail()).isNull();
    }

    @Test
    void givenTutorWithNullPhone_whenSave_thenShouldPersist() {
        final var tutor = Tutor.newTutor("No Phone User", "nophone@example.com", null);
        final var result = tutorGateway.save(tutor);
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("No Phone User");
        assertThat(result.getPhone()).isNull();
    }

    @Test
    void givenExistingTutor_whenUpdate_thenShouldUpdateFields() {
        final var tutor = Tutor.newTutor("Original Name", "original@example.com", "+1111111111");
        final var saved = tutorGateway.save(tutor);
        saved.update("Updated Name", "updated@example.com", "+9999999999");
        final var updated = tutorGateway.save(saved);
        final var result = tutorGateway.findById(updated.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Updated Name");
        assertThat(result.get().getEmail().getValue()).isEqualTo("updated@example.com");
        assertThat(result.get().getPhone().getValue()).isEqualTo("+9999999999");
    }

    @Test
    void givenTutors_whenSearchWithAscSorting_thenShouldReturnSortedResults() {
        final var tutor1 = Tutor.newTutor("Charlie", "charlie@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("Alice", "alice@example.com", "+2222222222");
        final var tutor3 = Tutor.newTutor("Bob", "bob@example.com", "+3333333333");

        tutorGateway.save(tutor1);
        tutorGateway.save(tutor2);
        tutorGateway.save(tutor3);

        final var query = new SearchQuery(0, 10, "", "name", "asc");
        final var result = tutorGateway.findAll(query);
        assertThat(result.items())
                .extracting(Tutor::getName)
                .containsExactly("Alice", "Bob", "Charlie");
    }

    @Test
    void givenTutors_whenSearchWithDescSorting_thenShouldReturnSortedResults() {
        final var tutor1 = Tutor.newTutor("Charlie", "charlie@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("Alice", "alice@example.com", "+2222222222");
        final var tutor3 = Tutor.newTutor("Bob", "bob@example.com", "+3333333333");

        tutorGateway.save(tutor1);
        tutorGateway.save(tutor2);
        tutorGateway.save(tutor3);

        final var query = new SearchQuery(0, 10, "", "name", "desc");
        final var result = tutorGateway.findAll(query);
        assertThat(result.items())
                .extracting(Tutor::getName)
                .containsExactly("Charlie", "Bob", "Alice");
    }
}
