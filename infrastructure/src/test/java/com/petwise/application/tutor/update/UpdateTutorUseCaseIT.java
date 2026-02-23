package com.petwise.application.tutor.update;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.petwise.IntegrationTest;
import com.petwise.domain.exceptions.DomainException;
import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.exceptions.NotificationException;
import com.petwise.domain.tutor.Tutor;
import com.petwise.infrastructure.tutor.persistence.TutorJpaEntity;
import com.petwise.infrastructure.tutor.persistence.TutorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/** Integration tests for UpdateTutorUseCase. */
@IntegrationTest
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals",
    "PMD.DoNotUseThreads"
})
class UpdateTutorUseCaseIT {

    /** Default constructor. */
    UpdateTutorUseCaseIT() {}

    /** The use case under test. */
    @Autowired private UpdateTutorUseCase useCase;

    /** The tutor repository. */
    @Autowired private TutorRepository tutorRepository;

    @Test
    void givenValidCommand_whenCallsUpdateTutor_thenShouldUpdateTutor() {
        // given
        final var tutor = Tutor.newTutor("John Doe", "john@example.com", "+1234567890");
        tutorRepository.save(TutorJpaEntity.from(tutor));

        final var expectedName = "John Updated";
        final var expectedEmail = "john.updated@example.com";
        final var expectedPhone = "+9999999999";

        final var command =
                UpdateTutorCommand.with(
                        tutor.getId().getValue(), expectedName, expectedEmail, expectedPhone);

        // when
        final var output = useCase.execute(command);

        // then
        assertThat(output).isNotNull();
        assertThat(output.id()).isEqualTo(tutor.getId().getValue());

        // Verify persistence
        final var updatedTutor = tutorRepository.findById(tutor.getId().getValue());
        assertThat(updatedTutor).isPresent();
        assertThat(updatedTutor.get().getName()).isEqualTo(expectedName);
        assertThat(updatedTutor.get().getEmail()).isEqualTo(expectedEmail);
        assertThat(updatedTutor.get().getPhone()).isEqualTo(expectedPhone);
        assertThat(updatedTutor.get().getUpdatedAt())
                .isAfterOrEqualTo(updatedTutor.get().getCreatedAt());
    }

    @Test
    void givenValidCommandWithNullEmail_whenCallsUpdateTutor_thenShouldUpdateTutor() {
        // given
        final var tutor = Tutor.newTutor("Jane Smith", "jane@example.com", "+1111111111");
        tutorRepository.save(TutorJpaEntity.from(tutor));

        final var expectedName = "Jane Updated";
        final String expectedEmail = null;
        final var expectedPhone = "+2222222222";

        final var command =
                UpdateTutorCommand.with(
                        tutor.getId().getValue(), expectedName, expectedEmail, expectedPhone);

        // when
        final var output = useCase.execute(command);

        // then
        final var updatedTutor = tutorRepository.findById(output.id());
        assertThat(updatedTutor).isPresent();
        assertThat(updatedTutor.get().getName()).isEqualTo(expectedName);
        assertThat(updatedTutor.get().getEmail()).isNull();
        assertThat(updatedTutor.get().getPhone()).isEqualTo(expectedPhone);
    }

    @Test
    void givenValidCommandWithNullPhone_whenCallsUpdateTutor_thenShouldUpdateTutor() {
        // given
        final var tutor = Tutor.newTutor("Bob Johnson", "bob@example.com", "+3333333333");
        tutorRepository.save(TutorJpaEntity.from(tutor));

        final var expectedName = "Bob Updated";
        final var expectedEmail = "bob.updated@example.com";
        final String expectedPhone = null;

        final var command =
                UpdateTutorCommand.with(
                        tutor.getId().getValue(), expectedName, expectedEmail, expectedPhone);

        // when
        final var output = useCase.execute(command);

        // then
        final var updatedTutor = tutorRepository.findById(output.id());
        assertThat(updatedTutor).isPresent();
        assertThat(updatedTutor.get().getName()).isEqualTo(expectedName);
        assertThat(updatedTutor.get().getEmail()).isEqualTo(expectedEmail);
        assertThat(updatedTutor.get().getPhone()).isNull();
    }

