package com.petwise.infrastructure.api;

import com.petwise.domain.exceptions.DomainException;
import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.exceptions.NotificationException;
import com.petwise.domain.validation.Error;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Centralised exception handler for all REST controllers.
 *
 * <p>All error responses follow <strong>RFC 7807 Problem Details</strong> ({@code
 * application/problem+json}) with the standard fields {@code type}, {@code title}, {@code status},
 * {@code detail}, and {@code instance}. Domain-specific validation errors are carried in the {@code
 * errors} extension property.
 *
 * <p>Translates domain and application exceptions into proper HTTP responses and logs each scenario
 * at the appropriate severity level:
 *
 * <ul>
 *   <li>{@link NoResourceFoundException} → 404 (DEBUG) — missing static resources like favicon
 *   <li>{@link com.petwise.domain.exceptions.NotFoundException} → 404 (WARN)
 *   <li>{@link com.petwise.domain.exceptions.NotificationException} → 422 (WARN)
 *   <li>{@link DomainException} → 400 (WARN)
 *   <li>{@link AccessDeniedException} → 403 (WARN)
 *   <li>{@link AuthenticationException} → 401 (WARN)
 *   <li>{@link Exception} → 500 (ERROR)
 * </ul>
 */
@RestControllerAdvice
@SuppressWarnings("PMD.ShortVariable")
public class GlobalExceptionHandler {
    /** Base URI prefix for RFC 7807 {@code type} identifiers. */
    private static final String ERROR_BASE_URI = "https://petwise.example.com/errors/";

    /** Class-level logger. */
    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** Default constructor. */
    public GlobalExceptionHandler() {}

    /**
     * Handles {@link NotFoundException} — entity not found.
     *
     * @param exception the exception
     * @param request the current HTTP request
     * @return 404 response with RFC 7807 problem details
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(
            final NotFoundException exception, final HttpServletRequest request) {
        if (LOG.isWarnEnabled()) {
            LOG.warn("Resource not found: {}", exception.getMessage());
        }
        final var problem =
                createProblemDetail(
                        HttpStatus.NOT_FOUND,
                        "not-found",
                        exception.getMessage(),
                        request,
                        exception.getErrors());
        return problemResponse(HttpStatus.NOT_FOUND, problem);
    }

    /**
     * Handles {@link NotificationException} — validation failures.
     *
     * @param exception the exception
     * @param request the current HTTP request
     * @return 422 response with RFC 7807 problem details
     */
    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<ProblemDetail> handleNotification(
            final NotificationException exception, final HttpServletRequest request) {
        if (LOG.isWarnEnabled()) {
            LOG.warn(
                    "Validation failed: {} — errors: {}",
                    exception.getMessage(),
                    exception.getErrors());
        }
        final var problem =
                createProblemDetail(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "validation-failed",
                        exception.getMessage(),
                        request,
                        exception.getErrors());
        return problemResponse(HttpStatus.UNPROCESSABLE_ENTITY, problem);
    }

