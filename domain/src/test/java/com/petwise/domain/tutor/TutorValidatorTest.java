package com.petwise.domain.tutor;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.domain.UnitTest;
import com.petwise.domain.validation.handler.Notification;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link TutorValidator}. */
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals"
})
class TutorValidatorTest extends UnitTest {

    /** Default constructor. */
    TutorValidatorTest() {}

    @Test
    void givenValidTutor_whenValidate_thenShouldNotHaveErrors() {
        // given
        final var tutor = Tutor.newTutor("John Doe", "john@example.com", "+1234567890");
        final var notification = Notification.create();
        final var validator = new TutorValidator(tutor, notification);

        // when
        validator.validate();

        // then
        assertThat(notification.hasErrors()).isFalse();
        assertThat(notification.getErrors()).isEmpty();
    }

    @Test
    void givenNullName_whenValidate_thenShouldHaveError() {
        // given
        final var tutor = Tutor.newTutor("Valid", null, null);
        tutor.update(null, null, null);
        final var notification = Notification.create();
        final var validator = new TutorValidator(tutor, notification);

        // when
        validator.validate();

        // then
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(1);
        assertThat(notification.firstError().message()).isEqualTo("'name' should not be null");
    }

    @Test
    void givenEmptyName_whenValidate_thenShouldHaveError() {
        // given
        final var tutor = Tutor.newTutor("Valid", null, null);
        tutor.update("", null, null);
        final var notification = Notification.create();
        final var validator = new TutorValidator(tutor, notification);

        // when
        validator.validate();

        // then
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(1);
        assertThat(notification.firstError().message()).isEqualTo("'name' should not be empty");
    }

    @Test
    void givenBlankName_whenValidate_thenShouldHaveError() {
        // given
        final var tutor = Tutor.newTutor("Valid", null, null);
        tutor.update("   ", null, null);
        final var notification = Notification.create();
        final var validator = new TutorValidator(tutor, notification);

        // when
        validator.validate();

        // then
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(1);
        assertThat(notification.firstError().message()).isEqualTo("'name' should not be empty");
    }

    @Test
    void givenNameExceeding255Characters_whenValidate_thenShouldHaveError() {
        // given
        final var longName = "a".repeat(256);
        final var tutor = Tutor.newTutor("Valid", null, null);
        tutor.update(longName, null, null);
        final var notification = Notification.create();
        final var validator = new TutorValidator(tutor, notification);

        // when
        validator.validate();

        // then
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(1);
        assertThat(notification.firstError().message())
                .isEqualTo("'name' must be between 1 and 255 characters");
    }

    @Test
    void givenNameWith255Characters_whenValidate_thenShouldNotHaveError() {
        // given
        final var validLongName = "a".repeat(255);
        final var tutor = Tutor.newTutor(validLongName, null, null);
        final var notification = Notification.create();
        final var validator = new TutorValidator(tutor, notification);

        // when
        validator.validate();

        // then
        assertThat(notification.hasErrors()).isFalse();
        assertThat(notification.getErrors()).isEmpty();
    }

    @Test
    void givenNameWith1Character_whenValidate_thenShouldNotHaveError() {
        // given
        final var shortName = "A";
        final var tutor = Tutor.newTutor(shortName, null, null);
        final var notification = Notification.create();
        final var validator = new TutorValidator(tutor, notification);

        // when
        validator.validate();

        // then
        assertThat(notification.hasErrors()).isFalse();
        assertThat(notification.getErrors()).isEmpty();
    }

    @Test
    void givenTutorWithOptionalFields_whenValidate_thenShouldNotHaveErrors() {
        // given
        final var tutor = Tutor.newTutor("John Doe", null, null);
        final var notification = Notification.create();
        final var validator = new TutorValidator(tutor, notification);

        // when
        validator.validate();

        // then
        assertThat(notification.hasErrors()).isFalse();
        assertThat(notification.getErrors()).isEmpty();
    }
}
