package com.petwise.domain.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.domain.UnitTest;
import java.util.Locale;
import java.util.Set;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link CollectionUtils}. */
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate"
})
class CollectionUtilsTest extends UnitTest {
    /** Default constructor. */
    CollectionUtilsTest() {}

    @Test
    void givenNonNullSet_whenMapTo_thenShouldReturnMappedSet() {
        final var input = Set.of("a", "b", "c");
        final var result = CollectionUtils.mapTo(input, s -> s.toUpperCase(Locale.ROOT));
        assertThat(result).containsExactlyInAnyOrder("A", "B", "C");
    }

    @Test
    void givenNullSet_whenMapTo_thenShouldReturnNull() {
        final Set<String> nullSet = null;
        final var result = CollectionUtils.mapTo(nullSet, s -> s.toUpperCase(Locale.ROOT));
        assertThat(result).isNull();
    }

    @Test
    void givenNonEmptySet_whenNullIfEmpty_thenShouldReturnSameSet() {
        final var input = Set.of("x", "y");
        final var result = CollectionUtils.nullIfEmpty(input);
        assertThat(result).isSameAs(input);
    }

    @Test
    void givenEmptySet_whenNullIfEmpty_thenShouldReturnNull() {
        final var result = CollectionUtils.nullIfEmpty(Set.of());
        assertThat(result).isNull();
    }

    @Test
    void givenNullSet_whenNullIfEmpty_thenShouldReturnNull() {
        final Set<String> nullSet = null;
        final var result = CollectionUtils.nullIfEmpty(nullSet);
        assertThat(result).isNull();
    }
}
