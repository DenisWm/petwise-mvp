package com.petwise.domain.events;

import java.io.Serializable;
import java.time.Instant;

/**
 * Marker interface for domain events — immutable facts about something that happened. Name
 * implementations in past tense (e.g., {@code TutorRegistered}). Prefer records or final classes.
 */
@SuppressWarnings("PMD.ImplicitFunctionalInterface")
public interface DomainEvent extends Serializable {
    Instant occurredOn();
}
