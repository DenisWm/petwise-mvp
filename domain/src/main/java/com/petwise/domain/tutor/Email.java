package com.petwise.domain.tutor;

import com.petwise.domain.ValueObject;
import com.petwise.domain.exceptions.DomainException;
import com.petwise.domain.validation.Error;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Email value object that validates email addresses. Ensures the email follows a valid format when
 * provided.
 */
@SuppressWarnings({
    "PMD.ClassWithOnlyPrivateConstructorsShouldBeFinal",
    "PMD.OnlyOneReturn",
    "PMD.ShortVariable",
    "PMD.ControlStatementBraces",
    "PMD.MethodArgumentCouldBeFinal",
    "PMD.LocalVariableCouldBeFinal"
})
public final class Email extends ValueObject {

    /** Regex pattern string for email validation. */
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    /** Compiled pattern for email validation. */
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /** The raw email string. */
    private final String value;

    /** Private constructor — use {@link #from(String)} instead. */
    private Email(final String aValue) {
        super();
        this.value = aValue;
    }

    /**
     * Creates a new Email instance with validation. Email is optional — null and blank values
     * return {@code null}.
     *
     * @param email the email string to validate (can be null or blank)
     * @return a new Email instance, or {@code null} if email is null or blank
     * @throws DomainException if the email format is invalid
     */
    public static Email from(final String email) {
        if (email == null || email.isBlank()) {
            return null;
        }
        validate(email);
        return new Email(email);
    }

    private static void validate(final String email) {
        final var trimmedEmail = email.trim();
        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            throw DomainException.with(new Error("'email' is not a valid email address"));
        }
    }

    /**
     * Returns the raw email string.
     *
     * @return the email value; never {@code null}
     */
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final Email email = (Email) other;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
