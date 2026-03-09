package com.petwise.domain.pagination;

import java.util.List;
import java.util.function.Function;

/**
 * Immutable container for a single page of query results.
 *
 * @param <T> the type of items in this page
 */
public record Pagination<T>(int currentPage, int perPage, long total, List<T> items) {
    /** Defensive copy — stored list is unmodifiable. */
    public Pagination {
        items = List.copyOf(items);
    }

    public <R> Pagination<R> map(final Function<T, R> mapper) {
        final List<R> mappedItems = this.items.stream().map(mapper).toList();
        return new Pagination<>(currentPage(), perPage(), total(), mappedItems);
    }
}
