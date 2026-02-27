package com.petwise.domain.pet;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.domain.UnitTest;
import com.petwise.domain.tutor.TutorID;
import com.petwise.domain.validation.handler.Notification;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link Pet}. */
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
class PetTest extends UnitTest {

    /** Default constructor. */
    PetTest() {}

    @Test
    void givenValidParams_whenCallsNewPet_thenShouldInstantiatePet() {
        // given
        final var expectedTutorId = TutorID.unique();
        final var expectedName = "Fluffy";
        final var expectedSpecies = "Cat";
        final var expectedBreed = "Persian";
        final var expectedBirthDate = LocalDate.of(2020, 3, 15);
        final var expectedNotes = "Allergic to chicken";

        // when
        final var actualPet =
                Pet.newPet(
                        expectedTutorId,
                        expectedName,
                        expectedSpecies,
                        expectedBreed,
                        expectedBirthDate,
                        expectedNotes);

        // then
        assertThat(actualPet).isNotNull();
        assertThat(actualPet.getId()).isNotNull();
        assertThat(actualPet.getTutorId()).isEqualTo(expectedTutorId);
        assertThat(actualPet.getName()).isEqualTo(expectedName);
        assertThat(actualPet.getSpecies()).isEqualTo(expectedSpecies);
        assertThat(actualPet.getBreed()).isEqualTo(expectedBreed);
        assertThat(actualPet.getBirthDate()).isEqualTo(expectedBirthDate);
        assertThat(actualPet.getNotes()).isEqualTo(expectedNotes);
        assertThat(actualPet.getCreatedAt()).isNotNull();
        assertThat(actualPet.getUpdatedAt()).isNotNull();
    }

    @Test
    void givenValidParamsWithoutOptionals_whenCallsNewPet_thenShouldInstantiatePet() {
        // given
        final var expectedTutorId = TutorID.unique();
        final var expectedName = "Buddy";

        // when
        final var actualPet = Pet.newPet(expectedTutorId, expectedName, null, null, null, null);

        // then
        assertThat(actualPet).isNotNull();
        assertThat(actualPet.getId()).isNotNull();
        assertThat(actualPet.getTutorId()).isEqualTo(expectedTutorId);
        assertThat(actualPet.getName()).isEqualTo(expectedName);
        assertThat(actualPet.getSpecies()).isNull();
        assertThat(actualPet.getBreed()).isNull();
        assertThat(actualPet.getBirthDate()).isNull();
        assertThat(actualPet.getNotes()).isNull();
        assertThat(actualPet.getCreatedAt()).isNotNull();
        assertThat(actualPet.getUpdatedAt()).isNotNull();
    }

    @Test
    void givenNullName_whenCallsNewPetAndValidate_thenShouldReceiveError() {
        // given
        final var expectedTutorId = TutorID.unique();
        final String invalidName = null;
        final var expectedErrorMessage = "'name' should not be null";

        // when
        final var actualPet = Pet.newPet(expectedTutorId, invalidName, null, null, null, null);
        final var notification = Notification.create();
        actualPet.validate(notification);

        // then
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(1);
        assertThat(notification.firstError().message()).isEqualTo(expectedErrorMessage);
    }

    @Test
    void givenEmptyName_whenCallsNewPetAndValidate_thenShouldReceiveError() {
        // given
        final var expectedTutorId = TutorID.unique();
        final var invalidName = "  ";
        final var expectedErrorMessage = "'name' should not be empty";

        // when
        final var actualPet = Pet.newPet(expectedTutorId, invalidName, null, null, null, null);
        final var notification = Notification.create();
        actualPet.validate(notification);

        // then
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(1);
        assertThat(notification.firstError().message()).isEqualTo(expectedErrorMessage);
    }

