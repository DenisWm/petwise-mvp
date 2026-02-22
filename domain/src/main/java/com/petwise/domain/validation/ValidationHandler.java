package com.petwise.domain.validation;

import java.util.List;

/**
 * Contract for accumulating validation errors during domain object validation.
 *
 * <p>Rather than throwing an exception at the first broken invariant, implementations collect all
 * {@link Error}s so that callers can report every violation in a single response. Use {@link
 * com.petwise.domain.validation.handler.Notification} as the standard implementation.
 *
 * <p>Typical usage inside a {@link Validator}:
 *
 * <pre>{@code
 * handler.append(new Error("'name' should not be null"));
 * }</pre>
 */
@SuppressWarnings({"PMD.LongVariable", "PMD.OnlyOneReturn"})
public interface ValidationHandler {

    /**
     * Appends a single {@link Error} to this handler.
     *
     * @param anError the error to record; must not be {@code null}
     * @return this handler (for fluent chaining)
     */
    ValidationHandler append(Error anError);

    /**
     * Appends all errors from another {@link ValidationHandler}.
     *
     * @param handler the source handler whose errors will be merged
     * @return this handler (for fluent chaining)
     */
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

    /**
     * Returns the first recorded error, or {@code null} if none exist.
     *
     * @return the first {@link Error}, or {@code null}
     */
    default Error firstError() {
        if (getErrors() != null && !getErrors().isEmpty()) {
            return getErrors().getFirst();
        }
        return null;
    }

    /**
     * Returns {@code true} if at least one error has been recorded.
     *
     * @return {@code true} when there are validation errors
     */
    default boolean hasErrors() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    /**
     * Returns all recorded errors.
     *
     * @return a list of {@link Error}s; never {@code null}
     */
    List<Error> getErrors();

    /**
     * A single unit of validation logic that produces a result of type {@code T}.
     *
     * @param <T> the type returned by the validation
     */
    @SuppressWarnings("PMD.ImplicitFunctionalInterface")
    interface Validation<T> {
        /**
         * Executes this validation.
         *
         * @return the result
         */
        T validate();
    }
}
