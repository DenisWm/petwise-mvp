package com.petwise.infrastructure.tutor.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.PostgresGatewayTest;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/** Integration tests for {@link TutorRepository}. */
@PostgresGatewayTest
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals",
    "PMD.TooManyMethods"
})
class TutorRepositoryTest {
    TutorRepositoryTest() {}

    @Autowired private TutorRepository tutorRepository;

    @Test
    void givenValidTutor_whenSave_thenShouldPersist() {
        final var entity = new TutorJpaEntity();
        entity.setId("tutor-123");
        entity.setName("John Doe");
        entity.setEmail("john.doe@example.com");
        entity.setPhone("+1234567890");
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        final var result = tutorRepository.save(entity);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("tutor-123");
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(result.getPhone()).isEqualTo("+1234567890");
    }

    @Test
    void givenExistingTutor_whenFindById_thenShouldReturnTutor() {
        final var entity = new TutorJpaEntity();
        entity.setId("tutor-456");
        entity.setName("Jane Smith");
        entity.setEmail("jane.smith@example.com");
        entity.setPhone("+9876543210");
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        tutorRepository.save(entity);
        final var result = tutorRepository.findById("tutor-456");
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo("tutor-456");
        assertThat(result.get().getName()).isEqualTo("Jane Smith");
        assertThat(result.get().getEmail()).isEqualTo("jane.smith@example.com");
    }

    @Test
    void givenNonExistingTutor_whenFindById_thenShouldReturnEmpty() {
        final var result = tutorRepository.findById("non-existing-id");
        assertThat(result).isEmpty();
    }

    @Test
    void givenMultipleTutors_whenFindAll_thenShouldReturnAllTutors() {
        final var entity1 =
                createTutorEntity("tutor-1", "Alice Brown", "alice@example.com", "+1111111111");
        final var entity2 =
                createTutorEntity("tutor-2", "Bob Green", "bob@example.com", "+2222222222");
        final var entity3 =
                createTutorEntity("tutor-3", "Charlie White", "charlie@example.com", "+3333333333");

        tutorRepository.save(entity1);
        tutorRepository.save(entity2);
        tutorRepository.save(entity3);
        final var result = tutorRepository.findAll();
        assertThat(result).hasSize(3);
        assertThat(result)
                .extracting(TutorJpaEntity::getName)
                .containsExactlyInAnyOrder("Alice Brown", "Bob Green", "Charlie White");
    }

    @Test
    void givenExistingTutor_whenDelete_thenShouldRemoveTutor() {
        final var entity =
                createTutorEntity("tutor-delete", "Delete Me", "delete@example.com", "+9999999999");
        tutorRepository.save(entity);
        tutorRepository.deleteById("tutor-delete");
        final var result = tutorRepository.findById("tutor-delete");
        assertThat(result).isEmpty();
    }

