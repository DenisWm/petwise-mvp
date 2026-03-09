package com.petwise.domain.pet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.petwise.domain.UnitTest;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link PetID}. */
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.ShortVariable",
    "PMD.AvoidDuplicateLiterals"
})
class PetIDTest extends UnitTest {
    PetIDTest() {}

    @Test
    void givenNoParameters_whenCallsUnique_thenShouldGenerateNewId() {
        final var id1 = PetID.unique();
        final var id2 = PetID.unique();
        assertThat(id1).isNotNull();
        assertThat(id1.getValue()).isNotNull();
        assertThat(id1.getValue()).isNotBlank();
        assertThat(id1).isNotEqualTo(id2);
        assertThat(id1.getValue()).isNotEqualTo(id2.getValue());
    }

    @Test
    void givenValidId_whenCallsFrom_thenShouldCreatePetID() {
        final var expectedId = "123e4567-e89b-12d3-a456-426614174000";
        final var actualId = PetID.from(expectedId);
        assertThat(actualId).isNotNull();
        assertThat(actualId.getValue()).isEqualTo(expectedId);
    }

    @Test
    void givenNullId_whenCallsFrom_thenShouldThrowException() {
        assertThatThrownBy(() -> PetID.from(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    void givenSameIdValue_whenCompare_thenShouldBeEqual() {
        final var id = "test-id-123";
        final var petId1 = PetID.from(id);
        final var petId2 = PetID.from(id);
        assertThat(petId1).isEqualTo(petId2);
        assertThat(petId1.hashCode()).isEqualTo(petId2.hashCode());
    }

    @Test
    void givenDifferentIdValues_whenCompare_thenShouldNotBeEqual() {
        final var petId1 = PetID.from("id-1");
        final var petId2 = PetID.from("id-2");
        assertThat(petId1).isNotEqualTo(petId2);
        assertThat(petId1.hashCode()).isNotEqualTo(petId2.hashCode());
    }

    @Test
    void givenPetID_whenCallsToString_thenShouldReturnValue() {
        final var expectedId = "test-id";
        final var petId = PetID.from(expectedId);
        final var result = petId.toString();
        assertThat(result).isEqualTo(expectedId);
    }

    @Test
    void givenPetID_whenCallsGetValue_thenShouldReturnCorrectValue() {
        final var expectedValue = "my-pet-id";
        final var petId = PetID.from(expectedValue);
        final var actualValue = petId.getValue();
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    void givenPetID_whenCompareWithNull_thenShouldNotBeEqual() {
        final var petId = PetID.from("id-1");
        assertThat(petId).isNotEqualTo(null);
    }

    @Test
    void givenPetID_whenCompareWithSelf_thenShouldBeEqual() {
        final var petId = PetID.from("id-1");
        assertThat(petId).isEqualTo(petId);
    }

    @Test
    void givenPetID_whenCompareWithDifferentType_thenShouldNotBeEqual() {
        final var petId = PetID.from("id-1");
        assertThat(petId).isNotEqualTo("id-1");
    }
}
