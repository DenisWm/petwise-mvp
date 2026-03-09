package com.petwise.domain.tutor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.petwise.domain.UnitTest;
import com.petwise.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link Phone}. */
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals",
    "PMD.TooManyMethods"
})
class PhoneTest extends UnitTest {
    PhoneTest() {}

    @Test
    void givenValidPhone_whenCallsFrom_thenShouldInstantiatePhone() {
        final var expectedPhone = "+1 (555) 123-4567";
        final var actualPhone = Phone.from(expectedPhone);
        assertThat(actualPhone).isNotNull();
        assertThat(actualPhone.getValue()).isEqualTo(expectedPhone);
    }

    @Test
    void givenNullPhone_whenCallsFrom_thenShouldReturnNull() {
        final String nullPhone = null;
        final var actualPhone = Phone.from(nullPhone);
        assertThat(actualPhone).isNull();
    }

    @Test
    void givenEmptyPhone_whenCallsFrom_thenShouldReturnNull() {
        final var emptyPhone = "";
        final var actualPhone = Phone.from(emptyPhone);
        assertThat(actualPhone).isNull();
    }

    @Test
    void givenBlankPhone_whenCallsFrom_thenShouldReturnNull() {
        final var blankPhone = "   ";
        final var actualPhone = Phone.from(blankPhone);
        assertThat(actualPhone).isNull();
    }

    @Test
    void givenInvalidPhone_whenCallsFrom_thenShouldThrowDomainException() {
        final var invalidPhone = "abc";
        assertThatThrownBy(() -> Phone.from(invalidPhone))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'phone' is not a valid phone number");
    }

    @Test
    void givenPhoneTooShort_whenCallsFrom_thenShouldThrowDomainException() {
        final var shortPhone = "123";
        assertThatThrownBy(() -> Phone.from(shortPhone))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'phone' is not a valid phone number");
    }

    @Test
    void givenValidPhoneWithSpaces_whenCallsFrom_thenShouldInstantiatePhone() {
        final var validPhone = "555 123 4567";
        final var actualPhone = Phone.from(validPhone);
        assertThat(actualPhone).isNotNull();
        assertThat(actualPhone.getValue()).isEqualTo(validPhone);
    }

    @Test
    void givenValidPhoneWithDashes_whenCallsFrom_thenShouldInstantiatePhone() {
        final var validPhone = "555-123-4567";
        final var actualPhone = Phone.from(validPhone);
        assertThat(actualPhone).isNotNull();
        assertThat(actualPhone.getValue()).isEqualTo(validPhone);
    }

    @Test
    void givenValidPhoneWithParentheses_whenCallsFrom_thenShouldInstantiatePhone() {
        final var validPhone = "(555) 123-4567";
        final var actualPhone = Phone.from(validPhone);
        assertThat(actualPhone).isNotNull();
        assertThat(actualPhone.getValue()).isEqualTo(validPhone);
    }

    @Test
    void givenValidInternationalPhone_whenCallsFrom_thenShouldInstantiatePhone() {
        final var validPhone = "+55 11 98765-4321";
        final var actualPhone = Phone.from(validPhone);
        assertThat(actualPhone).isNotNull();
        assertThat(actualPhone.getValue()).isEqualTo(validPhone);
    }

    @Test
    void givenTwoPhonesWithSameValue_whenCompare_thenShouldBeEqual() {
        final var phone1 = Phone.from("555-123-4567");
        final var phone2 = Phone.from("555-123-4567");
        assertThat(phone1).isEqualTo(phone2);
        assertThat(phone1.hashCode()).isEqualTo(phone2.hashCode());
    }

    @Test
    void givenTwoPhonesWithDifferentValues_whenCompare_thenShouldNotBeEqual() {
        final var phone1 = Phone.from("555-123-4567");
        final var phone2 = Phone.from("555-987-6543");
        assertThat(phone1).isNotEqualTo(phone2);
    }

    @Test
    void givenPhone_whenCallsToString_thenShouldReturnPhoneValue() {
        final var expectedPhone = "555-123-4567";
        final var phone = Phone.from(expectedPhone);
        final var result = phone.toString();
        assertThat(result).isEqualTo(expectedPhone);
    }

    @Test
    void givenPhone_whenCompareWithSelf_thenShouldBeEqual() {
        final var phone = Phone.from("555-123-4567");
        assertThat(phone).isEqualTo(phone);
    }

    @Test
    void givenPhone_whenCompareWithNull_thenShouldNotBeEqual() {
        final var phone = Phone.from("555-123-4567");
        assertThat(phone).isNotEqualTo(null);
    }

    @Test
    void givenPhone_whenCompareWithDifferentType_thenShouldNotBeEqual() {
        final var phone = Phone.from("555-123-4567");
        assertThat(phone).isNotEqualTo("555-123-4567");
    }
}
