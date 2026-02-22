package com.petwise.domain;

import com.petwise.domain.events.DomainEvent;
import com.petwise.domain.events.DomainEventPublisher;
import com.petwise.domain.validation.ValidationHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Base class for all domain entities.
 *
 * <p>An entity is an object with a distinct identity that runs through time and different
 * representations. Equality is determined by {@link #getId()} rather than by field values. Entities
 * can accumulate {@link DomainEvent}s that are dispatched once the transaction commits.
 *
 * @param <ID> the strongly-typed identifier for this entity
 */
@SuppressWarnings({"PMD.ShortVariable", "PMD.GenericsNaming"})
public abstract class Entity<ID extends Identifier<?>> {

    /** The entity's strongly-typed identifier. */
    private final ID id;

    /** Pending domain events to be dispatched after the transaction commits. */
    private final List<DomainEvent> domainEvents;

    /**
     * Constructs an entity with the given identifier and an optional list of pre-existing events.
     *
     * <p>A defensive copy of {@code domainEvents} is always made, so the caller's list is never
     * mutated by this entity.
     *
     * @param anId the entity's identity; must not be {@code null}
     * @param anDomainEvents initial domain events; may be {@code null}
     * @throws NullPointerException if {@code anId} is {@code null}
     */
    protected Entity(final ID anId, final List<DomainEvent> anDomainEvents) {
        this.domainEvents =
                new ArrayList<>(anDomainEvents == null ? Collections.emptyList() : anDomainEvents);
        Objects.requireNonNull(anId, "'id' should not be null");
        this.id = anId;
    }

    /**
     * Validates the entity's invariants using the supplied {@link ValidationHandler}.
     *
     * <p>Implementations should append every constraint violation to {@code handler} rather than
     * throwing exceptions directly, allowing all errors to be collected in a single pass.
     *
     * @param handler the handler that accumulates validation errors
     */
    public abstract void validate(ValidationHandler handler);

    /**
     * Returns the entity's identifier.
     *
     * @return the entity ID; never {@code null}
     */
    public ID getId() {
        return id;
    }

    /**
     * Returns an unmodifiable view of the pending domain events.
     *
     * @return an unmodifiable list of domain events; never {@code null}
     */
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * Publishes all pending domain events through the given {@link DomainEventPublisher} and then
     * clears the internal event list.
     *
     * <p>If {@code publisher} is {@code null} this method is a no-op.
     *
     * @param publisher the publisher responsible for dispatching events
     */
    public void publishDomainEvents(final DomainEventPublisher publisher) {
        if (publisher == null) {
            return;
        }
        getDomainEvents().forEach(publisher::publish);
        this.domainEvents.clear();
    }

    /**
     * Adds a domain event to the pending event list.
     *
     * <p>Null events are silently ignored.
     *
     * @param event the domain event to register; ignored if null
     */
    public void registerEvent(final DomainEvent event) {
        if (event != null) {
            this.domainEvents.add(event);
        }
    }

    /**
     * Checks equality based on the entity's identifier.
     *
     * <p>Subclasses that need field-level equality must override this method and provide a safe
     * implementation.
     *
     * @param other the object to compare with
     * @return {@code true} if both entities share the same identifier
     */
    @Override
    public boolean equals(final Object other) {
        final boolean result;
        if (this == other) {
            result = true;
        } else if (other == null || getClass() != other.getClass()) {
            result = false;
        } else {
            final Entity<?> entity = (Entity<?>) other;
            result = Objects.equals(getId(), entity.getId());
        }
        return result;
    }

    /**
     * Returns a hash code based on the entity's identifier.
     *
     * <p>Subclasses that override {@link #equals} must also override this method consistently.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
