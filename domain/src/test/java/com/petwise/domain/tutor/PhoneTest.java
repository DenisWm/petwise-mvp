package com.petwise.domain.tutor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.petwise.domain.UnitTest;
import com.petwise.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

class PhoneTest extends UnitTest {

    @Test
    void givenValidPhone_whenCallsFrom_thenShouldInstantiatePhone() {
        // given
        final var expectedPhone = "+1 (555) 123-4567";

        // when
        final var actualPhone = Phone.from(expectedPhone);

        // then
        assertThat(actualPhone).isNotNull();
        assertThat(actualPhone.getValue()).isEqualTo(expectedPhone);
    }

    @Test
    void givenNullPhone_whenCallsFrom_thenShouldReturnNull() {
        // given
        final String nullPhone = null;

        // when
        final var actualPhone = Phone.from(nullPhone);

        // then
        assertThat(actualPhone).isNull();
    }

    @Test
    void givenEmptyPhone_whenCallsFrom_thenShouldReturnNull() {
        // given
        final var emptyPhone = "";

        // when
        final var actualPhone = Phone.from(emptyPhone);

        // then
        assertThat(actualPhone).isNull();
    }

    @Test
    void givenBlankPhone_whenCallsFrom_thenShouldReturnNull() {
        // given
        final var blankPhone = "   ";

        // when
        final var actualPhone = Phone.from(blankPhone);

        // then
        assertThat(actualPhone).isNull();
    }

    @Test
    void givenInvalidPhone_whenCallsFrom_thenShouldThrowDomainException() {
        // given
        final var invalidPhone = "abc";

        // when & then
        assertThatThrownBy(() -> Phone.from(invalidPhone))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'phone' is not a valid phone number");
    }

    @Test
    void givenPhoneTooShort_whenCallsFrom_thenShouldThrowDomainException() {
        // given
        final var shortPhone = "123";

        // when & then
        assertThatThrownBy(() -> Phone.from(shortPhone))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("'phone' is not a valid phone number");
    }

    @Test
    void givenValidPhoneWithSpaces_whenCallsFrom_thenShouldInstantiatePhone() {
        // given
        final var validPhone = "555 123 4567";

        // when
        final var actualPhone = Phone.from(validPhone);

        // then
        assertThat(actualPhone).isNotNull();
        assertThat(actualPhone.getValue()).isEqualTo(validPhone);
    }

    @Test
    void givenValidPhoneWithDashes_whenCallsFrom_thenShouldInstantiatePhone() {
        // given
        final var validPhone = "555-123-4567";

        // when
        final var actualPhone = Phone.from(validPhone);

        // then
        assertThat(actualPhone).isNotNull();
        assertThat(actualPhone.getValue()).isEqualTo(validPhone);
    }

    @Test
    void givenValidPhoneWithParentheses_whenCallsFrom_thenShouldInstantiatePhone() {
        // given
        final var validPhone = "(555) 123-4567";

        // when
        final var actualPhone = Phone.from(validPhone);

        // then
        assertThat(actualPhone).isNotNull();
        assertThat(actualPhone.getValue()).isEqualTo(validPhone);
    }

    @Test
    void givenValidInternationalPhone_whenCallsFrom_thenShouldInstantiatePhone() {
        // given
        final var validPhone = "+55 11 98765-4321";

        // when
        final var actualPhone = Phone.from(validPhone);

        // then
        assertThat(actualPhone).isNotNull();
        assertThat(actualPhone.getValue()).isEqualTo(validPhone);
    }

    @Test
    void givenTwoPhonesWithSameValue_whenCompare_thenShouldBeEqual() {
        // given
        final var phone1 = Phone.from("555-123-4567");
        final var phone2 = Phone.from("555-123-4567");

        // when & then
        assertThat(phone1).isEqualTo(phone2);
        assertThat(phone1.hashCode()).isEqualTo(phone2.hashCode());
    }

    @Test
    void givenTwoPhonesWithDifferentValues_whenCompare_thenShouldNotBeEqual() {
        // given
        final var phone1 = Phone.from("555-123-4567");
        final var phone2 = Phone.from("555-987-6543");

        // when & then
        assertThat(phone1).isNotEqualTo(phone2);
    }

    @Test
    void givenPhone_whenCallsToString_thenShouldReturnPhoneValue() {
        // given
        final var expectedPhone = "555-123-4567";
        final var phone = Phone.from(expectedPhone);

        // when
        final var result = phone.toString();

        // then
        assertThat(result).isEqualTo(expectedPhone);
    }
}
