package com.petwise.domain.exceptions;

/**
 * {@link RuntimeException} that suppresses stack-trace generation ({@code writableStackTrace =
 * false}). Use for well-known business rule violations where the trace adds no debugging value.
 */
@SuppressWarnings({"PMD.MissingSerialVersionUID", "PMD.MethodArgumentCouldBeFinal"})
public class NoStacktraceRuntimeException extends RuntimeException {
    public NoStacktraceRuntimeException(final String message) {
        this(message, null);
    }

    public NoStacktraceRuntimeException(final String message, final Throwable cause) {
        super(message, cause, true, false);
    }
}
