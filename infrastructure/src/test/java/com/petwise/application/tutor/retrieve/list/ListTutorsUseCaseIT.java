package com.petwise.application.tutor.retrieve.list;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.petwise.IntegrationTest;
import com.petwise.domain.pagination.SearchQuery;
import com.petwise.domain.tutor.Tutor;
import com.petwise.infrastructure.tutor.persistence.TutorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/** Integration tests for ListTutorsUseCase. */
@IntegrationTest
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals",
    "PMD.TooManyMethods"
})
class ListTutorsUseCaseIT {
    ListTutorsUseCaseIT() {}

    @Autowired private ListTutorsUseCase useCase;
    @Autowired private TutorRepository tutorRepository;

    @Test
    void givenValidQuery_whenCallsListTutors_thenShouldReturnPaginatedTutors() {
        final var tutor1 = Tutor.newTutor("Alice Brown", "alice@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("Bob Green", "bob@example.com", "+2222222222");
        final var tutor3 = Tutor.newTutor("Charlie White", "charlie@example.com", "+3333333333");

        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor1));
        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor2));
        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor3));

        final var query = new SearchQuery(0, 10, "", "name", "asc");
        final var result = useCase.execute(query);
        assertThat(result).isNotNull();
        assertThat(result.currentPage()).isZero();
        assertThat(result.perPage()).isEqualTo(10);
        assertThat(result.total()).isEqualTo(3);
        assertThat(result.items()).hasSize(3);
        assertThat(result.items())
                .extracting(ListTutorsOutput::name)
                .containsExactly("Alice Brown", "Bob Green", "Charlie White");
    }

    @Test
    void givenValidQueryWithPagination_whenCallsListTutors_thenShouldReturnCorrectPage() {
        for (int i = 0; i < 15; i++) {
            final var tutor =
                    Tutor.newTutor(
                            "Tutor " + i,
                            "tutor" + i + "@example.com",
                            "+555000" + String.format("%04d", i));
            tutorRepository.save(
                    com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor));
        }

        final var query = new SearchQuery(1, 5, "", "name", "asc");
        final var result = useCase.execute(query);
        assertThat(result).isNotNull();
        assertThat(result.currentPage()).isEqualTo(1);
        assertThat(result.perPage()).isEqualTo(5);
        assertThat(result.total()).isEqualTo(15);
        assertThat(result.items()).hasSize(5);
    }

    @Test
    void givenValidQueryWithSearchTerms_whenCallsListTutors_thenShouldReturnFilteredTutors() {
        final var tutor1 = Tutor.newTutor("Maria Silva", "maria@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("João Silva", "joao@example.com", "+2222222222");
        final var tutor3 = Tutor.newTutor("Pedro Santos", "pedro@example.com", "+3333333333");

        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor1));
        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor2));
        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor3));

        final var query = new SearchQuery(0, 10, "Silva", "name", "asc");
        final var result = useCase.execute(query);
        assertThat(result).isNotNull();
        assertThat(result.total()).isEqualTo(2);
        assertThat(result.items()).hasSize(2);
        assertThat(result.items())
                .extracting(ListTutorsOutput::name)
                .containsExactlyInAnyOrder("Maria Silva", "João Silva");
    }

    @Test
    void givenValidQueryWithEmailSearch_whenCallsListTutors_thenShouldReturnFilteredTutors() {
        final var tutor1 = Tutor.newTutor("User One", "test@gmail.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("User Two", "test@yahoo.com", "+2222222222");
        final var tutor3 = Tutor.newTutor("User Three", "user@example.com", "+3333333333");

        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor1));
        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor2));
        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor3));

        final var query = new SearchQuery(0, 10, "test@", "name", "asc");
        final var result = useCase.execute(query);
        assertThat(result).isNotNull();
        assertThat(result.total()).isEqualTo(2);
        assertThat(result.items()).hasSize(2);
        assertThat(result.items())
                .extracting(ListTutorsOutput::email)
                .containsExactlyInAnyOrder("test@gmail.com", "test@yahoo.com");
    }

    @Test
    void givenValidQueryWithAscSorting_whenCallsListTutors_thenShouldReturnSortedTutors() {
        final var tutor1 = Tutor.newTutor("Charlie", "charlie@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("Alice", "alice@example.com", "+2222222222");
        final var tutor3 = Tutor.newTutor("Bob", "bob@example.com", "+3333333333");

        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor1));
        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor2));
        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor3));

        final var query = new SearchQuery(0, 10, "", "name", "asc");
        final var result = useCase.execute(query);
        assertThat(result.items())
                .extracting(ListTutorsOutput::name)
                .containsExactly("Alice", "Bob", "Charlie");
    }

    @Test
    void givenValidQueryWithDescSorting_whenCallsListTutors_thenShouldReturnSortedTutors() {
        final var tutor1 = Tutor.newTutor("Charlie", "charlie@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("Alice", "alice@example.com", "+2222222222");
        final var tutor3 = Tutor.newTutor("Bob", "bob@example.com", "+3333333333");

        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor1));
        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor2));
        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor3));

        final var query = new SearchQuery(0, 10, "", "name", "desc");
        final var result = useCase.execute(query);
        assertThat(result.items())
                .extracting(ListTutorsOutput::name)
                .containsExactly("Charlie", "Bob", "Alice");
    }

    @Test
    void givenEmptyDatabase_whenCallsListTutors_thenShouldReturnEmptyPagination() {
        final var query = new SearchQuery(0, 10, "", "name", "asc");
        final var result = useCase.execute(query);
        assertThat(result).isNotNull();
        assertThat(result.currentPage()).isZero();
        assertThat(result.perPage()).isEqualTo(10);
        assertThat(result.total()).isZero();
        assertThat(result.items()).isEmpty();
    }

    @Test
    void givenValidQueryWithNoMatch_whenCallsListTutors_thenShouldReturnEmptyPagination() {
        final var tutor = Tutor.newTutor("John Doe", "john@example.com", "+1234567890");
        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor));

        final var query = new SearchQuery(0, 10, "NonExistingTerm", "name", "asc");
        final var result = useCase.execute(query);
        assertThat(result).isNotNull();
        assertThat(result.total()).isZero();
        assertThat(result.items()).isEmpty();
    }

    @Test
    void givenNullQuery_whenCallsListTutors_thenShouldThrowNullPointerException() {
        assertThatThrownBy(() -> useCase.execute(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("SearchQuery cannot be null");
    }

    @Test
    void givenValidQueryWithNullTerms_whenCallsListTutors_thenShouldReturnAllTutors() {
        final var tutor1 = Tutor.newTutor("Tutor 1", "tutor1@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("Tutor 2", "tutor2@example.com", "+2222222222");

        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor1));
        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor2));

        final var query = new SearchQuery(0, 10, null, "name", "asc");
        final var result = useCase.execute(query);
        assertThat(result).isNotNull();
        assertThat(result.total()).isEqualTo(2);
        assertThat(result.items()).hasSize(2);
    }

    @Test
    void
            givenValidQueryWithCaseInsensitiveSearch_whenCallsListTutors_thenShouldReturnMatchingTutors() {
        final var tutor1 = Tutor.newTutor("UPPERCASE NAME", "uppercase@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("lowercase name", "lowercase@example.com", "+2222222222");
        final var tutor3 = Tutor.newTutor("MiXeD CaSe NaMe", "mixed@example.com", "+3333333333");

        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor1));
        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor2));
        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor3));

        final var query = new SearchQuery(0, 10, "name", "name", "asc");
        final var result = useCase.execute(query);
        assertThat(result).isNotNull();
        assertThat(result.total()).isEqualTo(3);
        assertThat(result.items()).hasSize(3);
    }

    @Test
    void givenValidQuery_whenCallsListTutors_thenShouldMapAllFieldsCorrectly() {
        final var expectedName = "Test User";
        final var expectedEmail = "test@example.com";
        final var expectedPhone = "+1234567890";

        final var tutor = Tutor.newTutor(expectedName, expectedEmail, expectedPhone);
        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor));

        final var query = new SearchQuery(0, 10, "", "name", "asc");
        final var result = useCase.execute(query);
        assertThat(result.items()).hasSize(1);
        final var output = result.items().get(0);
        assertThat(output.id()).isEqualTo(tutor.getId().getValue());
        assertThat(output.name()).isEqualTo(expectedName);
        assertThat(output.email()).isEqualTo(expectedEmail);
        assertThat(output.phone()).isEqualTo(expectedPhone);
        assertThat(output.createdAt()).isNotNull();
        assertThat(output.updatedAt()).isNotNull();
    }
}
