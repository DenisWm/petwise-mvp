package com.petwise.domain.exceptions;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.domain.UnitTest;
import com.petwise.domain.validation.Error;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link DomainException}. */
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.ShortVariable"
})
class DomainExceptionTest extends UnitTest {
    DomainExceptionTest() {}

    @Test
    void givenSingleError_whenWithError_thenShouldContainIt() {
        final var error = new Error("bad input");
        final var ex = DomainException.with(error);
        assertThat(ex.getMessage()).isEqualTo("bad input");
        assertThat(ex.getErrors()).hasSize(1);
        assertThat(ex.getErrors().getFirst().message()).isEqualTo("bad input");
    }

    @Test
    void givenListOfErrors_whenWithList_thenShouldContainAll() {
        final var errors = List.of(new Error("e1"), new Error("e2"));
        final var ex = DomainException.with(errors);
        assertThat(ex.getErrors()).hasSize(2);
        assertThat(ex.getMessage()).isEmpty();
    }

    @Test
    void givenErrors_whenGetErrors_thenShouldReturnUnmodifiableList() {
        final var ex = DomainException.with(new Error("e"));
        assertThat(ex.getErrors()).isUnmodifiable();
    }
}
