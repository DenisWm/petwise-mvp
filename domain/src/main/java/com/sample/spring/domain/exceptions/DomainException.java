package com.sample.spring.domain.exceptions;

import com.sample.spring.domain.validation.Error;

import java.util.List;
import java.util.stream.Collectors;

public class DomainException extends NoStacktraceRuntimeException {

    protected final List<Error> errors;

    protected DomainException(final String message, final List<Error> errors) {
        super(message);
        this.errors = errors;
    }

    public static DomainException with(final List<Error> anErrors) {
        final String message = anErrors == null || anErrors.isEmpty()
                ? "Domain validation failed"
                : anErrors.stream().map(Error::message).collect(Collectors.joining(", "));
        return new DomainException(message, anErrors);
    }

    public static DomainException with(final Error aError) {
        return new DomainException(aError.message(), List.of(aError));
    }

    public List<Error> getErrors() {
        return this.errors;
    }
}
