package com.petwise.application.tutor.retrieve.list;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import com.petwise.domain.tutor.Email;
import com.petwise.domain.tutor.Phone;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorGateway;
import com.petwise.domain.tutor.TutorID;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Unit tests for {@link DefaultListTutorsUseCase}. */
@ExtendWith(MockitoExtension.class)
@DisplayName("ListTutorsUseCase Tests")
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate"
})
class ListTutorsUseCaseTest {

    /** Default constructor. */
    ListTutorsUseCaseTest() {}

    /** The mocked tutor gateway. */
    @Mock private TutorGateway tutorGateway;

    /** The use case under test. */
    @InjectMocks private DefaultListTutorsUseCase useCase;

    /** First test tutor. */
    private Tutor tutor1;

    /** Second test tutor. */
    private Tutor tutor2;

    /** Third test tutor. */
    private Tutor tutor3;

    /** The search query used in tests. */
    private SearchQuery searchQuery;

    @BeforeEach
    void setUp() {
        tutor1 =
                Tutor.with(
                        TutorID.unique(),
                        "John Doe",
                        Email.from("john@example.com"),
                        Phone.from("+5511987654321"),
                        Instant.now().minusSeconds(7200),
                        Instant.now().minusSeconds(3600));

        tutor2 =
                Tutor.with(
                        TutorID.unique(),
                        "Jane Smith",
                        Email.from("jane@example.com"),
                        null,
                        Instant.now().minusSeconds(5400),
                        Instant.now().minusSeconds(1800));

        tutor3 =
                Tutor.with(
                        TutorID.unique(),
                        "Bob Johnson",
                        null,
                        Phone.from("+5511912345678"),
                        Instant.now().minusSeconds(3600),
                        Instant.now().minusSeconds(900));

        searchQuery = new SearchQuery(0, 10, "", "name", "asc");
    }

    @Test
    @DisplayName("Should list tutors with pagination successfully")
    void givenExistingTutors_whenExecute_thenShouldReturnPaginatedResults() {
        // Given
        final var tutors = List.of(tutor1, tutor2, tutor3);
        final var pagination = new Pagination<>(0, 10, 3L, tutors);

        when(tutorGateway.findAll(searchQuery)).thenReturn(pagination);

        // When
        final var output = useCase.execute(searchQuery);

        // Then
        assertNotNull(output);
        assertEquals(0, output.currentPage());
        assertEquals(10, output.perPage());
        assertEquals(3L, output.total());
        assertNotNull(output.items());
        assertEquals(3, output.items().size());

        final var items = output.items();

        // Verify first tutor
        assertEquals(tutor1.getId().getValue(), items.get(0).id());
        assertEquals("John Doe", items.get(0).name());
        assertEquals("john@example.com", items.get(0).email());
        assertEquals("+5511987654321", items.get(0).phone());

        // Verify second tutor (no phone)
        assertEquals(tutor2.getId().getValue(), items.get(1).id());
        assertEquals("Jane Smith", items.get(1).name());
        assertEquals("jane@example.com", items.get(1).email());
        assertNull(items.get(1).phone());

        // Verify third tutor (no email)
        assertEquals(tutor3.getId().getValue(), items.get(2).id());
        assertEquals("Bob Johnson", items.get(2).name());
        assertNull(items.get(2).email());
        assertEquals("+5511912345678", items.get(2).phone());

        verify(tutorGateway).findAll(searchQuery);
    }

    @Test
    @DisplayName("Should return empty pagination when no tutors exist")
    void givenNoTutors_whenExecute_thenShouldReturnEmptyPagination() {
        // Given
        final var emptyPagination = new Pagination<Tutor>(0, 10, 0L, Collections.emptyList());

        when(tutorGateway.findAll(searchQuery)).thenReturn(emptyPagination);

        // When
        final var output = useCase.execute(searchQuery);

        // Then
        assertNotNull(output);
        assertEquals(0, output.currentPage());
        assertEquals(10, output.perPage());
        assertEquals(0L, output.total());
        assertNotNull(output.items());
        assertTrue(output.items().isEmpty());

        verify(tutorGateway).findAll(searchQuery);
    }

    @Test
    @DisplayName("Should handle paginated results correctly")
    void givenSecondPageQuery_whenExecute_thenShouldReturnCorrectPage() {
        // Given - Second page with 2 items, total of 5
        final var tutors = List.of(tutor1, tutor2);
        final var pagination = new Pagination<>(1, 2, 5L, tutors);
        final var query = new SearchQuery(1, 2, "", "name", "asc");

        when(tutorGateway.findAll(query)).thenReturn(pagination);

        // When
        final var output = useCase.execute(query);

        // Then
        assertNotNull(output);
        assertEquals(1, output.currentPage());
        assertEquals(2, output.perPage());
        assertEquals(5L, output.total());
        assertEquals(2, output.items().size());

        verify(tutorGateway).findAll(query);
    }

    @Test
    @DisplayName("Should handle tutors with all optional fields null")
    void givenTutorWithNullOptionalFields_whenExecute_thenShouldReturnNullFields() {
        // Given
        final var tutorMinimal =
                Tutor.with(
                        TutorID.unique(),
                        "Minimal Tutor",
                        null,
                        null,
                        Instant.now().minusSeconds(3600),
                        Instant.now().minusSeconds(1800));

        final var pagination = new Pagination<>(0, 10, 1L, List.of(tutorMinimal));

        when(tutorGateway.findAll(searchQuery)).thenReturn(pagination);

        // When
        final var output = useCase.execute(searchQuery);

        // Then
        assertNotNull(output);
        assertEquals(1, output.items().size());

        final var item = output.items().getFirst();
        assertEquals("Minimal Tutor", item.name());
        assertNull(item.email());
        assertNull(item.phone());

        verify(tutorGateway).findAll(searchQuery);
    }

    @Test
    @DisplayName("Should handle search with terms")
    void givenSearchTerms_whenExecute_thenShouldFilterResults() {
        // Given
        final var searchWithTerms = new SearchQuery(0, 10, "John", "name", "asc");
        final var pagination = new Pagination<>(0, 10, 1L, List.of(tutor1));

        when(tutorGateway.findAll(searchWithTerms)).thenReturn(pagination);

        // When
        final var output = useCase.execute(searchWithTerms);

        // Then
        assertNotNull(output);
        assertEquals(1, output.items().size());
        assertEquals("John Doe", output.items().getFirst().name());

        verify(tutorGateway).findAll(searchWithTerms);
    }

    @Test
    @DisplayName("Should throw NullPointerException when query is null")
    void givenNullQuery_whenExecute_thenShouldThrowNullPointerException() {
        // When & Then
        assertThrows(NullPointerException.class, () -> useCase.execute(null));

        verify(tutorGateway, never()).findAll(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when gateway is null")
    void givenNullGateway_whenConstruct_thenShouldThrowNullPointerException() {
        // When & Then
        assertThrows(NullPointerException.class, () -> new DefaultListTutorsUseCase(null));
    }
}
