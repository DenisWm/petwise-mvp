package com.petwise.domain.utils;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility methods for working with {@link java.util.Collection}s in the domain layer.
 *
 * <p>This is a non-instantiable utility class — all methods are static.
 */
public final class CollectionUtils {

    private CollectionUtils() {}

    /**
     * Transforms each element of {@code set} using {@code mapper} and collects the results into a
     * new {@link Set}.
     *
     * @param <IN> the source element type
     * @param <OUT> the target element type
     * @param list the input set; if {@code null}, {@code null} is returned
     * @param mapper the transformation function; must not be {@code null}
     * @return a new {@code Set} of mapped elements, or {@code null} if {@code set} is {@code null}
     */
    public static <IN, OUT> Set<OUT> mapTo(final Set<IN> list, final Function<IN, OUT> mapper) {
        if (list == null) {
            return null;
        }
        return list.stream().map(mapper).collect(Collectors.toSet());
    }

    /**
     * Returns {@code null} if {@code values} is {@code null} or empty; otherwise returns {@code
     * values} unchanged.
     *
     * <p>Useful for normalising optional collection-type fields to {@code null} so that
     * serialisation layers can omit them cleanly.
     *
     * @param <T> the element type
     * @param values the set to inspect
     * @return {@code values} if non-empty, {@code null} otherwise
     */
    public static <T> Set<T> nullIfEmpty(final Set<T> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values;
    }
}
