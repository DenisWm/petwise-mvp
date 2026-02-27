package com.petwise.domain.validation.handler;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.domain.UnitTest;
import com.petwise.domain.exceptions.DomainException;
import com.petwise.domain.validation.Error;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link Notification}. */
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals"
})
class NotificationTest extends UnitTest {

    /** Default constructor. */
    NotificationTest() {}

    @Test
    void givenCreate_whenEmpty_thenShouldHaveNoErrors() {
        final var notification = Notification.create();
        assertThat(notification.hasErrors()).isFalse();
        assertThat(notification.getErrors()).isEmpty();
        assertThat(notification.firstError()).isNull();
    }

    @Test
    void givenCreateWithError_whenChecked_thenShouldContainError() {
        final var error = new Error("something went wrong");
        final var notification = Notification.create(error);
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.getErrors()).hasSize(1);
        assertThat(notification.firstError().message()).isEqualTo("something went wrong");
    }

    @Test
    void givenCreateWithThrowable_whenChecked_thenShouldContainThrowableMessage() {
        final var throwable = new RuntimeException("boom");
        final var notification = Notification.create(throwable);
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.firstError().message()).isEqualTo("boom");
    }

    @Test
    void givenNotification_whenAppendError_thenShouldAccumulateErrors() {
        final var notification = Notification.create();
        notification.append(new Error("first"));
        notification.append(new Error("second"));
        assertThat(notification.getErrors()).hasSize(2);
    }

    @Test
    void givenNotification_whenAppendHandler_thenShouldMergeErrors() {
        final var source = Notification.create(new Error("from source"));
        final var target = Notification.create();
        target.append(source);
        assertThat(target.getErrors()).hasSize(1);
        assertThat(target.firstError().message()).isEqualTo("from source");
    }

    @Test
    void givenValidation_whenValidateSucceeds_thenShouldReturnResult() {
        final var notification = Notification.create();
        final var result = notification.validate(() -> "ok");
        assertThat(result).isEqualTo("ok");
        assertThat(notification.hasErrors()).isFalse();
    }

    @Test
    void givenValidation_whenValidateThrowsDomainException_thenShouldCaptureErrors() {
        final var notification = Notification.create();
        final var result =
                notification.validate(
                        () -> {
                            throw DomainException.with(new Error("domain error"));
                        });
        assertThat(result).isNull();
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.firstError().message()).isEqualTo("domain error");
    }

    @Test
    void givenValidation_whenValidateThrowsRuntimeException_thenShouldCaptureMessage() {
        final var notification = Notification.create();
        final var result =
                notification.validate(
                        () -> {
                            throw new IllegalStateException("runtime error");
                        });
        assertThat(result).isNull();
        assertThat(notification.hasErrors()).isTrue();
        assertThat(notification.firstError().message()).isEqualTo("runtime error");
    }

    @Test
    void givenErrorsList_whenGetErrors_thenShouldReturnUnmodifiableCopy() {
        final var notification = Notification.create(new Error("e1"));
        final var errors = notification.getErrors();
        assertThat(errors).isUnmodifiable();
    }
}
