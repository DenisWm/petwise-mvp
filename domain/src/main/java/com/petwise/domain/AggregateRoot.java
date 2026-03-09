package com.petwise.domain;

import com.petwise.domain.events.DomainEvent;
import java.util.Collections;
import java.util.List;

/**
 * Entry point to an aggregate — a cluster of domain objects treated as a single unit for data
 * changes. Only aggregate roots are persisted directly by the repository layer.
 *
 * @param <ID> the strongly-typed identifier for this aggregate root
 */
@SuppressWarnings({"PMD.ShortVariable", "PMD.GenericsNaming"})
public abstract class AggregateRoot<ID extends Identifier<?>> extends Entity<ID> {
    public AggregateRoot(final ID id) {
        this(id, Collections.emptyList());
    }

    public AggregateRoot(final ID id, final List<DomainEvent> domainEvents) {
        super(id, domainEvents);
    }
}
