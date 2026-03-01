package com.petwise.infrastructure.api;

import com.petwise.domain.exceptions.DomainException;
import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.exceptions.NotificationException;
import com.petwise.domain.validation.Error;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Centralised exception handler for all REST controllers.
 *
 * <p>Translates domain and application exceptions into proper HTTP responses and logs each scenario
 * at the appropriate severity level:
 *
 * <ul>
 *   <li>{@link NoResourceFoundException} → 404 (DEBUG) — missing static resources like favicon
 *   <li>{@link NotFoundException} → 404 (WARN)
 *   <li>{@link NotificationException} → 422 (WARN)
 *   <li>{@link DomainException} → 400 (WARN)
 *   <li>{@link Exception} → 500 (ERROR)
 * </ul>
 */
@RestControllerAdvice
@SuppressWarnings("PMD.ShortVariable")
public class GlobalExceptionHandler {

    /** SLF4J logger for this class. */
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** Default constructor. */
    public GlobalExceptionHandler() {}

    /**
     * Handles {@link NotFoundException} — entity not found.
     *
     * @param exception the exception
     * @return 404 response with error details
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(final NotFoundException exception) {
        if (LOG.isWarnEnabled()) {
            LOG.warn("Resource not found: {}", exception.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        ApiError.from(
                                HttpStatus.NOT_FOUND,
                                exception.getMessage(),
                                exception.getErrors()));
    }

    /**
     * Handles {@link NotificationException} — validation failures.
     *
     * @param exception the exception
     * @return 422 response with error details
     */
    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<ApiError> handleNotification(final NotificationException exception) {
        if (LOG.isWarnEnabled()) {
            LOG.warn(
                    "Validation failed: {} — errors: {}",
                    exception.getMessage(),
                    exception.getErrors());
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(
                        ApiError.from(
                                HttpStatus.UNPROCESSABLE_ENTITY,
                                exception.getMessage(),
                                exception.getErrors()));
    }

    /**
     * Handles generic {@link DomainException} — domain rule violations.
     *
     * @param exception the exception
     * @return 400 response with error details
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiError> handleDomain(final DomainException exception) {
        if (LOG.isWarnEnabled()) {
            LOG.warn(
                    "Domain rule violation: {} — errors: {}",
                    exception.getMessage(),
                    exception.getErrors());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ApiError.from(
                                HttpStatus.BAD_REQUEST,
                                exception.getMessage(),
                                exception.getErrors()));
    }

    /**
     * Handles {@link NoResourceFoundException} — missing static resources (e.g. favicon.ico).
     *
     * <p>Logged at DEBUG because this is expected behaviour for a pure REST API that serves no
     * static content. Browsers routinely request {@code /favicon.ico}, and this must not pollute
     * ERROR logs.
     *
     * @param exception the exception
     * @return 404 response with error details
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNoResourceFound(
            final NoResourceFoundException exception) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Static resource not found: {}", exception.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiError.from(HttpStatus.NOT_FOUND, exception.getMessage(), List.of()));
    }

    /**
     * Catch-all for unexpected exceptions.
     *
     * @param exception the exception
     * @return 500 response with a generic message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(final Exception exception) {
        if (LOG.isErrorEnabled()) {
            LOG.error("Unexpected error occurred", exception);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ApiError.from(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "An unexpected error occurred",
                                List.of()));
    }

    /**
     * Lightweight error body returned to API clients.
     *
     * @param status the HTTP status code
     * @param message a human-readable error summary
     * @param errors the list of detailed domain errors (may be empty)
     */
    public record ApiError(int status, String message, List<Error> errors) {

        /**
         * Canonical constructor that defensively copies the error list.
         *
         * @param status the HTTP status code
         * @param message a human-readable error summary
         * @param errors the list of detailed domain errors (may be empty)
         */
        public ApiError(final int status, final String message, final List<Error> errors) {
            this.status = status;
            this.message = message;
            this.errors = List.copyOf(errors);
        }

        static ApiError from(
                final HttpStatus httpStatus, final String message, final List<Error> errors) {
            return new ApiError(httpStatus.value(), message, errors);
        }
    }
}
