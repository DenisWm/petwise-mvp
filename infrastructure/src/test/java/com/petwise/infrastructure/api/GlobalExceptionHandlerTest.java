package com.petwise.infrastructure.api;

import static org.junit.jupiter.api.Assertions.*;

import com.petwise.domain.exceptions.DomainException;
import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.exceptions.NotificationException;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorID;
import com.petwise.domain.validation.Error;
import com.petwise.domain.validation.handler.Notification;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/** Unit tests for {@link GlobalExceptionHandler}. */
@DisplayName("GlobalExceptionHandler Tests")
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals",
    "PMD.TooManyMethods"
})
class GlobalExceptionHandlerTest {
    GlobalExceptionHandlerTest() {}

    /** Unit under test. */
    private GlobalExceptionHandler handler;

    /** Shared mock request for all tests. */
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = new MockHttpServletRequest("GET", "/api/tutors/tutor-123");
    }

    @Test
    @DisplayName("Should return 404 RFC 7807 ProblemDetail when NotFoundException is thrown")
    void givenNotFoundException_whenHandle_thenShouldReturn404() {
        final var tutorId = TutorID.from("tutor-123");
        final var exception = NotFoundException.with(Tutor.class, tutorId);
        final var response = handler.handleNotFound(exception, request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PROBLEM_JSON, response.getHeaders().getContentType());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals(404, body.getStatus());
        assertEquals("Not Found", body.getTitle());
        assertEquals(URI.create("https://petwise.example.com/errors/not-found"), body.getType());
        assertEquals(URI.create("/api/tutors/tutor-123"), body.getInstance());
        assertNotNull(body.getDetail());
        assertTrue(body.getDetail().contains("Tutor"));
        assertTrue(body.getDetail().contains("tutor-123"));
        final var props = body.getProperties();
        assertNotNull(props);
        @SuppressWarnings("unchecked")
        final var errors = (List<Error>) props.get("errors");
        assertNotNull(errors);
    }

    @Test
    @DisplayName("Should return 422 RFC 7807 ProblemDetail when NotificationException is thrown")
    void givenNotificationException_whenHandle_thenShouldReturn422() {
        final var notification = Notification.create();
        notification.append(new Error("'name' should not be null"));
        notification.append(new Error("'email' is not valid"));
        final var exception = new NotificationException("Validation failed", notification);
        final var response = handler.handleNotification(exception, request);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PROBLEM_JSON, response.getHeaders().getContentType());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals(422, body.getStatus());
        assertEquals("Unprocessable Entity", body.getTitle());
        assertEquals(
                URI.create("https://petwise.example.com/errors/validation-failed"), body.getType());
        assertEquals(URI.create("/api/tutors/tutor-123"), body.getInstance());
        assertEquals("Validation failed", body.getDetail());
        final var props = body.getProperties();
        assertNotNull(props);
        @SuppressWarnings("unchecked")
        final var errors = (List<Error>) props.get("errors");
        assertEquals(2, errors.size());
        assertEquals("'name' should not be null", errors.get(0).message());
        assertEquals("'email' is not valid", errors.get(1).message());
    }

    @Test
    @DisplayName("Should return 400 RFC 7807 ProblemDetail when DomainException is thrown")
    void givenDomainException_whenHandle_thenShouldReturn400() {
        final var exception =
                DomainException.with(
                        new Error("'status' cannot transition from COMPLETED to ACTIVE"));
        final var response = handler.handleDomain(exception, request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PROBLEM_JSON, response.getHeaders().getContentType());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertEquals("Bad Request", body.getTitle());
        assertEquals(
                URI.create("https://petwise.example.com/errors/domain-rule-violation"),
                body.getType());
        assertEquals(URI.create("/api/tutors/tutor-123"), body.getInstance());
        final var props = body.getProperties();
        assertNotNull(props);
        @SuppressWarnings("unchecked")
        final var errors = (List<Error>) props.get("errors");
        assertEquals(1, errors.size());
        assertEquals(
                "'status' cannot transition from COMPLETED to ACTIVE", errors.getFirst().message());
    }

    @Test
    @DisplayName(
            "Should return 400 RFC 7807 ProblemDetail when DomainException with multiple errors is"
                    + " thrown")
    void givenDomainExceptionWithMultipleErrors_whenHandle_thenShouldReturn400WithAllErrors() {
        final var domainErrors =
                List.of(new Error("'name' should not be null"), new Error("'phone' is invalid"));
        final var exception = DomainException.with(domainErrors);
        final var response = handler.handleDomain(exception, request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        final var body = response.getBody();
        assertNotNull(body);
        final var props = body.getProperties();
        assertNotNull(props);
        @SuppressWarnings("unchecked")
        final var errors = (List<Error>) props.get("errors");
        assertEquals(2, errors.size());
    }

    @Test
    @DisplayName("Should return 404 RFC 7807 ProblemDetail when NoResourceFoundException is thrown")
    void givenNoResourceFoundException_whenHandle_thenShouldReturn404() {
        final var faviconRequest = new MockHttpServletRequest("GET", "/favicon.ico");
        final var exception =
                new NoResourceFoundException(
                        org.springframework.http.HttpMethod.GET, "favicon.ico");
        final var response = handler.handleNoResourceFound(exception, faviconRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PROBLEM_JSON, response.getHeaders().getContentType());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals(404, body.getStatus());
        assertEquals("Not Found", body.getTitle());
        assertEquals(
                URI.create("https://petwise.example.com/errors/resource-not-found"),
                body.getType());
        assertEquals(URI.create("/favicon.ico"), body.getInstance());
        final var props = body.getProperties();
        assertNotNull(props);
        @SuppressWarnings("unchecked")
        final var errors = (List<Error>) props.get("errors");
        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("Should return 500 RFC 7807 ProblemDetail when unexpected Exception is thrown")
    void givenUnexpectedException_whenHandle_thenShouldReturn500() {
        final var exception = new RuntimeException("Something went wrong");
        final var response = handler.handleUnexpected(exception, request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PROBLEM_JSON, response.getHeaders().getContentType());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals(500, body.getStatus());
        assertEquals("Internal Server Error", body.getTitle());
        assertEquals(
                URI.create("https://petwise.example.com/errors/internal-error"), body.getType());
        assertEquals(URI.create("/api/tutors/tutor-123"), body.getInstance());
        assertEquals("An unexpected error occurred", body.getDetail());
        final var props = body.getProperties();
        assertNotNull(props);
        @SuppressWarnings("unchecked")
        final var errors = (List<Error>) props.get("errors");
        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("Should return 403 RFC 7807 ProblemDetail when AccessDeniedException is thrown")
    void givenAccessDeniedException_whenHandle_thenShouldReturn403() {
        final var exception =
                new org.springframework.security.access.AccessDeniedException("Forbidden");
        final var response = handler.handleAccessDenied(exception, request);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PROBLEM_JSON, response.getHeaders().getContentType());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals(403, body.getStatus());
        assertEquals("Forbidden", body.getTitle());
        assertEquals(
                URI.create("https://petwise.example.com/errors/access-denied"), body.getType());
        assertEquals(URI.create("/api/tutors/tutor-123"), body.getInstance());
    }

    @Test
    @DisplayName("Should return 401 RFC 7807 ProblemDetail when AuthenticationException is thrown")
    void givenAuthenticationException_whenHandle_thenShouldReturn401() {
        final var exception =
                new org.springframework.security.authentication.BadCredentialsException(
                        "An error occurred while attempting to decode the Jwt: Jwt expired at 2026-03-09T00:00:00Z");
        final var response = handler.handleAuthentication(exception, request);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PROBLEM_JSON, response.getHeaders().getContentType());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals(401, body.getStatus());
        assertEquals("Unauthorized", body.getTitle());
        assertEquals(
                URI.create("https://petwise.example.com/errors/authentication-required"),
                body.getType());
        assertEquals(URI.create("/api/tutors/tutor-123"), body.getInstance());
        assertNotNull(body.getDetail());
        assertTrue(body.getDetail().contains("Jwt expired"));
    }

    @Test
    @DisplayName(
            "Should return 401 with fallback detail when AuthenticationException has null message")
    void givenAuthenticationExceptionWithNullMessage_whenHandle_thenShouldReturnFallbackDetail() {
        final var exception =
                new org.springframework.security.authentication
                        .AuthenticationCredentialsNotFoundException(null);
        final var response = handler.handleAuthentication(exception, request);
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals(401, body.getStatus());
        assertEquals("Authentication required — provide a valid Bearer token", body.getDetail());
    }

    @Test
    @DisplayName("createProblemDetail should populate all RFC 7807 fields correctly")
    void givenInputs_whenCreateProblemDetail_thenShouldPopulateAllFields() {
        final var errors = List.of(new Error("test error"));
        final var problem =
                GlobalExceptionHandler.createProblemDetail(
                        HttpStatus.UNPROCESSABLE_ENTITY,
                        "validation-failed",
                        "Something is invalid",
                        request,
                        errors);
        assertEquals(422, problem.getStatus());
        assertEquals("Unprocessable Entity", problem.getTitle());
        assertEquals("Something is invalid", problem.getDetail());
        assertEquals(
                URI.create("https://petwise.example.com/errors/validation-failed"),
                problem.getType());
        assertEquals(URI.create("/api/tutors/tutor-123"), problem.getInstance());
        final var props = problem.getProperties();
        assertNotNull(props);
        @SuppressWarnings("unchecked")
        final var actualErrors = (List<Error>) props.get("errors");
        assertEquals(1, actualErrors.size());
        assertEquals("test error", actualErrors.getFirst().message());
    }
}
