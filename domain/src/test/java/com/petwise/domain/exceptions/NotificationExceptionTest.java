package com.petwise.domain.exceptions;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.domain.UnitTest;
import com.petwise.domain.validation.Error;
import com.petwise.domain.validation.handler.Notification;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link NotificationException}. */
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.ShortVariable"
})
class NotificationExceptionTest extends UnitTest {

    /** Default constructor. */
    NotificationExceptionTest() {}

    @Test
    void givenNotification_whenConstructed_thenShouldExposeErrors() {
        final var notification = Notification.create(new Error("validation failed"));
        final var ex = new NotificationException("Create failed", notification);
        assertThat(ex.getMessage()).isEqualTo("Create failed");
        assertThat(ex.getErrors()).hasSize(1);
        assertThat(ex.getErrors().getFirst().message()).isEqualTo("validation failed");
    }

    @Test
    void givenMultipleErrors_whenConstructed_thenShouldExposeAllErrors() {
        final var notification = Notification.create();
        notification.append(new Error("error one"));
        notification.append(new Error("error two"));
        final var ex = new NotificationException("Multiple errors", notification);
        assertThat(ex.getErrors()).hasSize(2);
    }
}
