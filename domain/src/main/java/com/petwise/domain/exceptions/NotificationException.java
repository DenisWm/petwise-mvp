package com.petwise.domain.exceptions;

import com.petwise.domain.validation.handler.Notification;

/** Raised when one or more validation constraints are violated during a use-case execution. */
@SuppressWarnings("PMD.MissingSerialVersionUID")
public class NotificationException extends DomainException {
    public NotificationException(final String message, final Notification notification) {
        super(message, notification.getErrors());
    }
}
