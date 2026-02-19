package com.petwise.infrastructure.tutor.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.domain.pagination.SearchQuery;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorID;
import com.petwise.PostgresGatewayTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@PostgresGatewayTest
class TutorPostgresGatewayTest {

    @Autowired
    private TutorRepository tutorRepository;

    @Autowired
    private TutorPostgresGateway tutorGateway;


    @Test
    void givenValidTutor_whenSave_thenShouldPersistAndReturn() {
        // given
        final var tutor = Tutor.newTutor("John Doe", "john.doe@example.com", "+1234567890");

        // when
        final var result = tutorGateway.save(tutor);

        // then
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
        // given
        final var tutor = Tutor.newTutor("Jane Smith", "jane.smith@example.com", "+9876543210");
        final var saved = tutorGateway.save(tutor);

        // when
        final var result = tutorGateway.findById(saved.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(saved.getId());
        assertThat(result.get().getName()).isEqualTo("Jane Smith");
        assertThat(result.get().getEmail().getValue()).isEqualTo("jane.smith@example.com");
    }

    @Test
    void givenNonExistingTutor_whenFindById_thenShouldReturnEmpty() {
        // when
        final var result = tutorGateway.findById(TutorID.from("non-existing-id"));

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void givenMultipleTutors_whenFindAll_thenShouldReturnAllTutors() {
        // given
        final var tutor1 = Tutor.newTutor("Alice Brown", "alice@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("Bob Green", "bob@example.com", "+2222222222");
        final var tutor3 = Tutor.newTutor("Charlie White", "charlie@example.com", "+3333333333");

        tutorGateway.save(tutor1);
        tutorGateway.save(tutor2);
        tutorGateway.save(tutor3);

        // when
        final var result = tutorGateway.findAll();

        // then
        assertThat(result).hasSize(3);
        assertThat(result).extracting(Tutor::getName)
                .containsExactlyInAnyOrder("Alice Brown", "Bob Green", "Charlie White");
    }

    @Test
    void givenExistingTutor_whenDelete_thenShouldRemoveTutor() {
        // given
        final var tutor = Tutor.newTutor("Delete Me", "delete@example.com", "+9999999999");
        final var saved = tutorGateway.save(tutor);

        // when
        tutorGateway.deleteById(saved.getId());

        // then
        final var result = tutorGateway.findById(saved.getId());
        assertThat(result).isEmpty();
    }

    @Test
    void givenTutorsWithMatchingName_whenSearchWithTerms_thenShouldReturnMatchingTutors() {
        // given
        final var tutor1 = Tutor.newTutor("Maria Silva", "maria@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("João Silva", "joao@example.com", "+2222222222");
        final var tutor3 = Tutor.newTutor("Pedro Santos", "pedro@example.com", "+3333333333");

        tutorGateway.save(tutor1);
        tutorGateway.save(tutor2);
        tutorGateway.save(tutor3);

        final var query = new SearchQuery(0, 10, "Silva", "name", "asc");

        // when
        final var result = tutorGateway.findAll(query);

        // then
        assertThat(result.items()).hasSize(2);
        assertThat(result.items()).extracting(Tutor::getName)
                .containsExactlyInAnyOrder("Maria Silva", "João Silva");
        assertThat(result.total()).isEqualTo(2);
    }

    @Test
    void givenTutorsWithMatchingEmail_whenSearchWithTerms_thenShouldReturnMatchingTutors() {
        // given
        final var tutor1 = Tutor.newTutor("User One", "test@gmail.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("User Two", "test@yahoo.com", "+2222222222");
        final var tutor3 = Tutor.newTutor("User Three", "user@example.com", "+3333333333");

        tutorGateway.save(tutor1);
        tutorGateway.save(tutor2);
        tutorGateway.save(tutor3);

        final var query = new SearchQuery(0, 10, "test@", "name", "asc");

        // when
        final var result = tutorGateway.findAll(query);

        // then
        assertThat(result.items()).hasSize(2);
        assertThat(result.items()).extracting(t -> t.getEmail().getValue())
                .containsExactlyInAnyOrder("test@gmail.com", "test@yahoo.com");
    }

    @Test
    void givenTutors_whenSearchWithPagination_thenShouldRespectPagination() {
        // given
        for (int i = 0; i < 15; i++) {
            final var tutor = Tutor.newTutor(
                    "Tutor " + i,
                    "tutor" + i + "@example.com",
                    "+555000" + String.format("%04d", i));
            tutorGateway.save(tutor);
        }

        final var query = new SearchQuery(1, 5, "Tutor", "name", "asc");

        // when
        final var result = tutorGateway.findAll(query);

        // then
        assertThat(result.items()).hasSize(5);
        assertThat(result.total()).isEqualTo(15);
        assertThat(result.currentPage()).isEqualTo(1);
        assertThat(result.perPage()).isEqualTo(5);
    }

    @Test
    void givenTutors_whenSearchCaseInsensitive_thenShouldMatchRegardlessOfCase() {
        // given
        final var tutor1 = Tutor.newTutor("UPPERCASE NAME", "uppercase@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("lowercase name", "lowercase@example.com", "+2222222222");
        final var tutor3 = Tutor.newTutor("MiXeD CaSe NaMe", "mixed@example.com", "+3333333333");

        tutorGateway.save(tutor1);
        tutorGateway.save(tutor2);
        tutorGateway.save(tutor3);

        final var query = new SearchQuery(0, 10, "name", "name", "asc");

        // when
        final var result = tutorGateway.findAll(query);

        // then
        assertThat(result.items()).hasSize(3);
        assertThat(result.items()).extracting(Tutor::getName)
                .containsExactlyInAnyOrder("UPPERCASE NAME", "lowercase name", "MiXeD CaSe NaMe");
    }

    @Test
    void givenTutors_whenSearchWithEmptyTerms_thenShouldReturnAll() {
        // given
        final var tutor1 = Tutor.newTutor("John Doe", "john@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("Jane Doe", "jane@example.com", "+2222222222");

        tutorGateway.save(tutor1);
        tutorGateway.save(tutor2);

        final var query = new SearchQuery(0, 10, "", "name", "asc");

        // when
        final var result = tutorGateway.findAll(query);

        // then
        assertThat(result.items()).hasSize(2);
    }

    @Test
    void givenTutors_whenSearchWithNullTerms_thenShouldReturnAll() {
        // given
        final var tutor1 = Tutor.newTutor("John Doe", "john@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("Jane Doe", "jane@example.com", "+2222222222");

        tutorGateway.save(tutor1);
        tutorGateway.save(tutor2);

        final var query = new SearchQuery(0, 10, null, "name", "asc");

        // when
        final var result = tutorGateway.findAll(query);

        // then
        assertThat(result.items()).hasSize(2);
    }

    @Test
    void givenTutors_whenSearchWithNoMatch_thenShouldReturnEmpty() {
        // given
        final var tutor1 = Tutor.newTutor("John Doe", "john@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("Jane Doe", "jane@example.com", "+2222222222");

        tutorGateway.save(tutor1);
        tutorGateway.save(tutor2);

        final var query = new SearchQuery(0, 10, "NonExistingTerm", "name", "asc");

        // when
        final var result = tutorGateway.findAll(query);

        // then
        assertThat(result.items()).isEmpty();
        assertThat(result.total()).isEqualTo(0);
    }

    @Test
    void givenTutorWithNullEmail_whenSave_thenShouldPersist() {
        // given
        final var tutor = Tutor.newTutor("No Email User", null, "+1234567890");

        // when
        final var result = tutorGateway.save(tutor);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("No Email User");
        assertThat(result.getEmail()).isNull();
    }

    @Test
    void givenTutorWithNullPhone_whenSave_thenShouldPersist() {
        // given
        final var tutor = Tutor.newTutor("No Phone User", "nophone@example.com", null);

        // when
        final var result = tutorGateway.save(tutor);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("No Phone User");
        assertThat(result.getPhone()).isNull();
    }

    @Test
    void givenExistingTutor_whenUpdate_thenShouldUpdateFields() {
        // given
        final var tutor = Tutor.newTutor("Original Name", "original@example.com", "+1111111111");
        final var saved = tutorGateway.save(tutor);

        // when
        saved.update("Updated Name", "updated@example.com", "+9999999999");
        final var updated = tutorGateway.save(saved);

        // then
        final var result = tutorGateway.findById(updated.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Updated Name");
        assertThat(result.get().getEmail().getValue()).isEqualTo("updated@example.com");
        assertThat(result.get().getPhone().getValue()).isEqualTo("+9999999999");
    }

    @Test
    void givenTutors_whenSearchWithAscSorting_thenShouldReturnSortedResults() {
        // given
        final var tutor1 = Tutor.newTutor("Charlie", "charlie@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("Alice", "alice@example.com", "+2222222222");
        final var tutor3 = Tutor.newTutor("Bob", "bob@example.com", "+3333333333");

        tutorGateway.save(tutor1);
        tutorGateway.save(tutor2);
        tutorGateway.save(tutor3);

        final var query = new SearchQuery(0, 10, "", "name", "asc");

        // when
        final var result = tutorGateway.findAll(query);

        // then
        assertThat(result.items()).extracting(Tutor::getName)
                .containsExactly("Alice", "Bob", "Charlie");
    }

    @Test
    void givenTutors_whenSearchWithDescSorting_thenShouldReturnSortedResults() {
        // given
        final var tutor1 = Tutor.newTutor("Charlie", "charlie@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("Alice", "alice@example.com", "+2222222222");
        final var tutor3 = Tutor.newTutor("Bob", "bob@example.com", "+3333333333");

        tutorGateway.save(tutor1);
        tutorGateway.save(tutor2);
        tutorGateway.save(tutor3);

        final var query = new SearchQuery(0, 10, "", "name", "desc");

        // when
        final var result = tutorGateway.findAll(query);

        // then
        assertThat(result.items()).extracting(Tutor::getName)
                .containsExactly("Charlie", "Bob", "Alice");
    }
}

