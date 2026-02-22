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
    "PMD.AvoidDuplicateLiterals"
})
class EmailTest extends UnitTest {

    /** Default constructor. */
    EmailTest() {}

    @Test
    void givenValidEmail_whenCallsFrom_thenShouldInstantiateEmail() {
        // given
        final var expectedEmail = "user@example.com";

        // when
        final var actualEmail = Email.from(expectedEmail);

        // then
        assertThat(actualEmail).isNotNull();
        assertThat(actualEmail.getValue()).isEqualTo(expectedEmail);
    }

    @Test
    void givenNullEmail_whenCallsFrom_thenShouldReturnNull() {
        // given
        final String nullEmail = null;

        // when
        final var actualEmail = Email.from(nullEmail);

        // then
        assertThat(actualEmail).isNull();
    }

    @Test
    void givenEmptyEmail_whenCallsFrom_thenShouldReturnNull() {
        // given
        final var emptyEmail = "";

        // when
        final var actualEmail = Email.from(emptyEmail);

        // then
        assertThat(actualEmail).isNull();
    }

    @Test
    void givenBlankEmail_whenCallsFrom_thenShouldReturnNull() {
        // given
        final var blankEmail = "   ";

        // when
        final var actualEmail = Email.from(blankEmail);

        // then
        assertThat(actualEmail).isNull();
    }

    @Test
    void givenInvalidEmail_whenCallsFrom_thenShouldThrowDomainException() {
        // given
        final var invalidEmail = "invalid-email";

        // when & then
        assertThatThrownBy(() -> Email.from(invalidEmail))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'email' is not a valid email address");
    }

    @Test
    void givenInvalidEmailWithoutAt_whenCallsFrom_thenShouldThrowDomainException() {
        // given
        final var invalidEmail = "userexample.com";

        // when & then
        assertThatThrownBy(() -> Email.from(invalidEmail))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'email' is not a valid email address");
    }

    @Test
    void givenInvalidEmailWithoutDomain_whenCallsFrom_thenShouldThrowDomainException() {
        // given
        final var invalidEmail = "user@";

        // when & then
        assertThatThrownBy(() -> Email.from(invalidEmail))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'email' is not a valid email address");
    }

    @Test
    void givenTwoEmailsWithSameValue_whenCompare_thenShouldBeEqual() {
        // given
        final var email1 = Email.from("user@example.com");
        final var email2 = Email.from("user@example.com");

        // when & then
        assertThat(email1).isEqualTo(email2);
        assertThat(email1.hashCode()).isEqualTo(email2.hashCode());
    }

    @Test
    void givenTwoEmailsWithDifferentValues_whenCompare_thenShouldNotBeEqual() {
        // given
        final var email1 = Email.from("user1@example.com");
        final var email2 = Email.from("user2@example.com");

        // when & then
        assertThat(email1).isNotEqualTo(email2);
    }

    @Test
    void givenEmail_whenCallsToString_thenShouldReturnEmailValue() {
        // given
        final var expectedEmail = "user@example.com";
        final var email = Email.from(expectedEmail);

        // when
        final var result = email.toString();

        // then
        assertThat(result).isEqualTo(expectedEmail);
    }
}
