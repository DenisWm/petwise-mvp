package com.petwise.domain.pagination;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.domain.UnitTest;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link SearchQuery}. */
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate"
})
class SearchQueryTest extends UnitTest {
    SearchQueryTest() {}

    @Test
    void givenValidParams_whenCreate_thenShouldHoldValues() {
        final var query = new SearchQuery(0, 10, "fluffy", "name", "asc");
        assertThat(query.page()).isZero();
        assertThat(query.perPage()).isEqualTo(10);
        assertThat(query.terms()).isEqualTo("fluffy");
        assertThat(query.sort()).isEqualTo("name");
        assertThat(query.direction()).isEqualTo("asc");
    }

    @Test
    void givenNullTerms_whenCreate_thenShouldAllowNull() {
        final var query = new SearchQuery(1, 20, null, "createdAt", "desc");
        assertThat(query.terms()).isNull();
        assertThat(query.direction()).isEqualTo("desc");
    }
}
