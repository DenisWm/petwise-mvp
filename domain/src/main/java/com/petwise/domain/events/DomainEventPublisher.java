package com.petwise.domain.events;

@FunctionalInterface
public interface DomainEventPublisher {

  void publish(DomainEvent event);
}
