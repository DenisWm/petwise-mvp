package com.petwise.infrastructure.pet.api;

import com.petwise.domain.pagination.Pagination;
import com.petwise.infrastructure.pet.models.CreatePetRequest;
import com.petwise.infrastructure.pet.models.PetResponse;
import com.petwise.infrastructure.pet.models.UpdatePetRequest;
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

/** API interface for Pet operations. Contains all Spring and OpenAPI annotations. */
@RequestMapping("/pets")
@Tag(name = "Pets", description = "Pet management API")
@SuppressWarnings({"PMD.UnnecessaryAnnotationValueElement", "PMD.ShortVariable"})
public interface PetAPI {

    /**
     * Creates a new pet.
     *
     * @param request the create-pet request body
     * @return HTTP 201 with Location header
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new pet", description = "Creates a new pet in the system")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Pet created successfully",
                        headers =
                                @Header(name = "Location", description = "URI of the created pet")),
                @ApiResponse(responseCode = "400", description = "Invalid input"),
                @ApiResponse(responseCode = "422", description = "Validation error")
            })
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Void> createPet(@RequestBody CreatePetRequest request);

    /**
     * Gets a pet by ID.
     *
     * @param petId the pet identifier
     * @return the pet response
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get pet by ID", description = "Retrieves a pet by its ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Pet found",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = PetResponse.class))),
                @ApiResponse(responseCode = "404", description = "Pet not found")
            })
    PetResponse getPetById(
            @Parameter(description = "Pet ID", required = true) @PathVariable("id") String petId);

    /**
     * Lists all pets with pagination.
     *
     * @param page the page number
     * @param perPage the page size
     * @param search optional search terms
     * @param sort the sort field
     * @param direction the sort direction
     * @return paginated pet responses
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "List all pets",
            description = "Retrieves a paginated list of pets with optional search")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Pets retrieved successfully")
            })
    Pagination<PetResponse> listPets(
            @Parameter(description = "Page number (0-based)", example = "0")
                    @RequestParam(name = "page", defaultValue = "0")
                    int page,
            @Parameter(description = "Items per page", example = "10")
                    @RequestParam(name = "perPage", defaultValue = "10")
                    int perPage,
            @Parameter(description = "Search terms", example = "Fluffy")
                    @RequestParam(name = "search", defaultValue = "")
                    String search,
            @Parameter(description = "Sort field", example = "name")
                    @RequestParam(name = "sort", defaultValue = "name")
                    String sort,
            @Parameter(description = "Sort direction (asc/desc)", example = "asc")
                    @RequestParam(name = "direction", defaultValue = "asc")
                    String direction);

    /**
     * Updates an existing pet.
     *
     * @param petId the pet identifier
     * @param request the update request body
     * @return HTTP 200 with Location header
     */
    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a pet", description = "Updates an existing pet")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Pet updated successfully",
                        headers =
                                @Header(name = "Location", description = "URI of the updated pet")),
                @ApiResponse(responseCode = "404", description = "Pet not found"),
                @ApiResponse(responseCode = "400", description = "Invalid input"),
                @ApiResponse(responseCode = "422", description = "Validation error")
            })
    ResponseEntity<Void> updatePet(
            @Parameter(description = "Pet ID", required = true) @PathVariable("id") String petId,
            @RequestBody UpdatePetRequest request);

    /**
     * Deletes a pet by ID.
     *
     * @param petId the pet identifier
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a pet", description = "Deletes a pet by its ID")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "204", description = "Pet deleted successfully"),
                @ApiResponse(responseCode = "404", description = "Pet not found")
            })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deletePet(
            @Parameter(description = "Pet ID", required = true) @PathVariable("id") String petId);
}
