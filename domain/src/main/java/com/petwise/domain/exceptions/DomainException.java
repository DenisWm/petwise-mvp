package com.petwise.domain.exceptions;

import com.petwise.domain.validation.Error;
import java.util.List;

/**
 * Base exception for domain rule violations. Carries a list of {@link Error}s and extends {@link
 * NoStacktraceRuntimeException} to avoid stack-trace overhead.
 */
@SuppressWarnings("PMD.MissingSerialVersionUID")
public class DomainException extends NoStacktraceRuntimeException {
    private final List<Error> errors;

    protected DomainException(final String message, final List<Error> anErrors) {
        super(message);
        this.errors = List.copyOf(anErrors);
    }

    public static DomainException with(final List<Error> anErrors) {
        return new DomainException("", anErrors);
    }

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
