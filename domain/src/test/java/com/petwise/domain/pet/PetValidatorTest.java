package com.petwise.domain.pet;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.domain.UnitTest;
import com.petwise.domain.tutor.TutorID;
import com.petwise.domain.validation.handler.Notification;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link PetValidator}. */
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals",
    "PMD.TooManyMethods"
})
class PetValidatorTest extends UnitTest {

    /** Default constructor. */
    PetValidatorTest() {}

    @Test
    void givenValidPet_whenValidate_thenShouldNotHaveErrors() {
        // given
        final var pet =
                Pet.newPet(
                        TutorID.unique(),
                        "Fluffy",
                        "Cat",
                        "Persian",
                        LocalDate.of(2020, 3, 15),
                        "Notes");
        final var notification = Notification.create();
        final var validator = new PetValidator(pet, notification);

        // when
        validator.validate();

        // then
        assertThat(notification.hasErrors()).isFalse();
        assertThat(notification.getErrors()).isEmpty();
    }

    @Test
    void givenNullName_whenValidate_thenShouldHaveError() {
        // given
        final var pet = Pet.newPet(TutorID.unique(), "Valid", null, null, null, null);
        pet.update(null, null, null, null, null);
        final var notification = Notification.create();
        final var validator = new PetValidator(pet, notification);

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
        final var pet = Pet.newPet(TutorID.unique(), "Valid", null, null, null, null);
        pet.update("", null, null, null, null);
        final var notification = Notification.create();
        final var validator = new PetValidator(pet, notification);

        // when
        validator.validate();

        // then
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(1);
        assertThat(notification.firstError().message()).isEqualTo("'name' should not be empty");
    }

    @Test
    void givenNullTutorId_whenValidate_thenShouldHaveError() {
        // given
        final var pet = Pet.newPet(null, "Valid", null, null, null, null);
        final var notification = Notification.create();
        final var validator = new PetValidator(pet, notification);

        // when
        validator.validate();

        // then
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(1);
        assertThat(notification.firstError().message()).isEqualTo("'tutorId' should not be null");
    }

    @Test
    void givenFutureBirthDate_whenValidate_thenShouldHaveError() {
        // given
        final var pet =
                Pet.newPet(
                        TutorID.unique(), "Valid", null, null, LocalDate.now().plusDays(1), null);
        final var notification = Notification.create();
        final var validator = new PetValidator(pet, notification);

        // when
        validator.validate();

        // then
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(1);
        assertThat(notification.firstError().message())
                .isEqualTo("'birthDate' must not be in the future");
    }

    @Test
    void givenBlankSpecies_whenValidate_thenShouldHaveError() {
        // given
        final var pet = Pet.newPet(TutorID.unique(), "Buddy", "  ", null, null, null);
        final var notification = Notification.create();

        // when
        new PetValidator(pet, notification).validate();

        // then
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.firstError().message()).isEqualTo("'species' should not be empty");
    }

    @Test
    void givenBlankBreed_whenValidate_thenShouldHaveError() {
        // given
        final var pet = Pet.newPet(TutorID.unique(), "Buddy", null, "  ", null, null);
        final var notification = Notification.create();

        // when
        new PetValidator(pet, notification).validate();

        // then
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.firstError().message()).isEqualTo("'breed' should not be empty");
    }

    @Test
    void givenBlankNotes_whenValidate_thenShouldHaveError() {
        // given
        final var pet = Pet.newPet(TutorID.unique(), "Buddy", null, null, null, "  ");
        final var notification = Notification.create();

        // when
        new PetValidator(pet, notification).validate();

        // then
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.firstError().message()).isEqualTo("'notes' should not be empty");
    }

    @Test
    void givenTooLongName_whenValidate_thenShouldHaveError() {
        // given
        final var longName = "a".repeat(256);
        final var pet = Pet.newPet(TutorID.unique(), longName, null, null, null, null);
        final var notification = Notification.create();

        // when
        new PetValidator(pet, notification).validate();

        // then
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.firstError().message())
                .isEqualTo("'name' must be between 1 and 255 characters");
    }

    @Test
    void givenTooLongSpecies_whenValidate_thenShouldHaveError() {
        // given
        final var longSpecies = "s".repeat(256);
        final var pet = Pet.newPet(TutorID.unique(), "Buddy", longSpecies, null, null, null);
        final var notification = Notification.create();

        // when
        new PetValidator(pet, notification).validate();

        // then
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.firstError().message())
                .isEqualTo("'species' must be between 1 and 255 characters");
    }

    @Test
    void givenTooLongNotes_whenValidate_thenShouldHaveError() {
        // given
        final var longNotes = "n".repeat(1001);
        final var pet = Pet.newPet(TutorID.unique(), "Buddy", null, null, null, longNotes);
        final var notification = Notification.create();

        // when
        new PetValidator(pet, notification).validate();

        // then
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.firstError().message())
                .isEqualTo("'notes' must be between 1 and 1000 characters");
    }
}
