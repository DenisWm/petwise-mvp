package com.petwise.domain.appointment;

import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import java.util.List;
import java.util.Optional;

/**
 * Gateway interface for Appointment persistence operations. To be implemented by the infrastructure
 * layer.
 */
@SuppressWarnings("PMD.ShortVariable")
public interface AppointmentGateway {

    /**
     * Saves an appointment (create or update).
     *
     * @param appointment the appointment to save
     * @return the saved appointment
     */
    Appointment save(Appointment appointment);

    /**
     * Finds an appointment by ID.
     *
     * @param anId the appointment ID
     * @return an Optional containing the appointment if found, empty otherwise
     */
    Optional<Appointment> findById(AppointmentID anId);

    /**
     * Finds all appointments.
     *
     * @return a list of all appointments
     */
    List<Appointment> findAll();

    /**
     * Finds all appointments with pagination and search.
     *
     * @param query the search query with pagination parameters
     * @return a paginated result of appointments
     */
    Pagination<Appointment> findAll(SearchQuery query);

    /**
     * Finds appointments for a specific date with optional status and service type filters.
     *
     * @param query the agenda search query with date, optional filters, and pagination
     * @return a paginated result of matching appointments
     */
    Pagination<Appointment> findDailyAgenda(AppointmentSearchQuery query);

    /**
     * Deletes an appointment by ID.
     *
     * @param anId the appointment ID to delete
     */
    void deleteById(AppointmentID anId);
}
