package com.sample.spring.domain.events;

@FunctionalInterface
public interface DomainEventPublisher {

    void publish(DomainEvent event);
}
