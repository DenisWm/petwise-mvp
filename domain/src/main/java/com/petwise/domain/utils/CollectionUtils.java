package com.petwise.domain.utils;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/** Utility methods for working with collections in the domain layer. */
@SuppressWarnings({
    "PMD.ReturnEmptyCollectionRatherThanNull",
    "PMD.GenericNaming",
    "PMD.OnlyOneReturn"
})
public final class CollectionUtils {

    private CollectionUtils() {}

    /**
     * Maps each element of {@code set} using {@code mapper}. Returns {@code null} if input is null.
     */
    public static <I, O> Set<O> mapTo(final Set<I> list, final Function<I, O> mapper) {
        if (list == null) {
            return null;
        }
        return list.stream().map(mapper).collect(Collectors.toSet());
    }

    /** Returns {@code null} if {@code values} is null or empty; otherwise returns it unchanged. */
    public static <T> Set<T> nullIfEmpty(final Set<T> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values;
    }
}
