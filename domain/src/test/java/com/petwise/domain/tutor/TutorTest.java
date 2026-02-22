package com.petwise.domain.tutor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.petwise.domain.UnitTest;
import com.petwise.domain.exceptions.DomainException;
import com.petwise.domain.validation.handler.Notification;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link Tutor}. */
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals",
    "PMD.LongVariable",
    "PMD.ShortVariable",
    "PMD.TooManyMethods"
})
class TutorTest extends UnitTest {

    /** Default constructor. */
    TutorTest() {}

    @Test
    void givenValidParams_whenCallsNewTutor_thenShouldInstantiateTutor() {
        // given
        final var expectedName = "John Doe";
        final var expectedEmail = "john.doe@example.com";
        final var expectedPhone = "+1 (555) 123-4567";

        // when
        final var actualTutor = Tutor.newTutor(expectedName, expectedEmail, expectedPhone);

        // then
        assertThat(actualTutor).isNotNull();
        assertThat(actualTutor.getId()).isNotNull();
        assertThat(actualTutor.getName()).isEqualTo(expectedName);
        assertThat(actualTutor.getEmail()).isNotNull();
        assertThat(actualTutor.getEmail().getValue()).isEqualTo(expectedEmail);
        assertThat(actualTutor.getPhone()).isNotNull();
        assertThat(actualTutor.getPhone().getValue()).isEqualTo(expectedPhone);
        assertThat(actualTutor.getCreatedAt()).isNotNull();
        assertThat(actualTutor.getUpdatedAt()).isNotNull();
    }

    @Test
    void givenValidParamsWithoutOptionals_whenCallsNewTutor_thenShouldInstantiateTutor() {
        // given
        final var expectedName = "Jane Smith";

        // when
        final var actualTutor = Tutor.newTutor(expectedName, null, null);

        // then
        assertThat(actualTutor).isNotNull();
        assertThat(actualTutor.getId()).isNotNull();
        assertThat(actualTutor.getName()).isEqualTo(expectedName);
        assertThat(actualTutor.getEmail()).isNull();
        assertThat(actualTutor.getPhone()).isNull();
        assertThat(actualTutor.getCreatedAt()).isNotNull();
        assertThat(actualTutor.getUpdatedAt()).isNotNull();
    }

    @Test
    void givenNullName_whenCallsNewTutorAndValidate_thenShouldReceiveError() {
        // given
        final String invalidName = null;
        final var expectedErrorMessage = "'name' should not be null";

        // when
        final var actualTutor = Tutor.newTutor(invalidName, null, null);
        final var notification = Notification.create();
        actualTutor.validate(notification);

        // then
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(1);
        assertThat(notification.firstError().message()).isEqualTo(expectedErrorMessage);
    }

    @Test
    void givenEmptyName_whenCallsNewTutorAndValidate_thenShouldReceiveError() {
        // given
        final var invalidName = "  ";
        final var expectedErrorMessage = "'name' should not be empty";

        // when
        final var actualTutor = Tutor.newTutor(invalidName, null, null);
        final var notification = Notification.create();
        actualTutor.validate(notification);

        // then
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(1);
        assertThat(notification.firstError().message()).isEqualTo(expectedErrorMessage);
    }

    @Test
    void givenNameExceeding255Characters_whenCallsNewTutorAndValidate_thenShouldReceiveError() {
        // given
        final var invalidName = "a".repeat(300);
        final var expectedErrorMessage = "'name' must be between 1 and 255 characters";

        // when
        final var actualTutor = Tutor.newTutor(invalidName, null, null);
        final var notification = Notification.create();
        actualTutor.validate(notification);

        // then
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.firstError().message()).isEqualTo(expectedErrorMessage);
    }

    @Test
    void givenInvalidEmail_whenCallsNewTutor_thenShouldThrowDomainException() {
        // given
        final var validName = "John Doe";
        final var invalidEmail = "invalid-email";

        // when & then
        assertThatThrownBy(() -> Tutor.newTutor(validName, invalidEmail, null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'email' is not a valid email address");
    }

    @Test
    void givenInvalidPhone_whenCallsNewTutor_thenShouldThrowDomainException() {
        // given
        final var validName = "John Doe";
        final var invalidPhone = "abc";

        // when & then
        assertThatThrownBy(() -> Tutor.newTutor(validName, null, invalidPhone))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'phone' is not a valid phone number");
    }

    @Test
    void givenValidTutor_whenCallsUpdate_thenShouldReturnUpdatedTutor() {
        // given
        final var expectedName = "John Updated";
        final var expectedEmail = "updated@example.com";
        final var expectedPhone = "+1 555-9999";
        final var tutor = Tutor.newTutor("John Doe", "john@example.com", "555-123-4567");
        final var createdAt = tutor.getCreatedAt();

        // when
        final var updatedTutor = tutor.update(expectedName, expectedEmail, expectedPhone);

        // then
        assertThat(updatedTutor).isNotNull();
        assertThat(updatedTutor.getName()).isEqualTo(expectedName);
        assertThat(updatedTutor.getEmail().getValue()).isEqualTo(expectedEmail);
        assertThat(updatedTutor.getPhone().getValue()).isEqualTo(expectedPhone);
        assertThat(updatedTutor.getCreatedAt()).isEqualTo(createdAt);
        assertThat(updatedTutor.getUpdatedAt()).isAfter(createdAt);
    }

    @Test
    void givenValidParams_whenCallsWith_thenShouldInstantiateTutor() {
        // given
        final var expectedId = "123456789";
        final var expectedName = "John Doe";
        final var expectedEmail = "john@example.com";
        final var expectedPhone = "555-123-4567";
        final var expectedCreatedAt = java.time.Instant.now();
        final var expectedUpdatedAt = java.time.Instant.now();

        // when
        final var actualTutor =
                Tutor.with(
                        expectedId,
                        expectedName,
                        expectedEmail,
                        expectedPhone,
                        expectedCreatedAt,
                        expectedUpdatedAt);

        // then
        assertThat(actualTutor).isNotNull();
        assertThat(actualTutor.getId().getValue()).isEqualTo(expectedId);
        assertThat(actualTutor.getName()).isEqualTo(expectedName);
        assertThat(actualTutor.getEmail().getValue()).isEqualTo(expectedEmail);
        assertThat(actualTutor.getPhone().getValue()).isEqualTo(expectedPhone);
        assertThat(actualTutor.getCreatedAt()).isEqualTo(expectedCreatedAt);
        assertThat(actualTutor.getUpdatedAt()).isEqualTo(expectedUpdatedAt);
    }

    @Test
    void givenTwoTutorsWithSameId_whenCompare_thenShouldBeEqual() {
        // given
        final var id = TutorID.unique();
        final var tutor1 =
                Tutor.with(
                        id, "John", null, null, java.time.Instant.now(), java.time.Instant.now());
        final var tutor2 =
                Tutor.with(
                        id, "Jane", null, null, java.time.Instant.now(), java.time.Instant.now());

        // when & then
        assertThat(tutor1).isEqualTo(tutor2);
    }

    @Test
    void givenTwoTutorsWithDifferentIds_whenCompare_thenShouldNotBeEqual() {
        // given
        final var tutor1 = Tutor.newTutor("John", null, null);
        final var tutor2 = Tutor.newTutor("John", null, null);

        // when & then
        assertThat(tutor1).isNotEqualTo(tutor2);
    }
}
