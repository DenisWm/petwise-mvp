package com.petwise.domain.exceptions;

import com.petwise.domain.validation.Error;
import java.util.List;

/**
 * Base exception for all domain rule violations.
 *
 * <p>Carries a list of {@link Error}s describing every constraint that was broken. Use the static
 * factory methods to create instances:
 *
 * <ul>
 *   <li>{@link #with(Error)} – single violation
 *   <li>{@link #with(List)} – multiple violations collected by a {@link
 *       com.petwise.domain.validation.ValidationHandler}
 * </ul>
 *
 * <p>Extends {@link NoStacktraceRuntimeException} so that no stack trace is filled in, keeping the
 * cost of domain exceptions low.
 */
@SuppressWarnings("PMD.MissingSerialVersionUID")
public class DomainException extends NoStacktraceRuntimeException {

    /** The list of validation errors that triggered this exception. */
    private final List<Error> errors;

    /**
     * Constructs a {@code DomainException} with a message and errors.
     *
     * @param message the detail message
     * @param anErrors the list of errors; must not be {@code null}
     */
    protected DomainException(final String message, final List<Error> anErrors) {
        super(message);
        this.errors = anErrors;
    }

    /**
     * Creates a {@code DomainException} from a list of validation errors.
     *
     * @param anErrors the list of errors; must not be {@code null}
     * @return a new {@code DomainException}
     */
    public static DomainException with(final List<Error> anErrors) {
        return new DomainException("", anErrors);
    }

    /**
     * Creates a {@code DomainException} from a single validation error.
     *
     * @param anError the error; must not be {@code null}
     * @return a new {@code DomainException}
     */
    public static DomainException with(final Error anError) {
        return new DomainException(anError.message(), List.of(anError));
    }

    /**
     * Returns all errors associated with this exception.
     *
     * @return a list of {@link Error}s; never {@code null}
     */
    public List<Error> getErrors() {
        return errors;
    }
}
