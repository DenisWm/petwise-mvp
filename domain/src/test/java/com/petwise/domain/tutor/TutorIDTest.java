package com.petwise.domain.tutor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.petwise.domain.UnitTest;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link TutorID}. */
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.ShortVariable",
    "PMD.AvoidDuplicateLiterals"
})
class TutorIDTest extends UnitTest {
    TutorIDTest() {}

    @Test
    void givenNoParameters_whenCallsUnique_thenShouldGenerateNewId() {
        final var id1 = TutorID.unique();
        final var id2 = TutorID.unique();
        assertThat(id1).isNotNull();
        assertThat(id1.getValue()).isNotNull();
        assertThat(id1.getValue()).isNotBlank();
        assertThat(id1).isNotEqualTo(id2);
        assertThat(id1.getValue()).isNotEqualTo(id2.getValue());
    }

    @Test
    void givenValidId_whenCallsFrom_thenShouldCreateTutorID() {
        final var expectedId = "123e4567-e89b-12d3-a456-426614174000";
        final var actualId = TutorID.from(expectedId);
        assertThat(actualId).isNotNull();
        assertThat(actualId.getValue()).isEqualTo(expectedId);
    }

    @Test
    void givenNullId_whenCallsFrom_thenShouldThrowException() {
        assertThatThrownBy(() -> TutorID.from(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenSameIdValue_whenCompare_thenShouldBeEqual() {
        final var id = "test-id-123";
        final var tutorId1 = TutorID.from(id);
        final var tutorId2 = TutorID.from(id);
        assertThat(tutorId1).isEqualTo(tutorId2);
        assertThat(tutorId1.hashCode()).isEqualTo(tutorId2.hashCode());
    }

    @Test
    void givenDifferentIdValues_whenCompare_thenShouldNotBeEqual() {
        final var tutorId1 = TutorID.from("id-1");
        final var tutorId2 = TutorID.from("id-2");
        assertThat(tutorId1).isNotEqualTo(tutorId2);
        assertThat(tutorId1.hashCode()).isNotEqualTo(tutorId2.hashCode());
    }

    @Test
    void givenTutorID_whenCallsToString_thenShouldReturnValue() {
        final var expectedId = "test-id";
        final var tutorId = TutorID.from(expectedId);
        final var result = tutorId.toString();
        assertThat(result).isEqualTo(expectedId);
    }

    @Test
    void givenTutorID_whenCallsGetValue_thenShouldReturnCorrectValue() {
        final var expectedValue = "my-tutor-id";
        final var tutorId = TutorID.from(expectedValue);
        final var actualValue = tutorId.getValue();
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void givenTutorID_whenCompareWithNull_thenShouldNotBeEqual() {
        final var tutorId = TutorID.from("id-1");
        assertThat(tutorId).isNotEqualTo(null);
    }

    @Test
    void givenTutorID_whenCompareWithSelf_thenShouldBeEqual() {
        final var tutorId = TutorID.from("id-1");
        assertThat(tutorId).isEqualTo(tutorId);
    }

    @Test
    void givenTutorID_whenCompareWithDifferentType_thenShouldNotBeEqual() {
        final var tutorId = TutorID.from("id-1");
        assertThat(tutorId).isNotEqualTo("id-1");
    }
}
