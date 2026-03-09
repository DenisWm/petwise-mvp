package com.petwise.domain.pagination;

/** Captures all parameters for a paginated search query. */
public record SearchQuery(int page, int perPage, String terms, String sort, String direction) {}
