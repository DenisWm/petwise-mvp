package com.petwise.infrastructure.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petwise.application.tutor.create.CreateTutorOutput;
import com.petwise.application.tutor.create.CreateTutorUseCase;
import com.petwise.application.tutor.delete.DeleteTutorUseCase;
import com.petwise.application.tutor.retrieve.getbyid.GetTutorByIdUseCase;
import com.petwise.application.tutor.retrieve.getbyid.TutorOutput;
import com.petwise.application.tutor.retrieve.list.ListTutorsOutput;
import com.petwise.application.tutor.retrieve.list.ListTutorsUseCase;
import com.petwise.application.tutor.update.UpdateTutorOutput;
import com.petwise.application.tutor.update.UpdateTutorUseCase;
import com.petwise.domain.pagination.Pagination;
import com.petwise.ControllerTest;
import com.petwise.infrastructure.tutor.api.TutorController;
import com.petwise.infrastructure.tutor.models.CreateTutorRequest;
import com.petwise.infrastructure.tutor.models.UpdateTutorRequest;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ControllerTest(controllers = TutorController.class)
public class TutorAPITest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private CreateTutorUseCase createTutorUseCase;

    @MockitoBean private GetTutorByIdUseCase getTutorByIdUseCase;

    @MockitoBean private ListTutorsUseCase listTutorsUseCase;

    @MockitoBean private UpdateTutorUseCase updateTutorUseCase;

    @MockitoBean private DeleteTutorUseCase deleteTutorUseCase;

    @Test
    void givenValidRequest_whenCreateTutor_thenShouldReturnCreatedWithLocation() throws Exception {
        // given
        final var expectedId = "tutor-123";
        final var expectedName = "John Doe";
        final var expectedEmail = "john@example.com";
        final var expectedPhone = "+1234567890";

        final var request = new CreateTutorRequest(expectedName, expectedEmail, expectedPhone);

        when(createTutorUseCase.execute(any())).thenReturn(new CreateTutorOutput(expectedId));

        // when & then
        mockMvc.perform(
                        post("/tutors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/tutors/" + expectedId));

        verify(createTutorUseCase, times(1))
                .execute(
                        argThat(
                                cmd ->
                                        cmd.name().equals(expectedName)
                                                && cmd.email().equals(expectedEmail)
                                                && cmd.phone().equals(expectedPhone)));
    }

    @Test
    void givenValidId_whenGetTutorById_thenShouldReturnTutor() throws Exception {
        // given
        final var expectedId = "tutor-123";
        final var expectedName = "John Doe";
        final var expectedEmail = "john@example.com";
        final var expectedPhone = "+1234567890";
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();

        final var output =
                new TutorOutput(
                        expectedId,
                        expectedName,
                        expectedEmail,
                        expectedPhone,
                        expectedCreatedAt,
                        expectedUpdatedAt);

        when(getTutorByIdUseCase.execute(expectedId)).thenReturn(output);

        // when & then
        mockMvc.perform(get("/tutors/{id}", expectedId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.email", equalTo(expectedEmail)))
                .andExpect(jsonPath("$.phone", equalTo(expectedPhone)));

        verify(getTutorByIdUseCase, times(1)).execute(expectedId);
    }

    @Test
    void givenValidParams_whenListTutors_thenShouldReturnPaginatedList() throws Exception {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSearch = "John";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 1L;

        final var tutor =
                new ListTutorsOutput(
                        "tutor-123",
                        "John Doe",
                        "john@example.com",
                        "+1234567890",
                        Instant.now(),
                        Instant.now());

        final var expectedPagination =
                new Pagination<>(expectedPage, expectedPerPage, expectedTotal, List.of(tutor));

        when(listTutorsUseCase.execute(any())).thenReturn(expectedPagination);

        // when & then
        mockMvc.perform(
                        get("/tutors")
                                .param("page", String.valueOf(expectedPage))
                                .param("perPage", String.valueOf(expectedPerPage))
                                .param("search", expectedSearch)
                                .param("sort", expectedSort)
                                .param("direction", expectedDirection)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo((int) expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].id", equalTo("tutor-123")))
                .andExpect(jsonPath("$.items[0].name", equalTo("John Doe")));

        verify(listTutorsUseCase, times(1))
                .execute(
                        argThat(
                                query ->
                                        query.page() == expectedPage
                                                && query.perPage() == expectedPerPage
                                                && query.terms().equals(expectedSearch)
                                                && query.sort().equals(expectedSort)
                                                && query.direction().equals(expectedDirection)));
    }

    @Test
    void givenDefaultParams_whenListTutors_thenShouldUseDefaults() throws Exception {
        // given
        final var expectedPagination = new Pagination<ListTutorsOutput>(0, 10, 0L, List.of());

        when(listTutorsUseCase.execute(any())).thenReturn(expectedPagination);

        // when & then
        mockMvc.perform(get("/tutors").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(listTutorsUseCase, times(1))
                .execute(
                        argThat(
                                query ->
                                        query.page() == 0
                                                && query.perPage() == 10
                                                && query.terms().isEmpty()
                                                && query.sort().equals("name")
                                                && query.direction().equals("asc")));
    }

    @Test
    void givenValidRequest_whenUpdateTutor_thenShouldReturnOkWithLocation() throws Exception {
        // given
        final var expectedId = "tutor-123";
        final var expectedName = "John Updated";
        final var expectedEmail = "john.updated@example.com";
        final var expectedPhone = "+9876543210";

        final var request = new UpdateTutorRequest(expectedName, expectedEmail, expectedPhone);

        when(updateTutorUseCase.execute(any())).thenReturn(new UpdateTutorOutput(expectedId));

        // when & then
        mockMvc.perform(
                        put("/tutors/{id}", expectedId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(header().string("Location", "/tutors/" + expectedId));

        verify(updateTutorUseCase, times(1))
                .execute(
                        argThat(
                                cmd ->
                                        cmd.id().equals(expectedId)
                                                && cmd.name().equals(expectedName)
                                                && cmd.email().equals(expectedEmail)
                                                && cmd.phone().equals(expectedPhone)));
    }

    @Test
    void givenValidId_whenDeleteTutor_thenShouldReturnNoContent() throws Exception {
        // given
        final var expectedId = "tutor-123";

        doNothing().when(deleteTutorUseCase).execute(expectedId);

        // when & then
        mockMvc.perform(delete("/tutors/{id}", expectedId)).andExpect(status().isNoContent());

        verify(deleteTutorUseCase, times(1)).execute(expectedId);
    }

    @Test
    void givenNullEmail_whenCreateTutor_thenShouldStillWork() throws Exception {
        // given
        final var expectedId = "tutor-123";
        final var expectedName = "John Doe";
        final var expectedPhone = "+1234567890";

        final var request = new CreateTutorRequest(expectedName, null, expectedPhone);

        when(createTutorUseCase.execute(any())).thenReturn(new CreateTutorOutput(expectedId));

        // when & then
        mockMvc.perform(
                        post("/tutors")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/tutors/" + expectedId));

        verify(createTutorUseCase, times(1))
                .execute(
                        argThat(
                                cmd ->
                                        cmd.name().equals(expectedName)
                                                && cmd.email() == null
                                                && cmd.phone().equals(expectedPhone)));
    }

    @Test
    void givenTutorWithNullEmail_whenGetTutorById_thenShouldNotRenderEmailField() throws Exception {
        // given
        final var expectedId = "tutor-123";
        final var expectedName = "John Doe";
        final var expectedPhone = "+1234567890";

        final var output =
                new TutorOutput(
                        expectedId,
                        expectedName,
                        null, // null email
                        expectedPhone,
                        Instant.now(),
                        Instant.now());

        when(getTutorByIdUseCase.execute(expectedId)).thenReturn(output);

        // when & then
        mockMvc.perform(get("/tutors/{id}", expectedId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.email").doesNotExist()) // email should not be rendered
                .andExpect(jsonPath("$.phone", equalTo(expectedPhone)));

        verify(getTutorByIdUseCase, times(1)).execute(expectedId);
    }
}
