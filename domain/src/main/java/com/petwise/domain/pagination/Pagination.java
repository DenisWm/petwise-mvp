package com.petwise.domain.pagination;

import java.util.List;
import java.util.function.Function;

/**
 * Immutable container for a single page of query results.
 *
 * @param <T> the type of items in this page
 * @param currentPage zero-based page index
 * @param perPage maximum number of items per page
 * @param total total number of items across all pages
 * @param items the items on the current page
 */
public record Pagination<T>(int currentPage, int perPage, long total, List<T> items) {

    public Pagination {
        items = List.copyOf(items);
    }

    /**
     * Maps each item in this page to a new type using the supplied {@code mapper}, preserving the
     * pagination metadata.
     *
     * @param <R> the target item type
     * @param mapper a function to transform each item
     * @return a new {@code Pagination} with transformed items
     */
    public <R> Pagination<R> map(final Function<T, R> mapper) {
        final List<R> mappedItems = this.items.stream().map(mapper).toList();
        return new Pagination<>(currentPage(), perPage(), total(), mappedItems);
    }
}
