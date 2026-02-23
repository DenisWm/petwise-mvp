package com.petwise.domain.pagination;

/**
 * Immutable value object that captures all parameters for a paginated search query.
 *
 * @param page zero-based page index requested by the caller
 * @param perPage number of items per page
 * @param terms optional free-text search terms; {@code null} or blank means no filtering
 * @param sort field name to sort by
 * @param direction sort direction – {@code "asc"} for ascending, {@code "desc"} for descending
 */
public record SearchQuery(int page, int perPage, String terms, String sort, String direction) {}