    /**
     * Handles generic {@link DomainException} — domain rule violations.
     *
     * @param exception the exception
     * @param request the current HTTP request
     * @return 400 response with RFC 7807 problem details
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ProblemDetail> handleDomain(
            final DomainException exception, final HttpServletRequest request) {
        if (LOG.isWarnEnabled()) {
            LOG.warn(
                    "Domain rule violation: {} — errors: {}",
                    exception.getMessage(),
                    exception.getErrors());
        }
        final var problem =
                createProblemDetail(
                        HttpStatus.BAD_REQUEST,
                        "domain-rule-violation",
                        exception.getMessage(),
                        request,
                        exception.getErrors());
        return problemResponse(HttpStatus.BAD_REQUEST, problem);
    }

    /**
     * Handles {@link NoResourceFoundException} — missing static resources (e.g. favicon.ico).
     *
     * <p>Logged at DEBUG because this is expected behaviour for a pure REST API that serves no
     * static content. Browsers routinely request {@code /favicon.ico}, and this must not pollute
     * ERROR logs.
     *
     * @param exception the exception
     * @param request the current HTTP request
     * @return 404 response with RFC 7807 problem details
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ProblemDetail> handleNoResourceFound(
            final NoResourceFoundException exception, final HttpServletRequest request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Static resource not found: {}", exception.getMessage());
        }
        final var problem =
                createProblemDetail(
                        HttpStatus.NOT_FOUND,
                        "resource-not-found",
                        exception.getMessage(),
                        request,
                        List.of());
        return problemResponse(HttpStatus.NOT_FOUND, problem);
    }

    /**
     * Handles {@link AccessDeniedException} — authenticated user lacks the required role /
     * authority for the requested resource.
     *
     * @param exception the exception
     * @param request the current HTTP request
     * @return 403 response with RFC 7807 problem details
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDenied(
            final AccessDeniedException exception, final HttpServletRequest request) {
        if (LOG.isWarnEnabled()) {
            LOG.warn("Access denied: {}", exception.getMessage());
        }
        final var problem =
                createProblemDetail(
                        HttpStatus.FORBIDDEN,
                        "access-denied",
                        "Access denied — insufficient permissions",
                        request,
                        List.of());
        return problemResponse(HttpStatus.FORBIDDEN, problem);
    }

    /**
     * Handles {@link AuthenticationException} — request arrived without a valid Bearer token
     * (missing, expired, malformed, bad signature).
     *
     * @param exception the exception
     * @param request the current HTTP request
     * @return 401 response with RFC 7807 problem details
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ProblemDetail> handleAuthentication(
            final AuthenticationException exception, final HttpServletRequest request) {
        if (LOG.isWarnEnabled()) {
            LOG.warn("Authentication failed: {}", exception.getMessage());
        }
        final var detail =
                exception.getMessage() != null
                        ? exception.getMessage()
                        : "Authentication required — provide a valid Bearer token";
        final var problem =
                createProblemDetail(
                        HttpStatus.UNAUTHORIZED,
                        "authentication-required",
                        detail,
                        request,
                        List.of());
        return problemResponse(HttpStatus.UNAUTHORIZED, problem);
    }

    /**
     * Catch-all for unexpected exceptions.
     *
     * @param exception the exception
     * @param request the current HTTP request
     * @return 500 response with a generic RFC 7807 problem detail
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpected(
            final Exception exception, final HttpServletRequest request) {
        if (LOG.isErrorEnabled()) {
            LOG.error("Unexpected error occurred", exception);
        }
        final var problem =
                createProblemDetail(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "internal-error",
                        "An unexpected error occurred",
                        request,
                        List.of());
        return problemResponse(HttpStatus.INTERNAL_SERVER_ERROR, problem);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------
    /**
     * Builds a {@link ProblemDetail} conforming to RFC 7807.
     *
     * @param status the HTTP status
     * @param errorSlug short identifier appended to the base error URI (e.g. {@code "not-found"})
     * @param detail human-readable explanation of the problem
     * @param request the current HTTP request (used for the {@code instance} field)
     * @param errors domain validation errors (may be empty)
     * @return fully populated {@link ProblemDetail}
     */
    static ProblemDetail createProblemDetail(
            final HttpStatus status,
            final String errorSlug,
            final String detail,
            final HttpServletRequest request,
            final List<Error> errors) {
        final var problem = ProblemDetail.forStatus(status);
        problem.setType(URI.create(ERROR_BASE_URI + errorSlug));
        problem.setTitle(status.getReasonPhrase());
        problem.setDetail(detail);
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("errors", errors);
        return problem;
    }

    /**
     * Wraps a {@link ProblemDetail} in a {@link ResponseEntity} with {@code
     * application/problem+json} content type.
     *
     * @param status the HTTP status
     * @param problem the problem detail body
     * @return the response entity
     */
    private static ResponseEntity<ProblemDetail> problemResponse(
            final HttpStatus status, final ProblemDetail problem) {
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }
}
