package com.petwise.domain.exceptions;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.domain.UnitTest;
import com.petwise.domain.pet.Pet;
import com.petwise.domain.pet.PetID;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorID;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link NotFoundException}. */
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.ShortVariable"
})
class NotFoundExceptionTest extends UnitTest {

    /** Default constructor. */
    NotFoundExceptionTest() {}

    @Test
    void givenEntityAndId_whenWith_thenShouldFormatMessage() {
        final var id = PetID.from("pet-123");
        final var ex = NotFoundException.with(Pet.class, id);
        assertThat(ex).isInstanceOf(NotFoundException.class);
        assertThat(ex.getMessage()).isEqualTo("Pet with ID pet-123 was not found");
        assertThat(ex.getErrors()).isEmpty();
    }

    @Test
    void givenTutorEntityAndId_whenWith_thenShouldFormatMessageWithClassName() {
        final var id = TutorID.from("tutor-abc");
        final var ex = NotFoundException.with(Tutor.class, id);
        assertThat(ex.getMessage()).isEqualTo("Tutor with ID tutor-abc was not found");
    }

    @Test
    void givenNotFoundException_whenGetErrors_thenShouldReturnEmptyList() {
        final var ex = NotFoundException.with(Pet.class, PetID.from("x"));
        assertThat(ex.getErrors()).isEmpty();
    }
}
