package com.petwise.application.tutor.delete;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.petwise.IntegrationTest;
import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.tutor.Tutor;
import com.petwise.infrastructure.tutor.persistence.TutorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/** Integration tests for DeleteTutorUseCase. */
@IntegrationTest
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate"
})
class DeleteTutorUseCaseIT {

    /** Default constructor. */
    DeleteTutorUseCaseIT() {}

    /** The use case under test. */
    @Autowired private DeleteTutorUseCase useCase;

    /** The tutor repository. */
    @Autowired private TutorRepository tutorRepository;

    @Test
    void givenValidId_whenCallsDeleteTutor_thenShouldDeleteTutor() {
        // given
        final var tutor = Tutor.newTutor("John Doe", "john@example.com", "+1234567890");
        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor));

        final var tutorId = tutor.getId().getValue();

        // Verify tutor exists
        assertThat(tutorRepository.findById(tutorId)).isPresent();

        // when
        useCase.execute(tutorId);

        // then
        assertThat(tutorRepository.findById(tutorId)).isEmpty();
    }

    @Test
    void givenNonExistingId_whenCallsDeleteTutor_thenShouldThrowNotFoundException() {
        // given
        final var nonExistingId = "non-existing-id";

        // when & then
        assertThatThrownBy(() -> useCase.execute(nonExistingId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Tutor with ID non-existing-id was not found");

        // Verify nothing was deleted
        assertThat(tutorRepository.count()).isZero();
    }

    @Test
    void givenNullId_whenCallsDeleteTutor_thenShouldThrowNullPointerException() {
        // when & then
        assertThatThrownBy(() -> useCase.execute(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Tutor ID cannot be null");
    }

    @Test
    void givenMultipleTutors_whenCallsDeleteOne_thenShouldDeleteOnlyTargetTutor() {
        // given
        final var tutor1 = Tutor.newTutor("Tutor 1", "tutor1@example.com", "+1111111111");
        final var tutor2 = Tutor.newTutor("Tutor 2", "tutor2@example.com", "+2222222222");
        final var tutor3 = Tutor.newTutor("Tutor 3", "tutor3@example.com", "+3333333333");

        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor1));
        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor2));
        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor3));

        assertThat(tutorRepository.count()).isEqualTo(3);

        // when
        useCase.execute(tutor2.getId().getValue());

        // then
        assertThat(tutorRepository.count()).isEqualTo(2);
        assertThat(tutorRepository.findById(tutor1.getId().getValue())).isPresent();
        assertThat(tutorRepository.findById(tutor2.getId().getValue())).isEmpty();
        assertThat(tutorRepository.findById(tutor3.getId().getValue())).isPresent();
    }

    @Test
    void givenValidId_whenCallsDeleteTutorTwice_thenShouldThrowNotFoundExceptionOnSecondCall() {
        // given
        final var tutor = Tutor.newTutor("Test User", "test@example.com", "+1234567890");
        tutorRepository.save(
                com.petwise.infrastructure.tutor.persistence.TutorJpaEntity.from(tutor));

        final var tutorId = tutor.getId().getValue();

        // when - first delete
        useCase.execute(tutorId);

        // then - second delete should fail
        assertThatThrownBy(() -> useCase.execute(tutorId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Tutor with ID " + tutorId + " was not found");
    }
}
