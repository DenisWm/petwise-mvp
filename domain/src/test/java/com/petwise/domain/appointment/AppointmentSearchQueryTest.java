package com.petwise.domain.appointment;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link AppointmentSearchQuery}. */
@DisplayName("AppointmentSearchQuery Tests")
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals"
})
class AppointmentSearchQueryTest {

    /** Default constructor. */
    AppointmentSearchQueryTest() {}

    @Test
    @DisplayName("Should create query with all parameters")
    void shouldCreateQueryWithAllParameters() {
        final var date = LocalDate.of(2026, 2, 26);
        final var query =
                new AppointmentSearchQuery(
                        date,
                        AppointmentStatus.ACTIVE,
                        ServiceType.CRECHE,
                        0,
                        20,
                        "startAt",
                        "asc");

        assertEquals(date, query.date());
        assertEquals(AppointmentStatus.ACTIVE, query.status());
        assertEquals(ServiceType.CRECHE, query.serviceType());
        assertEquals(0, query.page());
        assertEquals(20, query.perPage());
        assertEquals("startAt", query.sort());
        assertEquals("asc", query.direction());
    }

    @Test
    @DisplayName("Should allow null status and serviceType")
    void shouldAllowNullOptionalFilters() {
        final var date = LocalDate.of(2026, 2, 26);
        final var query = new AppointmentSearchQuery(date, null, null, 0, 10, "startAt", "asc");

        assertEquals(date, query.date());
        assertNull(query.status());
        assertNull(query.serviceType());
    }

    @Test
    @DisplayName("Should throw NullPointerException when date is null")
    void shouldThrowWhenDateIsNull() {
        assertThrows(
                NullPointerException.class,
                () -> new AppointmentSearchQuery(null, null, null, 0, 10, "startAt", "asc"));
    }
}
