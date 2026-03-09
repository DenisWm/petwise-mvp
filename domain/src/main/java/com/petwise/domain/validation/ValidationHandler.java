package com.petwise.domain.validation;

import java.util.List;

/**
 * Accumulates validation errors instead of failing at the first broken invariant. Use {@link
 * com.petwise.domain.validation.handler.Notification} as the standard implementation.
 */
@SuppressWarnings({"PMD.LongVariable", "PMD.OnlyOneReturn"})
public interface ValidationHandler {
    ValidationHandler append(Error anError);

    ValidationHandler append(ValidationHandler handler);

    /**
     * Executes the given {@link Validation}, catching any {@link
     * com.petwise.domain.exceptions.DomainException} and appending its errors to this handler
     * instead of propagating.
     *
     * @param <T> the return type of the validation block
     * @param aValidation the validation logic to execute
     * @return the result of the validation, or {@code null} if caught
     */
    <T> T validate(Validation<T> aValidation);

    default Error firstError() {
        if (getErrors() != null && !getErrors().isEmpty()) {
            return getErrors().getFirst();
        }
        return null;
    }

    default boolean hasErrors() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    List<Error> getErrors();

    /**
     * @param <T> the type returned by the validation
     */
    @SuppressWarnings("PMD.ImplicitFunctionalInterface")
    interface Validation<T> {
        T validate();
    }
}
