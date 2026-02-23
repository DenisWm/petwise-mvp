package com.petwise.domain.events;

import java.io.Serializable;
import java.time.Instant;

/**
 * Marker interface for all domain events.
 *
 * <p>A domain event represents something that happened in the domain that domain experts care
 * about. Events are immutable and should be named in past tense (e.g., {@code TutorRegistered}).
 *
 * <p>Implement this interface on records or final classes to ensure immutability.
 *
 * <p>Implement this interface on records or final classes to ensure immutability.
 */
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface DomainEvent extends Serializable {

    /**
     * Returns the instant at which this event occurred.
     *
     * @return the event timestamp; never {@code null}
     */
    Instant occurredOn();
}
