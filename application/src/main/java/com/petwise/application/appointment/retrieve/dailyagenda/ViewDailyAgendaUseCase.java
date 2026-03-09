package com.petwise.application.appointment.retrieve.dailyagenda;

import com.petwise.application.UseCase;
import com.petwise.domain.pagination.Pagination;

/**
 * Abstract use case for viewing the daily agenda — appointments for a specific date with optional
 * status and service type filters.
 */
@SuppressWarnings("PMD.AbstractClassWithoutAnyMethod")
public abstract class ViewDailyAgendaUseCase
        extends UseCase<ViewDailyAgendaCommand, Pagination<ViewDailyAgendaOutput>> {
    protected ViewDailyAgendaUseCase() {}
}
