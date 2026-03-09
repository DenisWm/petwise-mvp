package com.petwise.domain;

import com.petwise.domain.events.DomainEvent;
import com.petwise.domain.events.DomainEventPublisher;
import com.petwise.domain.validation.ValidationHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Base class for all domain entities. Equality is determined by identity, not field values.
 * Entities can accumulate {@link DomainEvent}s dispatched after transaction commit.
 *
 * @param <ID> the strongly-typed identifier for this entity
 */
@SuppressWarnings({"PMD.ShortVariable", "PMD.GenericsNaming"})
public abstract class Entity<ID extends Identifier<?>> {
    private final ID id;
    private final List<DomainEvent> domainEvents;

    /** Makes a defensive copy of {@code anDomainEvents} so the caller's list is never mutated. */
    protected Entity(final ID anId, final List<DomainEvent> anDomainEvents) {
        this.domainEvents =
                new ArrayList<>(anDomainEvents == null ? Collections.emptyList() : anDomainEvents);
        Objects.requireNonNull(anId, "'id' should not be null");
        this.id = anId;
    }

    public abstract void validate(ValidationHandler handler);

    public ID getId() {
        return id;
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * Publishes all pending events through {@code publisher} and clears the list. No-op if null.
     */
    public void publishDomainEvents(final DomainEventPublisher publisher) {
        if (publisher == null) {
            return;
        }
        getDomainEvents().forEach(publisher::publish);
        this.domainEvents.clear();
    }

    /** Adds a domain event to the pending list. Null events are silently ignored. */
    public void registerEvent(final DomainEvent event) {
        if (event != null) {
            this.domainEvents.add(event);
        }
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
