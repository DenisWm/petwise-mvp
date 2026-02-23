package com.petwise.domain;

import org.junit.jupiter.api.Tag;

/**
 * Base class for domain unit tests.
 *
 * <p>Annotates subclasses with the {@code unitTest} tag so that the build system can include or
 * exclude them selectively (e.g., run only unit tests with {@code ./gradlew test -PunitTest}). No
 * Spring context is loaded — tests should be fast and isolated.
 */
@Tag("unitTest")
@SuppressWarnings("PMD.TestClassWithoutTestCases")
public class UnitTest {

    /** Default constructor. */
    protected UnitTest() {}
}
