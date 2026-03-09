package com.petwise.infrastructure.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petwise.ControllerTest;
import com.petwise.application.pet.create.CreatePetOutput;
import com.petwise.application.pet.create.CreatePetUseCase;
import com.petwise.application.pet.delete.DeletePetUseCase;
import com.petwise.application.pet.retrieve.getbyid.GetPetByIdUseCase;
import com.petwise.application.pet.retrieve.getbyid.PetOutput;
import com.petwise.application.pet.retrieve.list.ListPetsOutput;
import com.petwise.application.pet.retrieve.list.ListPetsUseCase;
import com.petwise.application.pet.update.UpdatePetOutput;
import com.petwise.application.pet.update.UpdatePetUseCase;
import com.petwise.domain.pagination.Pagination;
import com.petwise.infrastructure.pet.api.PetController;
import com.petwise.infrastructure.pet.models.CreatePetRequest;
import com.petwise.infrastructure.pet.models.UpdatePetRequest;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/** Integration tests for the Pet REST API. */
@ControllerTest(controllers = PetController.class)
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.TooManyFields",
    "PMD.AvoidDuplicateLiterals",
    "PMD.LongVariable",
    "PMD.TooManyStaticImports",
    "PMD.ExcessiveImports"
})
class PetAPITest {
    PetAPITest() {}

    /** MockMvc for performing HTTP requests. */
    @Autowired private MockMvc mockMvc;

    /** Object mapper for JSON serialization. */
    @Autowired private ObjectMapper objectMapper;

    /** Mocked create pet use case. */
    @MockitoBean private CreatePetUseCase createPetUseCase;

    /** Mocked get pet by id use case. */
    @MockitoBean private GetPetByIdUseCase getPetByIdUseCase;

    /** Mocked list pets use case. */
    @MockitoBean private ListPetsUseCase listPetsUseCase;

    /** Mocked update pet use case. */
    @MockitoBean private UpdatePetUseCase updatePetUseCase;

    /** Mocked delete pet use case. */
    @MockitoBean private DeletePetUseCase deletePetUseCase;

    @Test
    void givenValidRequest_whenCreatePet_thenShouldReturnCreatedWithLocation() throws Exception {
        final var expectedId = "pet-123";
        final var request =
                new CreatePetRequest("tutor-id", "Fluffy", "Cat", "Persian", null, null);

        when(createPetUseCase.execute(any())).thenReturn(new CreatePetOutput(expectedId));
        mockMvc.perform(
                        post("/pets")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/pets/" + expectedId));

        verify(createPetUseCase, times(1)).execute(argThat(cmd -> "Fluffy".equals(cmd.name())));
    }

    @Test
    void givenValidId_whenGetPetById_thenShouldReturnPet() throws Exception {
        final var expectedId = "pet-123";
        final var output =
                new PetOutput(
                        expectedId,
                        "tutor-id",
                        "Fluffy",
                        "Cat",
                        "Persian",
                        null,
                        null,
                        Instant.now(),
                        Instant.now());

        when(getPetByIdUseCase.execute(expectedId)).thenReturn(output);
        mockMvc.perform(get("/pets/{id}", expectedId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo("Fluffy")))
                .andExpect(jsonPath("$.species", equalTo("Cat")));

        verify(getPetByIdUseCase, times(1)).execute(expectedId);
    }

    @Test
    void givenValidParams_whenListPets_thenShouldReturnPaginatedList() throws Exception {
        final var pet =
                new ListPetsOutput(
                        "pet-123",
                        "tutor-id",
                        "Fluffy",
                        "Cat",
                        null,
                        null,
                        Instant.now(),
                        Instant.now());

        final var expectedPagination = new Pagination<>(0, 10, 1L, List.of(pet));
        when(listPetsUseCase.execute(any())).thenReturn(expectedPagination);
        mockMvc.perform(get("/pets").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Fluffy")));

        verify(listPetsUseCase, times(1)).execute(any());
    }

    @Test
    void givenValidRequest_whenUpdatePet_thenShouldReturnOkWithLocation() throws Exception {
        final var expectedId = "pet-123";
        final var request = new UpdatePetRequest("Max", "Dog", "Labrador", null, null);

        when(updatePetUseCase.execute(any())).thenReturn(new UpdatePetOutput(expectedId));
        mockMvc.perform(
                        put("/pets/{id}", expectedId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(header().string("Location", "/pets/" + expectedId));

        verify(updatePetUseCase, times(1))
                .execute(argThat(cmd -> expectedId.equals(cmd.id()) && "Max".equals(cmd.name())));
    }

    @Test
    void givenValidId_whenDeletePet_thenShouldReturnNoContent() throws Exception {
        final var expectedId = "pet-123";
        doNothing().when(deletePetUseCase).execute(expectedId);
        mockMvc.perform(delete("/pets/{id}", expectedId)).andExpect(status().isNoContent());

        verify(deletePetUseCase, times(1)).execute(expectedId);
    }
}
