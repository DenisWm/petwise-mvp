package com.petwise.domain.exceptions;

/**
 * Base {@link RuntimeException} that intentionally suppresses stack-trace generation for
 * performance-sensitive domain exceptions.
 *
 * <p>Stack traces are expensive to fill in. For well-known business rule violations (e.g.,
 * "entity not found", "validation failed") the trace provides no debugging value and only adds
 * overhead. Subclass this when creating domain-specific exceptions that do not need a full trace.
 */
public class NoStacktraceRuntimeException extends RuntimeException {

    /**
     * Constructs the exception with a human-readable message and no cause.
     *
     * @param message the detail message
     */
    public NoStacktraceRuntimeException(final String message) {
        this(message, null);
    }

    /**
     * Constructs the exception with a human-readable message and an optional cause.
     * Stack-trace filling is disabled via the {@code writableStackTrace = false} flag.
     *
     * @param message the detail message
     * @param cause   the underlying cause, or {@code null} if none
     */
    public NoStacktraceRuntimeException(final String message, Throwable cause) {
        super(message, cause, true, false);
    }
}