    @Test
    void givenNullTutorId_whenCallsNewPetAndValidate_thenShouldReceiveError() {
        // given
        final var expectedErrorMessage = "'tutorId' should not be null";

        // when
        final var actualPet = Pet.newPet(null, "Name", null, null, null, null);
        final var notification = Notification.create();
        actualPet.validate(notification);

        // then
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(1);
        assertThat(notification.firstError().message()).isEqualTo(expectedErrorMessage);
    }

    @Test
    void givenFutureBirthDate_whenCallsNewPetAndValidate_thenShouldReceiveError() {
        // given
        final var expectedTutorId = TutorID.unique();
        final var invalidBirthDate = LocalDate.now().plusDays(1);
        final var expectedErrorMessage = "'birthDate' must not be in the future";

        // when
        final var actualPet =
                Pet.newPet(expectedTutorId, "Name", null, null, invalidBirthDate, null);
        final var notification = Notification.create();
        actualPet.validate(notification);

        // then
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(1);
        assertThat(notification.firstError().message()).isEqualTo(expectedErrorMessage);
    }

    @Test
    void givenValidPet_whenCallsUpdate_thenShouldReturnUpdatedPet() {
        // given
        final var pet = Pet.newPet(TutorID.unique(), "Fluffy", null, null, null, null);
        final var createdAt = pet.getCreatedAt();
        final var expectedName = "Fluffy Updated";
        final var expectedSpecies = "Dog";
        final var expectedBreed = "Beagle";
        final var expectedBirthDate = LocalDate.of(2019, 1, 1);
        final var expectedNotes = "Friendly";

        // when
        final var updatedPet =
                pet.update(
                        expectedName,
                        expectedSpecies,
                        expectedBreed,
                        expectedBirthDate,
                        expectedNotes);

        // then
        assertThat(updatedPet).isNotNull();
        assertThat(updatedPet.getName()).isEqualTo(expectedName);
        assertThat(updatedPet.getSpecies()).isEqualTo(expectedSpecies);
        assertThat(updatedPet.getBreed()).isEqualTo(expectedBreed);
        assertThat(updatedPet.getBirthDate()).isEqualTo(expectedBirthDate);
        assertThat(updatedPet.getNotes()).isEqualTo(expectedNotes);
        assertThat(updatedPet.getCreatedAt()).isEqualTo(createdAt);
        assertThat(updatedPet.getUpdatedAt()).isAfter(createdAt);
    }

    @Test
    void givenValidParams_whenCallsWith_thenShouldInstantiatePet() {
        // given
        final var expectedId = "123456789";
        final var expectedTutorId = TutorID.unique();
        final var expectedName = "Fluffy";
        final var expectedSpecies = "Cat";
        final var expectedBreed = "Persian";
        final var expectedBirthDate = LocalDate.of(2020, 3, 15);
        final var expectedNotes = "Notes";
        final var expectedCreatedAt = java.time.Instant.now();
        final var expectedUpdatedAt = java.time.Instant.now();

        // when
        final var actualPet =
                Pet.with(
                        expectedId,
                        expectedTutorId.getValue(),
                        expectedName,
                        expectedSpecies,
                        expectedBreed,
                        expectedBirthDate,
                        expectedNotes,
                        expectedCreatedAt,
                        expectedUpdatedAt);

        // then
        assertThat(actualPet).isNotNull();
        assertThat(actualPet.getId().getValue()).isEqualTo(expectedId);
        assertThat(actualPet.getTutorId()).isEqualTo(expectedTutorId);
        assertThat(actualPet.getName()).isEqualTo(expectedName);
        assertThat(actualPet.getSpecies()).isEqualTo(expectedSpecies);
        assertThat(actualPet.getBreed()).isEqualTo(expectedBreed);
        assertThat(actualPet.getBirthDate()).isEqualTo(expectedBirthDate);
        assertThat(actualPet.getNotes()).isEqualTo(expectedNotes);
        assertThat(actualPet.getCreatedAt()).isEqualTo(expectedCreatedAt);
        assertThat(actualPet.getUpdatedAt()).isEqualTo(expectedUpdatedAt);
    }
}
