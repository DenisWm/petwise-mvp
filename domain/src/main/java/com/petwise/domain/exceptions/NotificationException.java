package com.petwise.domain.exceptions;

import com.petwise.domain.validation.handler.Notification;

public class NotificationException extends DomainException {

    public NotificationException(final String message, final Notification notification) {
        super(message, notification.getErrors());
    }
}
