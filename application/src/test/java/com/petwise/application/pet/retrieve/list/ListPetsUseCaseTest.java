package com.petwise.application.pet.retrieve.list;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import com.petwise.domain.pet.Pet;
import com.petwise.domain.pet.PetGateway;
import com.petwise.domain.pet.PetID;
import com.petwise.domain.tutor.TutorID;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Unit tests for {@link DefaultListPetsUseCase}. */
@ExtendWith(MockitoExtension.class)
@DisplayName("ListPetsUseCase Tests")
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate"
})
class ListPetsUseCaseTest {
    ListPetsUseCaseTest() {}

    @Mock private PetGateway petGateway;
    @InjectMocks private DefaultListPetsUseCase useCase;

    @Test
    @DisplayName("Should return paginated list of pets")
    void shouldReturnPaginatedListOfPets() {
        final var query = new SearchQuery(0, 10, "", "name", "asc");
        final var pet =
                Pet.with(
                        PetID.unique(),
                        TutorID.unique(),
                        "Fluffy",
                        "Cat",
                        null,
                        null,
                        null,
                        Instant.now(),
                        Instant.now());

        final var pagination = new Pagination<>(0, 10, 1, List.of(pet));
        when(petGateway.findAll(query)).thenReturn(pagination);
        final var result = useCase.execute(query);
        assertNotNull(result);
        assertEquals(1, result.total());
        assertEquals("Fluffy", result.items().get(0).name());

        verify(petGateway).findAll(query);
    }

    @Test
    @DisplayName("Should throw NullPointerException when query is null")
    void shouldThrowNullPointerExceptionWhenQueryIsNull() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verify(petGateway, never()).findAll(any());
    }

    @Test
    @DisplayName("Should throw NullPointerException when gateway is null")
    void shouldThrowNullPointerExceptionWhenGatewayIsNull() {
        assertThrows(NullPointerException.class, () -> new DefaultListPetsUseCase(null));
    }
}