    @Test
    void givenNonExistingId_whenCallsUpdateTutor_thenShouldThrowNotFoundException() {
        // given
        final var nonExistingId = "non-existing-id";
        final var command =
                UpdateTutorCommand.with(
                        nonExistingId, "Test Name", "test@example.com", "+1234567890");

        // when & then
        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Tutor with ID non-existing-id was not found");
    }

    @Test
    void givenInvalidCommandWithNullName_whenCallsUpdateTutor_thenShouldThrowDomainException() {
        // given
        final var tutor = Tutor.newTutor("Test User", "test@example.com", "+1234567890");
        tutorRepository.save(TutorJpaEntity.from(tutor));

        final String invalidName = null;
        final var command =
                UpdateTutorCommand.with(
                        tutor.getId().getValue(), invalidName, "test@example.com", "+1234567890");

        // when & then
        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(NotificationException.class)
                .hasMessageContaining("Could not update Aggregate Tutor");
    }

    @Test
    void givenInvalidCommandWithEmptyName_whenCallsUpdateTutor_thenShouldThrowDomainException() {
        // given
        final var tutor = Tutor.newTutor("Test User", "test@example.com", "+1234567890");
        tutorRepository.save(TutorJpaEntity.from(tutor));

        final var invalidName = "";
        final var command =
                UpdateTutorCommand.with(
                        tutor.getId().getValue(), invalidName, "test@example.com", "+1234567890");

        // when & then
        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(NotificationException.class)
                .hasMessageContaining("Could not update Aggregate Tutor");
    }

    @Test
    void givenInvalidCommandWithInvalidEmail_whenCallsUpdateTutor_thenShouldThrowDomainException() {
        // given
        final var tutor = Tutor.newTutor("Test User", "test@example.com", "+1234567890");
        tutorRepository.save(TutorJpaEntity.from(tutor));

        final var invalidEmail = "invalid-email";
        final var command =
                UpdateTutorCommand.with(
                        tutor.getId().getValue(), "Test User", invalidEmail, "+1234567890");

        // when & then
        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'email' is not a valid email address");
    }

    @Test
    void givenInvalidCommandWithInvalidPhone_whenCallsUpdateTutor_thenShouldThrowDomainException() {
        // given
        final var tutor = Tutor.newTutor("Test User", "test@example.com", "+1234567890");
        tutorRepository.save(TutorJpaEntity.from(tutor));

        final var invalidPhone = "123";
        final var command =
                UpdateTutorCommand.with(
                        tutor.getId().getValue(), "Test User", "test@example.com", invalidPhone);

        // when & then
        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'phone' is not a valid phone number");
    }

    @Test
    void givenNullCommand_whenCallsUpdateTutor_thenShouldThrowNullPointerException() {
        // when & then
        assertThatThrownBy(() -> useCase.execute(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Command cannot be null");
    }

    @Test
    void givenValidCommand_whenCallsUpdateTutor_thenShouldUpdateTimestamp() {
        // given
        final var tutor = Tutor.newTutor("Test User", "test@example.com", "+1234567890");
        final var savedEntity = tutorRepository.save(TutorJpaEntity.from(tutor));
        final var originalUpdatedAt = savedEntity.getUpdatedAt();

        // Small delay to ensure timestamp difference
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        final var command =
                UpdateTutorCommand.with(
                        tutor.getId().getValue(),
                        "Updated Name",
                        "updated@example.com",
                        "+9999999999");

        // when
        useCase.execute(command);

        // then
        final var updatedTutor = tutorRepository.findById(tutor.getId().getValue());
        assertThat(updatedTutor).isPresent();
        assertThat(updatedTutor.get().getUpdatedAt()).isAfterOrEqualTo(originalUpdatedAt);
    }
}
