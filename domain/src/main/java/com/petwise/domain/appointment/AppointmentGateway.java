package com.petwise.domain.appointment;

import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import java.util.List;
import java.util.Optional;

/** Gateway for Appointment persistence operations. Implemented by the infrastructure layer. */
@SuppressWarnings("PMD.ShortVariable")
public interface AppointmentGateway {
    Appointment save(Appointment appointment);

    Optional<Appointment> findById(AppointmentID anId);

    List<Appointment> findAll();

    Pagination<Appointment> findAll(SearchQuery query);

    Pagination<Appointment> findDailyAgenda(AppointmentSearchQuery query);

    void deleteById(AppointmentID anId);
}
