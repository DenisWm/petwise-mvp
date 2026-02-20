package com.petwise.domain;

import com.petwise.domain.events.DomainEvent;
import java.util.Collections;
import java.util.List;

/**
 * Base class for all Aggregate Roots in the domain model.
 *
 * <p>An Aggregate Root is a specific type of {@link Entity} that acts as the entry point to an
 * aggregate — a cluster of domain objects treated as a single unit for data changes. All external
 * access to objects within the aggregate must go through the root.
 *
 * <p>Aggregate Roots are the only objects the repository layer works with directly.
 *
 * @param <ID> the strongly-typed identifier for this aggregate root
 * @see Entity
 */
public abstract class AggregateRoot<ID extends Identifier<?>> extends Entity<ID> {

    /**
     * Constructs an aggregate root with a generated identifier and no initial domain events.
     *
     * @param id the aggregate's identity; must not be {@code null}
     */
    public AggregateRoot(final ID id) {
        this(id, Collections.emptyList());
    }

    /**
     * Constructs an aggregate root with a generated identifier and a list of pre-existing events.
     *
     * @param id           the aggregate's identity; must not be {@code null}
     * @param domainEvents initial domain events; may be {@code null} (treated as empty)
     */
    public AggregateRoot(final ID id, final List<DomainEvent> domainEvents) {
        super(id, domainEvents);
    }
}
