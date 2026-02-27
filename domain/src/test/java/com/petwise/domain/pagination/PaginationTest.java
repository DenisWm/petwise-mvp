package com.petwise.domain.pagination;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.domain.UnitTest;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link Pagination}. */
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate"
})
class PaginationTest extends UnitTest {

    /** Default constructor. */
    PaginationTest() {}

    @Test
    void givenValidParams_whenCreate_thenShouldHoldValues() {
        final var pagination = new Pagination<>(0, 10, 3L, List.of("a", "b", "c"));
        assertThat(pagination.currentPage()).isZero();
        assertThat(pagination.perPage()).isEqualTo(10);
        assertThat(pagination.total()).isEqualTo(3L);
        assertThat(pagination.items()).containsExactly("a", "b", "c");
    }

    @Test
    void givenPagination_whenMap_thenShouldTransformItems() {
        final var pagination = new Pagination<>(1, 5, 2L, List.of("hello", "world"));
        final var mapped = pagination.map(String::toUpperCase);
        assertThat(mapped.currentPage()).isEqualTo(1);
        assertThat(mapped.perPage()).isEqualTo(5);
        assertThat(mapped.total()).isEqualTo(2L);
        assertThat(mapped.items()).containsExactly("HELLO", "WORLD");
    }

    @Test
    void givenPagination_whenCreate_thenItemsListShouldBeUnmodifiable() {
        final var pagination = new Pagination<>(0, 10, 1L, List.of("item"));
        assertThat(pagination.items()).isUnmodifiable();
    }
}
