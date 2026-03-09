package com.petwise.domain.events;

/** Dispatches {@link DomainEvent}s to subscribers (event bus, message broker, etc.). */
@FunctionalInterface
public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
