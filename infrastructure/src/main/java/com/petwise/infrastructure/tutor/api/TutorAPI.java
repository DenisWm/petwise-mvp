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
import org.springframework.web.bind.annotation.*;

/** API interface for Tutor operations. Contains all Spring and OpenAPI annotations. */
@RequestMapping(value = "/tutors")
@Tag(name = "Tutors", description = "Tutor management API")
public interface TutorAPI {

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
            @Parameter(description = "Tutor ID", required = true) @PathVariable("id") String id);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "List all tutors",
            description = "Retrieves a paginated list of tutors with optional search")
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
            @Parameter(description = "Tutor ID", required = true) @PathVariable("id") String id,
            @RequestBody UpdateTutorRequest request);

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a tutor", description = "Deletes a tutor by its ID")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Tutor deleted successfully"),
                @ApiResponse(responseCode = "404", description = "Tutor not found")
            })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteTutor(
            @Parameter(description = "Tutor ID", required = true) @PathVariable("id") String id);
}
