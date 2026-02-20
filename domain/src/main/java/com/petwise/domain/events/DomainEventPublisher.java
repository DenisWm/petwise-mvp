package com.petwise.domain.events;

/**
 * Functional interface for publishing {@link DomainEvent}s.
 *
 * <p>Implementations are responsible for dispatching events to the appropriate subscribers
 * (e.g., an application event bus or a message broker). An entity calls
 * {@link com.petwise.domain.Entity#publishDomainEvents(DomainEventPublisher)} passing an
 * implementation of this interface after a successful transaction.
 */
@FunctionalInterface
public interface DomainEventPublisher {

    /**
     * Publishes the given domain event to all interested subscribers.
     *
     * @param event the event to publish; must not be {@code null}
     */
    void publish(DomainEvent event);
}
