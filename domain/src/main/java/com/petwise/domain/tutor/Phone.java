package com.petwise.domain.tutor;

import com.petwise.domain.ValueObject;
import com.petwise.domain.exceptions.DomainException;
import com.petwise.domain.validation.Error;
import java.util.Objects;
import java.util.regex.Pattern;

/** Phone value object that validates phone numbers. Phone is optional - null values are allowed. */
public class Phone extends ValueObject {

    // Simple pattern that accepts common phone formats
    private static final String PHONE_REGEX = "^[\\d\\s()+-]{8,20}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    private final String value;

    private Phone(final String value) {
        this.value = value;
    }

    /**
     * Creates a new Phone instance with validation. Phone is optional - null and blank values are
     * allowed. Empty or blank strings are treated as null.
     *
     * @param phone the phone string to validate (can be null or blank)
     * @return a new Phone instance, or null if phone is null or blank
     * @throws DomainException if the phone format is invalid
     */
    public static Phone from(final String phone) {
        if (phone == null || phone.isBlank()) {
            return null;
        }
        validate(phone);
        return new Phone(phone);
    }

    private static void validate(final String phone) {
        final String trimmedPhone = phone.trim();

        if (!PHONE_PATTERN.matcher(trimmedPhone).matches()) {
            throw DomainException.with(new Error("'phone' is not a valid phone number"));
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return Objects.equals(value, phone.value);
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
