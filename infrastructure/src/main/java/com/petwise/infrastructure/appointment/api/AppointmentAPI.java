package com.petwise.infrastructure.appointment.api;

import com.petwise.domain.appointment.AppointmentStatus;
import com.petwise.domain.appointment.ServiceType;
import com.petwise.domain.pagination.Pagination;
import com.petwise.infrastructure.appointment.models.AppointmentResponse;
import com.petwise.infrastructure.appointment.models.ChangeAppointmentStatusRequest;
import com.petwise.infrastructure.appointment.models.CreateAppointmentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/** API interface for Appointment operations. Contains all Spring and OpenAPI annotations. */
@RequestMapping("/appointments")
@Tag(name = "Appointments", description = "Appointment management API")
@SuppressWarnings({
    "PMD.UnnecessaryAnnotationValueElement",
    "PMD.ShortVariable",
    "PMD.AvoidDuplicateLiterals"
})
public interface AppointmentAPI {
    /**
     * Creates a new appointment.
     *
     * @param request the create-appointment request body
     * @return HTTP 201 with Location header
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Create a new appointment",
            description = "Creates a new appointment for a pet")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Appointment created successfully",
                        headers =
                                @Header(
                                        name = "Location",
                                        description = "URI of the created appointment")),
                @ApiResponse(responseCode = "400", description = "Invalid input"),
                @ApiResponse(responseCode = "422", description = "Validation error")
            })
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Void> createAppointment(@RequestBody CreateAppointmentRequest request);

    /**
     * Gets an appointment by ID.
     *
     * @param appointmentId the appointment identifier
     * @return the appointment response
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Get appointment by ID",
            description = "Retrieves an appointment by its ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Appointment found",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema =
                                                @Schema(
                                                        implementation =
                                                                AppointmentResponse.class))),
                @ApiResponse(responseCode = "404", description = "Appointment not found")
            })
    AppointmentResponse getAppointmentById(
            @Parameter(description = "Appointment ID", required = true) @PathVariable("id")
                    String appointmentId);

    /**
     * Lists all appointments with pagination.
     *
     * @param page the page number
     * @param perPage the page size
     * @param search optional search terms
     * @param sort the sort field
     * @param direction the sort direction
     * @return paginated appointment responses
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "List all appointments",
            description = "Retrieves a paginated list of appointments with optional search terms")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Appointments retrieved successfully")
            })
    Pagination<AppointmentResponse> listAppointments(
            @Parameter(description = "Page number (0-based)", example = "0")
                    @RequestParam(name = "page", defaultValue = "0")
                    int page,
            @Parameter(description = "Items per page", example = "10")
                    @RequestParam(name = "perPage", defaultValue = "10")
                    int perPage,
            @Parameter(description = "Search terms", example = "CRECHE")
                    @RequestParam(name = "search", defaultValue = "")
                    String search,
            @Parameter(description = "Sort field", example = "startAt")
                    @RequestParam(name = "sort", defaultValue = "startAt")
                    String sort,
            @Parameter(description = "Sort direction (asc/desc)", example = "asc")
                    @RequestParam(name = "direction", defaultValue = "asc")
                    String direction);

    /**
     * Views the daily agenda — appointments for a specific date with optional filters.
     *
     * @param date the date to view (required, ISO format YYYY-MM-DD)
     * @param status optional status filter
     * @param serviceType optional service type filter
     * @param page the page number
     * @param perPage the page size
     * @param sort the sort field
     * @param direction the sort direction
     * @return paginated appointment responses
     */
    @GetMapping(value = "/agenda", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "View daily agenda",
            description =
                    "Retrieves appointments for a specific date with optional status and service"
                            + " type filters")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Agenda retrieved successfully"),
                @ApiResponse(responseCode = "400", description = "Invalid date format")
            })
    Pagination<AppointmentResponse> viewDailyAgenda(
            @Parameter(
                            description = "Date to view (ISO format)",
                            required = true,
                            example = "2026-02-26")
                    @RequestParam(name = "date")
                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    LocalDate date,
            @Parameter(description = "Filter by status", example = "ACTIVE")
                    @RequestParam(name = "status", required = false)
                    AppointmentStatus status,
            @Parameter(description = "Filter by service type", example = "CRECHE")
                    @RequestParam(name = "serviceType", required = false)
                    ServiceType serviceType,
            @Parameter(description = "Page number (0-based)", example = "0")
                    @RequestParam(name = "page", defaultValue = "0")
                    int page,
            @Parameter(description = "Items per page", example = "20")
                    @RequestParam(name = "perPage", defaultValue = "20")
                    int perPage,
            @Parameter(description = "Sort field", example = "startAt")
                    @RequestParam(name = "sort", defaultValue = "startAt")
                    String sort,
            @Parameter(description = "Sort direction (asc/desc)", example = "asc")
                    @RequestParam(name = "direction", defaultValue = "asc")
                    String direction);

    /**
     * Changes the status of an appointment.
     *
     * @param appointmentId the appointment identifier
     * @param request the status change request body
     * @return HTTP 200 with Location header
     */
    @PatchMapping(
            value = "/{id}/status",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Change appointment status",
            description = "Changes the lifecycle status of an appointment")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Status changed successfully"),
                @ApiResponse(responseCode = "404", description = "Appointment not found"),
                @ApiResponse(responseCode = "422", description = "Invalid status transition")
            })
    ResponseEntity<Void> changeAppointmentStatus(
            @Parameter(description = "Appointment ID", required = true) @PathVariable("id")
                    String appointmentId,
            @RequestBody ChangeAppointmentStatusRequest request);

    /**
     * Deletes an appointment by ID.
     *
     * @param appointmentId the appointment identifier
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an appointment", description = "Deletes an appointment by its ID")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "204",
                        description = "Appointment deleted successfully"),
                @ApiResponse(responseCode = "404", description = "Appointment not found")
            })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteAppointment(
            @Parameter(description = "Appointment ID", required = true) @PathVariable("id")
                    String appointmentId);
}