    @Test
    void givenTutorsWithMatchingName_whenFindBySearchTerms_thenShouldReturnMatchingTutors() {
        final var entity1 =
                createTutorEntity("tutor-1", "Maria Silva", "maria@example.com", "+1111111111");
        final var entity2 =
                createTutorEntity("tutor-2", "João Silva", "joao@example.com", "+2222222222");
        final var entity3 =
                createTutorEntity("tutor-3", "Pedro Santos", "pedro@example.com", "+3333333333");

        tutorRepository.save(entity1);
        tutorRepository.save(entity2);
        tutorRepository.save(entity3);

        final var pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        final var result = tutorRepository.findBySearchTerms("Silva", pageable);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
                .extracting(TutorJpaEntity::getName)
                .containsExactlyInAnyOrder("Maria Silva", "João Silva");
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    void givenTutorsWithMatchingEmail_whenFindBySearchTerms_thenShouldReturnMatchingTutors() {
        final var entity1 =
                createTutorEntity("tutor-1", "User One", "test@gmail.com", "+1111111111");
        final var entity2 =
                createTutorEntity("tutor-2", "User Two", "test@yahoo.com", "+2222222222");
        final var entity3 =
                createTutorEntity("tutor-3", "User Three", "user@example.com", "+3333333333");

        tutorRepository.save(entity1);
        tutorRepository.save(entity2);
        tutorRepository.save(entity3);

        final var pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        final var result = tutorRepository.findBySearchTerms("test@", pageable);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
                .extracting(TutorJpaEntity::getEmail)
                .containsExactlyInAnyOrder("test@gmail.com", "test@yahoo.com");
    }

    @Test
    void givenTutors_whenFindBySearchTermsWithPagination_thenShouldRespectPagination() {
        for (int i = 0; i < 15; i++) {
            final var entity =
                    createTutorEntity(
                            "tutor-" + i,
                            "Tutor " + i,
                            "tutor" + i + "@example.com",
                            "+555000" + String.format("%04d", i));
            tutorRepository.save(entity);
        }

        final var pageable = PageRequest.of(1, 5, Sort.by(Sort.Direction.ASC, "name"));
        final var result = tutorRepository.findBySearchTerms("Tutor", pageable);
        assertThat(result.getContent()).hasSize(5);
        assertThat(result.getTotalElements()).isEqualTo(15);
        assertThat(result.getTotalPages()).isEqualTo(3);
        assertThat(result.getNumber()).isEqualTo(1);
    }

    @Test
    void givenTutors_whenFindBySearchTermsCaseInsensitive_thenShouldMatchRegardlessOfCase() {
        final var entity1 =
                createTutorEntity(
                        "tutor-1", "UPPERCASE NAME", "uppercase@example.com", "+1111111111");
        final var entity2 =
                createTutorEntity(
                        "tutor-2", "lowercase name", "lowercase@example.com", "+2222222222");
        final var entity3 =
                createTutorEntity("tutor-3", "MiXeD CaSe NaMe", "mixed@example.com", "+3333333333");

        tutorRepository.save(entity1);
        tutorRepository.save(entity2);
        tutorRepository.save(entity3);

        final var pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        final var result = tutorRepository.findBySearchTerms("name", pageable);
        assertThat(result.getContent()).hasSize(3);
        assertThat(result.getContent())
                .extracting(TutorJpaEntity::getName)
                .containsExactlyInAnyOrder("UPPERCASE NAME", "lowercase name", "MiXeD CaSe NaMe");
    }

    @Test
    void givenTutors_whenFindBySearchTermsWithEmptyString_thenShouldNotMatch() {
        final var entity1 =
                createTutorEntity("tutor-1", "John Doe", "john@example.com", "+1111111111");
        final var entity2 =
                createTutorEntity("tutor-2", "Jane Doe", "jane@example.com", "+2222222222");

        tutorRepository.save(entity1);
        tutorRepository.save(entity2);

        final var pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        final var result = tutorRepository.findBySearchTerms("", pageable);
        // Empty string will match all due to LIKE '%%'
        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    void givenTutors_whenFindBySearchTermsWithNoMatch_thenShouldReturnEmpty() {
        final var entity1 =
                createTutorEntity("tutor-1", "John Doe", "john@example.com", "+1111111111");
        final var entity2 =
                createTutorEntity("tutor-2", "Jane Doe", "jane@example.com", "+2222222222");

        tutorRepository.save(entity1);
        tutorRepository.save(entity2);

        final var pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        final var result = tutorRepository.findBySearchTerms("NonExistingTerm", pageable);
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    void givenTutorWithNullEmail_whenSave_thenShouldPersist() {
        final var entity = new TutorJpaEntity();
        entity.setId("tutor-no-email");
        entity.setName("No Email User");
        entity.setEmail(null);
        entity.setPhone("+1234567890");
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        final var result = tutorRepository.save(entity);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("tutor-no-email");
        assertThat(result.getName()).isEqualTo("No Email User");
        assertThat(result.getEmail()).isNull();
    }

    @Test
    void givenTutorWithNullPhone_whenSave_thenShouldPersist() {
        final var entity = new TutorJpaEntity();
        entity.setId("tutor-no-phone");
        entity.setName("No Phone User");
        entity.setEmail("nophone@example.com");
        entity.setPhone(null);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        final var result = tutorRepository.save(entity);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("tutor-no-phone");
        assertThat(result.getName()).isEqualTo("No Phone User");
        assertThat(result.getPhone()).isNull();
    }

    @Test
    void givenExistingTutor_whenUpdate_thenShouldUpdateFields() {
        final var entity =
                createTutorEntity(
                        "tutor-update", "Original Name", "original@example.com", "+1111111111");
        tutorRepository.save(entity);
        entity.setName("Updated Name");
        entity.setEmail("updated@example.com");
        entity.setPhone("+9999999999");
        entity.setUpdatedAt(Instant.now());
        tutorRepository.save(entity);
        final var result = tutorRepository.findById("tutor-update");
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Updated Name");
        assertThat(result.get().getEmail()).isEqualTo("updated@example.com");
        assertThat(result.get().getPhone()).isEqualTo("+9999999999");
    }

    @Test
    void givenTutors_whenFindBySearchTermsWithSorting_thenShouldReturnSortedResults() {
        final var entity1 =
                createTutorEntity("tutor-1", "Charlie", "charlie@example.com", "+1111111111");
        final var entity2 =
                createTutorEntity("tutor-2", "Alice", "alice@example.com", "+2222222222");
        final var entity3 = createTutorEntity("tutor-3", "Bob", "bob@example.com", "+3333333333");

        tutorRepository.save(entity1);
        tutorRepository.save(entity2);
        tutorRepository.save(entity3);

        final var pageableAsc = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        final var pageableDesc = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "name"));
        final var resultAsc = tutorRepository.findBySearchTerms("", pageableAsc);
        final var resultDesc = tutorRepository.findBySearchTerms("", pageableDesc);
        assertThat(resultAsc.getContent())
                .extracting(TutorJpaEntity::getName)
                .containsExactly("Alice", "Bob", "Charlie");
        assertThat(resultDesc.getContent())
                .extracting(TutorJpaEntity::getName)
                .containsExactly("Charlie", "Bob", "Alice");
    }

    @SuppressWarnings({"PMD.ShortVariable", "PMD.MethodArgumentCouldBeFinal"})
    private TutorJpaEntity createTutorEntity(
            final String id, final String name, final String email, final String phone) {
        final var entity = new TutorJpaEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setEmail(email);
        entity.setPhone(phone);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        return entity;
    }
}
