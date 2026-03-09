package com.petwise.infrastructure.tutor;

import com.petwise.application.tutor.create.CreateTutorUseCase;
import com.petwise.application.tutor.create.DefaultCreateTutorUseCase;
import com.petwise.application.tutor.delete.DefaultDeleteTutorUseCase;
import com.petwise.application.tutor.delete.DeleteTutorUseCase;
import com.petwise.application.tutor.retrieve.getbyid.DefaultGetTutorByIdUseCase;
import com.petwise.application.tutor.retrieve.getbyid.GetTutorByIdUseCase;
import com.petwise.application.tutor.retrieve.list.DefaultListTutorsUseCase;
import com.petwise.application.tutor.retrieve.list.ListTutorsUseCase;
import com.petwise.application.tutor.update.DefaultUpdateTutorUseCase;
import com.petwise.application.tutor.update.UpdateTutorUseCase;
import com.petwise.domain.tutor.TutorGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration for the Tutor module. Wires together use cases, gateways, and controllers.
 */
@Configuration
@SuppressWarnings("PMD.LongVariable")
public class TutorConfig {
    public TutorConfig() {}

    /**
     * Creates the {@link CreateTutorUseCase} bean.
     *
     * @param gateway the tutor persistence gateway
     * @return the use case implementation
     */
    @Bean
    public CreateTutorUseCase createTutorUseCase(final TutorGateway gateway) {
        return new DefaultCreateTutorUseCase(gateway);
    }

    /**
     * Creates the {@link GetTutorByIdUseCase} bean.
     *
     * @param gateway the tutor persistence gateway
     * @return the use case implementation
     */
    @Bean
    public GetTutorByIdUseCase getTutorByIdUseCase(final TutorGateway gateway) {
        return new DefaultGetTutorByIdUseCase(gateway);
    }

    /**
     * Creates the {@link ListTutorsUseCase} bean.
     *
     * @param gateway the tutor persistence gateway
     * @return the use case implementation
     */
    @Bean
    public ListTutorsUseCase listTutorsUseCase(final TutorGateway gateway) {
        return new DefaultListTutorsUseCase(gateway);
    }

    /**
     * Creates the {@link UpdateTutorUseCase} bean.
     *
     * @param gateway the tutor persistence gateway
     * @return the use case implementation
     */
    @Bean
    public UpdateTutorUseCase updateTutorUseCase(final TutorGateway gateway) {
        return new DefaultUpdateTutorUseCase(gateway);
    }

    /**
     * Creates the {@link DeleteTutorUseCase} bean.
     *
     * @param gateway the tutor persistence gateway
     * @return the use case implementation
     */
    @Bean
    public DeleteTutorUseCase deleteTutorUseCase(final TutorGateway gateway) {
        return new DefaultDeleteTutorUseCase(gateway);
    }
}
