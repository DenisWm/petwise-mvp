package com.petwise.infrastructure.tutor.api;

import com.petwise.domain.pagination.Pagination;
import com.petwise.infrastructure.tutor.models.CreateTutorRequest;
import com.petwise.infrastructure.tutor.models.TutorResponse;
import com.petwise.infrastructure.tutor.models.UpdateTutorRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/** API interface for Tutor operations. Contains all Spring and OpenAPI annotations. */
@RequestMapping("/tutors")
@Tag(name = "Tutors", description = "Tutor management API")
@SuppressWarnings({"PMD.UnnecessaryAnnotationValueElement", "PMD.ShortVariable"})
public interface TutorAPI {

    /**
     * Creates a new tutor.
     *
     * @param request the create-tutor request body
     * @return HTTP 201 with Location header
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new tutor", description = "Creates a new tutor in the system")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Tutor created successfully",
                        headers =
                                @Header(
                                        name = "Location",
                                        description = "URI of the created tutor")),
                @ApiResponse(responseCode = "400", description = "Invalid input"),
                @ApiResponse(responseCode = "422", description = "Validation error")
            })
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Void> createTutor(@RequestBody CreateTutorRequest request);

    /**
     * Gets a tutor by ID.
     *
     * @param tutorId the tutor identifier
     * @return the tutor response
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get tutor by ID", description = "Retrieves a tutor by its ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Tutor found",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = TutorResponse.class))),
                @ApiResponse(responseCode = "404", description = "Tutor not found")
            })
    TutorResponse getTutorById(
            @Parameter(description = "Tutor ID", required = true) @PathVariable("id")
                    String tutorId);

    /**
     * Lists all tutors with pagination.
     *
     * @param page the page number
     * @param perPage the page size
     * @param search optional search terms
     * @param sort the sort field
     * @param direction the sort direction
     * @return paginated tutor responses
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "List all tutors",
            description = "Retrieves a paginated list of tutors with optional" + " search")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Tutors retrieved successfully")
            })
    Pagination<TutorResponse> listTutors(
            @Parameter(description = "Page number (0-based)", example = "0")
                    @RequestParam(name = "page", defaultValue = "0")
                    int page,
            @Parameter(description = "Items per page", example = "10")
                    @RequestParam(name = "perPage", defaultValue = "10")
                    int perPage,
            @Parameter(description = "Search terms", example = "John")
                    @RequestParam(name = "search", defaultValue = "")
                    String search,
            @Parameter(description = "Sort field", example = "name")
                    @RequestParam(name = "sort", defaultValue = "name")
                    String sort,
            @Parameter(description = "Sort direction (asc/desc)", example = "asc")
                    @RequestParam(name = "direction", defaultValue = "asc")
                    String direction);

    /**
     * Updates an existing tutor.
     *
     * @param tutorId the tutor identifier
     * @param request the update request body
     * @return HTTP 200 with Location header
     */
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a tutor", description = "Updates an existing tutor")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Tutor updated successfully",
                        headers =
                                @Header(
                                        name = "Location",
                                        description = "URI of the updated tutor")),
                @ApiResponse(responseCode = "404", description = "Tutor not found"),
                @ApiResponse(responseCode = "400", description = "Invalid input"),
                @ApiResponse(responseCode = "422", description = "Validation error")
            })
    ResponseEntity<Void> updateTutor(
            @Parameter(description = "Tutor ID", required = true) @PathVariable("id")
                    String tutorId,
            @RequestBody UpdateTutorRequest request);

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a tutor", description = "Deletes a tutor by its ID")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Tutor deleted successfully"),
                @ApiResponse(responseCode = "404", description = "Tutor not found")
            })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteTutor(
            @Parameter(description = "Tutor ID", required = true) @PathVariable("id")
                    String tutorId);
}
