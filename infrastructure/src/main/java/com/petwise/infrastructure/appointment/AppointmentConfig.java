package com.petwise.infrastructure.appointment;

import com.petwise.application.appointment.changestatus.ChangeAppointmentStatusUseCase;
import com.petwise.application.appointment.changestatus.DefaultChangeAppointmentStatusUseCase;
import com.petwise.application.appointment.create.CreateAppointmentUseCase;
import com.petwise.application.appointment.create.DefaultCreateAppointmentUseCase;
import com.petwise.application.appointment.delete.DefaultDeleteAppointmentUseCase;
import com.petwise.application.appointment.delete.DeleteAppointmentUseCase;
import com.petwise.application.appointment.retrieve.dailyagenda.DefaultViewDailyAgendaUseCase;
import com.petwise.application.appointment.retrieve.dailyagenda.ViewDailyAgendaUseCase;
import com.petwise.application.appointment.retrieve.getbyid.DefaultGetAppointmentByIdUseCase;
import com.petwise.application.appointment.retrieve.getbyid.GetAppointmentByIdUseCase;
import com.petwise.application.appointment.retrieve.list.DefaultListAppointmentsUseCase;
import com.petwise.application.appointment.retrieve.list.ListAppointmentsUseCase;
import com.petwise.domain.appointment.AppointmentGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for the Appointment module. Wires together use cases, gateways, and
 * controllers.
 */
@Configuration
@SuppressWarnings("PMD.LongVariable")
public class AppointmentConfig {

    /** Default constructor. */
    public AppointmentConfig() {}

    /**
     * Creates the {@link CreateAppointmentUseCase} bean.
     *
     * @param gateway the appointment persistence gateway
     * @return the use case implementation
     */
    @Bean
    public CreateAppointmentUseCase createAppointmentUseCase(final AppointmentGateway gateway) {
        return new DefaultCreateAppointmentUseCase(gateway);
    }

    /**
     * Creates the {@link GetAppointmentByIdUseCase} bean.
     *
     * @param gateway the appointment persistence gateway
     * @return the use case implementation
     */
    @Bean
    public GetAppointmentByIdUseCase getAppointmentByIdUseCase(final AppointmentGateway gateway) {
        return new DefaultGetAppointmentByIdUseCase(gateway);
    }

    /**
     * Creates the {@link ListAppointmentsUseCase} bean.
     *
     * @param gateway the appointment persistence gateway
     * @return the use case implementation
     */
    @Bean
    public ListAppointmentsUseCase listAppointmentsUseCase(final AppointmentGateway gateway) {
        return new DefaultListAppointmentsUseCase(gateway);
    }

    /**
     * Creates the {@link ViewDailyAgendaUseCase} bean.
     *
     * @param gateway the appointment persistence gateway
     * @return the use case implementation
     */
    @Bean
    public ViewDailyAgendaUseCase viewDailyAgendaUseCase(final AppointmentGateway gateway) {
        return new DefaultViewDailyAgendaUseCase(gateway);
    }

    /**
     * Creates the {@link ChangeAppointmentStatusUseCase} bean.
     *
     * @param gateway the appointment persistence gateway
     * @return the use case implementation
     */
    @Bean
    public ChangeAppointmentStatusUseCase changeAppointmentStatusUseCase(
            final AppointmentGateway gateway) {
        return new DefaultChangeAppointmentStatusUseCase(gateway);
    }

    /**
     * Creates the {@link DeleteAppointmentUseCase} bean.
     *
     * @param gateway the appointment persistence gateway
     * @return the use case implementation
     */
    @Bean
    public DeleteAppointmentUseCase deleteAppointmentUseCase(final AppointmentGateway gateway) {
        return new DefaultDeleteAppointmentUseCase(gateway);
    }
}
