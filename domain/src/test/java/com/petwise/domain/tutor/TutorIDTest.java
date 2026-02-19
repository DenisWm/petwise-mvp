package com.petwise.domain.tutor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.petwise.domain.UnitTest;
import org.junit.jupiter.api.Test;

class TutorIDTest extends UnitTest {

    @Test
    void givenNoParameters_whenCallsUnique_thenShouldGenerateNewId() {
        // when
        final var id1 = TutorID.unique();
        final var id2 = TutorID.unique();

        // then
        assertThat(id1).isNotNull();
        assertThat(id1.getValue()).isNotNull();
        assertThat(id1.getValue()).isNotBlank();
        assertThat(id1).isNotEqualTo(id2);
        assertThat(id1.getValue()).isNotEqualTo(id2.getValue());
    }

    @Test
    void givenValidId_whenCallsFrom_thenShouldCreateTutorID() {
        // given
        final var expectedId = "123e4567-e89b-12d3-a456-426614174000";

        // when
        final var actualId = TutorID.from(expectedId);

        // then
        assertThat(actualId).isNotNull();
        assertThat(actualId.getValue()).isEqualTo(expectedId);
    }

    @Test
    void givenNullId_whenCallsFrom_thenShouldThrowException() {
        // when & then
        assertThatThrownBy(() -> TutorID.from(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenSameIdValue_whenCompare_thenShouldBeEqual() {
        // given
        final var id = "test-id-123";
        final var tutorId1 = TutorID.from(id);
        final var tutorId2 = TutorID.from(id);

        // when & then
        assertThat(tutorId1).isEqualTo(tutorId2);
        assertThat(tutorId1.hashCode()).isEqualTo(tutorId2.hashCode());
    }

    @Test
    void givenDifferentIdValues_whenCompare_thenShouldNotBeEqual() {
        // given
        final var tutorId1 = TutorID.from("id-1");
        final var tutorId2 = TutorID.from("id-2");

        // when & then
        assertThat(tutorId1).isNotEqualTo(tutorId2);
        assertThat(tutorId1.hashCode()).isNotEqualTo(tutorId2.hashCode());
    }

    @Test
    void givenTutorID_whenCallsToString_thenShouldReturnValue() {
        // given
        final var expectedId = "test-id";
        final var tutorId = TutorID.from(expectedId);

        // when
        final var result = tutorId.toString();

        // then
        assertThat(result).isEqualTo(expectedId);
    }

    @Test
    void givenTutorID_whenCallsGetValue_thenShouldReturnCorrectValue() {
        // given
        final var expectedValue = "my-tutor-id";
        final var tutorId = TutorID.from(expectedValue);

        // when
        final var actualValue = tutorId.getValue();

        // then
        assertThat(actualValue).isEqualTo(expectedValue);
    }
}

