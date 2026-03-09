package com.petwise.infrastructure.api;

import static org.junit.jupiter.api.Assertions.*;

import com.petwise.domain.exceptions.DomainException;
import com.petwise.domain.exceptions.NotFoundException;
import com.petwise.domain.exceptions.NotificationException;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorID;
import com.petwise.domain.validation.Error;
import com.petwise.domain.validation.handler.Notification;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/** Unit tests for {@link GlobalExceptionHandler}. */
@DisplayName("GlobalExceptionHandler Tests")
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals"
})
class GlobalExceptionHandlerTest {
    GlobalExceptionHandlerTest() {}

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    @DisplayName("Should return 404 when NotFoundException is thrown")
    void givenNotFoundException_whenHandle_thenShouldReturn404() {
        final var tutorId = TutorID.from("tutor-123");
        final var exception = NotFoundException.with(Tutor.class, tutorId);
        final var response = handler.handleNotFound(exception);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.NOT_FOUND.value(), body.status());
        assertTrue(body.message().contains("Tutor"));
        assertTrue(body.message().contains("tutor-123"));
    }

    @Test
    @DisplayName("Should return 422 when NotificationException is thrown")
    void givenNotificationException_whenHandle_thenShouldReturn422() {
        final var notification = Notification.create();
        notification.append(new Error("'name' should not be null"));
        notification.append(new Error("'email' is not valid"));
        final var exception = new NotificationException("Validation failed", notification);
        final var response = handler.handleNotification(exception);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), body.status());
        assertEquals("Validation failed", body.message());
        assertEquals(2, body.errors().size());
        assertEquals("'name' should not be null", body.errors().get(0).message());
        assertEquals("'email' is not valid", body.errors().get(1).message());
    }

    @Test
    @DisplayName("Should return 400 when DomainException is thrown")
    void givenDomainException_whenHandle_thenShouldReturn400() {
        final var exception =
                DomainException.with(
                        new Error("'status' cannot transition from COMPLETED to ACTIVE"));
        final var response = handler.handleDomain(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.status());
        assertEquals(1, body.errors().size());
        assertEquals(
                "'status' cannot transition from COMPLETED to ACTIVE",
                body.errors().getFirst().message());
    }

    @Test
    @DisplayName("Should return 400 when DomainException with multiple errors is thrown")
    void givenDomainExceptionWithMultipleErrors_whenHandle_thenShouldReturn400WithAllErrors() {
        final var errors =
                List.of(new Error("'name' should not be null"), new Error("'phone' is invalid"));
        final var exception = DomainException.with(errors);
        final var response = handler.handleDomain(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals(2, body.errors().size());
    }

    @Test
    @DisplayName("Should return 404 when NoResourceFoundException is thrown")
    void givenNoResourceFoundException_whenHandle_thenShouldReturn404() {
        final var exception =
                new NoResourceFoundException(
                        org.springframework.http.HttpMethod.GET, "favicon.ico");
        final var response = handler.handleNoResourceFound(exception);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.NOT_FOUND.value(), body.status());
        assertTrue(body.errors().isEmpty());
    }

    @Test
    @DisplayName("Should return 500 when unexpected Exception is thrown")
    void givenUnexpectedException_whenHandle_thenShouldReturn500() {
        final var exception = new RuntimeException("Something went wrong");
        final var response = handler.handleUnexpected(exception);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        final var body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.status());
        assertEquals("An unexpected error occurred", body.message());
        assertTrue(body.errors().isEmpty());
    }

    @Test
    @DisplayName("ApiError should store all fields correctly")
    void givenApiErrorCreation_whenAccessFields_thenShouldReturnCorrectValues() {
        final var errors = List.of(new Error("test error"));
        final var apiError = new GlobalExceptionHandler.ApiError(422, "Validation failed", errors);
        assertEquals(422, apiError.status());
        assertEquals("Validation failed", apiError.message());
        assertEquals(1, apiError.errors().size());
        assertEquals("test error", apiError.errors().getFirst().message());
    }

    @Test
    @DisplayName("ApiError should defensively copy errors list")
    void givenApiError_whenModifyOriginalList_thenShouldNotAffectApiError() {
        final var errors = new java.util.ArrayList<>(List.of(new Error("test error")));
        final var apiError = new GlobalExceptionHandler.ApiError(400, "Bad request", errors);
        errors.add(new Error("extra error"));
        assertEquals(1, apiError.errors().size());
    }
}
