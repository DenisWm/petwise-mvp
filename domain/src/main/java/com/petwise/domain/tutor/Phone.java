package com.petwise.domain.tutor;

import com.petwise.domain.ValueObject;
import com.petwise.domain.exceptions.DomainException;
import com.petwise.domain.validation.Error;
import java.util.Objects;
import java.util.regex.Pattern;

/** Phone value object that validates phone numbers. Phone is optional - null values are allowed. */
@SuppressWarnings({
    "PMD.ClassWithOnlyPrivateConstructorsShouldBeFinal",
    "PMD.OnlyOneReturn",
    "PMD.ShortVariable",
    "PMD.ControlStatementBraces",
    "PMD.MethodArgumentCouldBeFinal",
    "PMD.LocalVariableCouldBeFinal"
})
public final class Phone extends ValueObject {
    private static final String PHONE_REGEX = "^[\\d\\s()+-]{8,20}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);
    private final String value;

    private Phone(final String aValue) {
        super();
        this.value = aValue;
    }

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
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final Phone phone = (Phone) other;
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
