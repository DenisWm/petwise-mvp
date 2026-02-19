package com.petwise.application.tutor.create;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.petwise.IntegrationTest;
import com.petwise.domain.exceptions.DomainException;
import com.petwise.domain.exceptions.NotificationException;
import com.petwise.infrastructure.tutor.persistence.TutorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class CreateTutorUseCaseIT {

    @Autowired private CreateTutorUseCase useCase;

    @Autowired private TutorRepository tutorRepository;

    @Test
    void givenValidCommand_whenCallsCreateTutor_thenShouldReturnTutorId() {
        // given
        final var expectedName = "John Doe";
        final var expectedEmail = "john.doe@example.com";
        final var expectedPhone = "+1234567890";

        final var command = CreateTutorCommand.with(expectedName, expectedEmail, expectedPhone);

        // when
        final var output = useCase.execute(command);

        // then
        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();

        // Verify persistence
        final var persistedTutor = tutorRepository.findById(output.id());
        assertThat(persistedTutor).isPresent();
        assertThat(persistedTutor.get().getId()).isEqualTo(output.id());
        assertThat(persistedTutor.get().getName()).isEqualTo(expectedName);
        assertThat(persistedTutor.get().getEmail()).isEqualTo(expectedEmail);
        assertThat(persistedTutor.get().getPhone()).isEqualTo(expectedPhone);
        assertThat(persistedTutor.get().getCreatedAt()).isNotNull();
        assertThat(persistedTutor.get().getUpdatedAt()).isNotNull();
    }

    @Test
    void givenValidCommandWithNullEmail_whenCallsCreateTutor_thenShouldReturnTutorId() {
        // given
        final var expectedName = "Jane Smith";
        final String expectedEmail = null;
        final var expectedPhone = "+9876543210";

        final var command = CreateTutorCommand.with(expectedName, expectedEmail, expectedPhone);

        // when
        final var output = useCase.execute(command);

        // then
        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();

        // Verify persistence
        final var persistedTutor = tutorRepository.findById(output.id());
        assertThat(persistedTutor).isPresent();
        assertThat(persistedTutor.get().getName()).isEqualTo(expectedName);
        assertThat(persistedTutor.get().getEmail()).isNull();
        assertThat(persistedTutor.get().getPhone()).isEqualTo(expectedPhone);
    }

    @Test
    void givenValidCommandWithNullPhone_whenCallsCreateTutor_thenShouldReturnTutorId() {
        // given
        final var expectedName = "Bob Johnson";
        final var expectedEmail = "bob.johnson@example.com";
        final String expectedPhone = null;

        final var command = CreateTutorCommand.with(expectedName, expectedEmail, expectedPhone);

        // when
        final var output = useCase.execute(command);

        // then
        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();

        // Verify persistence
        final var persistedTutor = tutorRepository.findById(output.id());
        assertThat(persistedTutor).isPresent();
        assertThat(persistedTutor.get().getName()).isEqualTo(expectedName);
        assertThat(persistedTutor.get().getEmail()).isEqualTo(expectedEmail);
        assertThat(persistedTutor.get().getPhone()).isNull();
    }

    @Test
    void givenValidCommandWithEmptyEmail_whenCallsCreateTutor_thenShouldReturnTutorId() {
        // given
        final var expectedName = "Alice Brown";
        final var expectedEmail = "";
        final var expectedPhone = "+1111111111";

        final var command = CreateTutorCommand.with(expectedName, expectedEmail, expectedPhone);

        // when
        final var output = useCase.execute(command);

        // then
        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();

        // Verify persistence - empty string is treated as null
        final var persistedTutor = tutorRepository.findById(output.id());
        assertThat(persistedTutor).isPresent();
        assertThat(persistedTutor.get().getName()).isEqualTo(expectedName);
        assertThat(persistedTutor.get().getEmail()).isNull();
    }

    @Test
    void givenValidCommandWithEmptyPhone_whenCallsCreateTutor_thenShouldReturnTutorId() {
        // given
        final var expectedName = "Charlie Davis";
        final var expectedEmail = "charlie@example.com";
        final var expectedPhone = "";

        final var command = CreateTutorCommand.with(expectedName, expectedEmail, expectedPhone);

        // when
        final var output = useCase.execute(command);

        // then
        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();

        // Verify persistence - empty string is treated as null
        final var persistedTutor = tutorRepository.findById(output.id());
        assertThat(persistedTutor).isPresent();
        assertThat(persistedTutor.get().getName()).isEqualTo(expectedName);
        assertThat(persistedTutor.get().getPhone()).isNull();
    }

    @Test
    void givenValidCommandWithBothNullEmailAndPhone_whenCallsCreateTutor_thenShouldReturnTutorId() {
        // given
        final var expectedName = "Diana Evans";
        final String expectedEmail = null;
        final String expectedPhone = null;

        final var command = CreateTutorCommand.with(expectedName, expectedEmail, expectedPhone);

        // when
        final var output = useCase.execute(command);

        // then
        assertThat(output).isNotNull();
        assertThat(output.id()).isNotNull();

        // Verify persistence
        final var persistedTutor = tutorRepository.findById(output.id());
        assertThat(persistedTutor).isPresent();
        assertThat(persistedTutor.get().getName()).isEqualTo(expectedName);
        assertThat(persistedTutor.get().getEmail()).isNull();
        assertThat(persistedTutor.get().getPhone()).isNull();
    }

    @Test
    void
            givenInvalidCommandWithNullName_whenCallsCreateTutor_thenShouldThrowNotificationException() {
        // given
        final String expectedName = null;
        final var expectedEmail = "test@example.com";
        final var expectedPhone = "+1234567890";

        final var command = CreateTutorCommand.with(expectedName, expectedEmail, expectedPhone);

        // when & then
        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(NotificationException.class)
                .hasMessageContaining("Could not create Aggregate Tutor");

        // Verify nothing was persisted
        assertThat(tutorRepository.count()).isZero();
    }

    @Test
    void
            givenInvalidCommandWithEmptyName_whenCallsCreateTutor_thenShouldThrowNotificationException() {
        // given
        final var expectedName = "";
        final var expectedEmail = "test@example.com";
        final var expectedPhone = "+1234567890";

        final var command = CreateTutorCommand.with(expectedName, expectedEmail, expectedPhone);

        // when & then
        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(NotificationException.class)
                .hasMessageContaining("Could not create Aggregate Tutor");

        // Verify nothing was persisted
        assertThat(tutorRepository.count()).isZero();
    }

    @Test
    void
            givenInvalidCommandWithBlankName_whenCallsCreateTutor_thenShouldThrowNotificationException() {
        // given
        final var expectedName = "   ";
        final var expectedEmail = "test@example.com";
        final var expectedPhone = "+1234567890";

        final var command = CreateTutorCommand.with(expectedName, expectedEmail, expectedPhone);

        // when & then
        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(NotificationException.class)
                .hasMessageContaining("Could not create Aggregate Tutor");

        // Verify nothing was persisted
        assertThat(tutorRepository.count()).isZero();
    }

    @Test
    void givenInvalidCommandWithInvalidEmail_whenCallsCreateTutor_thenShouldThrowDomainException() {
        // given
        final var expectedName = "Invalid Email User";
        final var expectedEmail = "invalid-email";
        final var expectedPhone = "+1234567890";

        final var command = CreateTutorCommand.with(expectedName, expectedEmail, expectedPhone);

        // when & then
        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'email' is not a valid email address");

        // Verify nothing was persisted
        assertThat(tutorRepository.count()).isZero();
    }

    @Test
    void
            givenInvalidCommandWithInvalidPhoneFormat_whenCallsCreateTutor_thenShouldThrowDomainException() {
        // given
        final var expectedName = "Invalid Phone User";
        final var expectedEmail = "test@example.com";
        final var expectedPhone = "123"; // Too short

        final var command = CreateTutorCommand.with(expectedName, expectedEmail, expectedPhone);

        // when & then
        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'phone' is not a valid phone number");

        // Verify nothing was persisted
        assertThat(tutorRepository.count()).isZero();
    }

    @Test
    void givenNullCommand_whenCallsCreateTutor_thenShouldThrowNullPointerException() {
        // when & then
        assertThatThrownBy(() -> useCase.execute(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Command cannot be null");
    }

    @Test
    void givenValidCommand_whenCallsCreateTutor_thenShouldSetTimestamps() {
        // given
        final var command = CreateTutorCommand.with("Test User", "test@example.com", "+1234567890");

        // when
        final var output = useCase.execute(command);

        // then
        final var persistedTutor = tutorRepository.findById(output.id());
        assertThat(persistedTutor).isPresent();
        assertThat(persistedTutor.get().getCreatedAt()).isNotNull();
        assertThat(persistedTutor.get().getUpdatedAt()).isNotNull();
        assertThat(persistedTutor.get().getCreatedAt())
                .isEqualTo(persistedTutor.get().getUpdatedAt());
    }

    @Test
    void givenMultipleValidCommands_whenCallsCreateTutor_thenShouldCreateMultipleTutors() {
        // given
        final var command1 =
                CreateTutorCommand.with("First User", "first@example.com", "+1111111111");
        final var command2 =
                CreateTutorCommand.with("Second User", "second@example.com", "+2222222222");
        final var command3 =
                CreateTutorCommand.with("Third User", "third@example.com", "+3333333333");

        // when
        final var output1 = useCase.execute(command1);
        final var output2 = useCase.execute(command2);
        final var output3 = useCase.execute(command3);

        // then
        assertThat(output1.id()).isNotNull();
        assertThat(output2.id()).isNotNull();
        assertThat(output3.id()).isNotNull();

        // Verify all IDs are unique
        assertThat(output1.id()).isNotEqualTo(output2.id());
        assertThat(output2.id()).isNotEqualTo(output3.id());
        assertThat(output1.id()).isNotEqualTo(output3.id());

        // Verify all were persisted
        assertThat(tutorRepository.count()).isEqualTo(3);
    }
}
