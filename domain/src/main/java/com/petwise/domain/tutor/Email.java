package com.petwise.domain.tutor;

import com.petwise.domain.ValueObject;
import com.petwise.domain.exceptions.DomainException;
import com.petwise.domain.validation.Error;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Email value object that validates email addresses. Ensures the email is not null and follows a
 * valid email format.
 */
public class Email extends ValueObject {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private final String value;

    private Email(final String value) {
        this.value = value;
    }

    /**
     * Creates a new Email instance with validation. Email is optional - null and blank values are
     * allowed. Empty or blank strings are treated as null.
     *
     * @param email the email string to validate (can be null or blank)
     * @return a new Email instance, or null if email is null or blank
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
        final String trimmedEmail = email.trim();

        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            throw DomainException.with(new Error("'email' is not a valid email address"));
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
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
