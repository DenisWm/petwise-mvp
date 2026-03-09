package com.petwise.domain.tutor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.petwise.domain.UnitTest;
import com.petwise.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link Email}. */
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals",
    "PMD.TooManyMethods"
})
class EmailTest extends UnitTest {
    EmailTest() {}

    @Test
    void givenValidEmail_whenCallsFrom_thenShouldInstantiateEmail() {
        final var expectedEmail = "user@example.com";
        final var actualEmail = Email.from(expectedEmail);
        assertThat(actualEmail).isNotNull();
        assertThat(actualEmail.getValue()).isEqualTo(expectedEmail);
    }

    @Test
    void givenNullEmail_whenCallsFrom_thenShouldReturnNull() {
        final String nullEmail = null;
        final var actualEmail = Email.from(nullEmail);
        assertThat(actualEmail).isNull();
    }

    @Test
    void givenEmptyEmail_whenCallsFrom_thenShouldReturnNull() {
        final var emptyEmail = "";
        final var actualEmail = Email.from(emptyEmail);
        assertThat(actualEmail).isNull();
    }

    @Test
    void givenBlankEmail_whenCallsFrom_thenShouldReturnNull() {
        final var blankEmail = "   ";
        final var actualEmail = Email.from(blankEmail);
        assertThat(actualEmail).isNull();
    }

    @Test
    void givenInvalidEmail_whenCallsFrom_thenShouldThrowDomainException() {
        final var invalidEmail = "invalid-email";
        assertThatThrownBy(() -> Email.from(invalidEmail))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'email' is not a valid email address");
    }

    @Test
    void givenInvalidEmailWithoutAt_whenCallsFrom_thenShouldThrowDomainException() {
        final var invalidEmail = "userexample.com";
        assertThatThrownBy(() -> Email.from(invalidEmail))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'email' is not a valid email address");
    }

    @Test
    void givenInvalidEmailWithoutDomain_whenCallsFrom_thenShouldThrowDomainException() {
        final var invalidEmail = "user@";
        assertThatThrownBy(() -> Email.from(invalidEmail))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'email' is not a valid email address");
    }

    @Test
    void givenTwoEmailsWithSameValue_whenCompare_thenShouldBeEqual() {
        final var email1 = Email.from("user@example.com");
        final var email2 = Email.from("user@example.com");
        assertThat(email1).isEqualTo(email2);
        assertThat(email1.hashCode()).isEqualTo(email2.hashCode());
    }

    @Test
    void givenTwoEmailsWithDifferentValues_whenCompare_thenShouldNotBeEqual() {
        final var email1 = Email.from("user1@example.com");
        final var email2 = Email.from("user2@example.com");
        assertThat(email1).isNotEqualTo(email2);
    }

    @Test
    void givenEmail_whenCallsToString_thenShouldReturnEmailValue() {
        final var expectedEmail = "user@example.com";
        final var email = Email.from(expectedEmail);
        final var result = email.toString();
        assertThat(result).isEqualTo(expectedEmail);
    }

    @Test
    void givenEmail_whenCompareWithSelf_thenShouldBeEqual() {
        final var email = Email.from("user@example.com");
        assertThat(email).isEqualTo(email);
    }

    @Test
    void givenEmail_whenCompareWithNull_thenShouldNotBeEqual() {
        final var email = Email.from("user@example.com");
        assertThat(email).isNotEqualTo(null);
    }

    @Test
    void givenEmail_whenCompareWithDifferentType_thenShouldNotBeEqual() {
        final var email = Email.from("user@example.com");
        assertThat(email).isNotEqualTo("user@example.com");
    }
}
