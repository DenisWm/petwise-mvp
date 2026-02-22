package com.petwise.domain.exceptions;

import com.petwise.domain.validation.handler.Notification;

/**
 * Exception raised when one or more domain validation constraints are violated during a use-case
 * execution.
 *
 * <p>It wraps a {@link Notification} so that all accumulated errors are accessible through {@link
 * #getErrors()}, enabling the presentation layer to return detailed feedback to the caller.
 */
@SuppressWarnings("PMD.MissingSerialVersionUID")
public class NotificationException extends DomainException {

    /**
     * Constructs a {@code NotificationException} with a descriptive message and the {@link
     * Notification} holding all collected errors.
     *
     * @param message a high-level description of what failed
     * @param notification the notification holding the validation errors; must not be {@code null}
     */
    public NotificationException(final String message, final Notification notification) {
        super(message, notification.getErrors());
    }
}
