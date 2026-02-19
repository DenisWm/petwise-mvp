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
import com.petwise.infrastructure.tutor.api.TutorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configuration for Tutor module. Wires together use cases, gateways, and controllers. */
@Configuration
public class TutorConfig {

    // Use Cases
    @Bean
    public CreateTutorUseCase createTutorUseCase(final TutorGateway tutorGateway) {
        return new DefaultCreateTutorUseCase(tutorGateway);
    }

    @Bean
    public GetTutorByIdUseCase getTutorByIdUseCase(final TutorGateway tutorGateway) {
        return new DefaultGetTutorByIdUseCase(tutorGateway);
    }

    @Bean
    public ListTutorsUseCase listTutorsUseCase(final TutorGateway tutorGateway) {
        return new DefaultListTutorsUseCase(tutorGateway);
    }

    @Bean
    public UpdateTutorUseCase updateTutorUseCase(final TutorGateway tutorGateway) {
        return new DefaultUpdateTutorUseCase(tutorGateway);
    }

    @Bean
    public DeleteTutorUseCase deleteTutorUseCase(final TutorGateway tutorGateway) {
        return new DefaultDeleteTutorUseCase(tutorGateway);
    }

    // Controller
    @Bean
    public TutorController tutorController(
            final CreateTutorUseCase createTutorUseCase,
            final GetTutorByIdUseCase getTutorByIdUseCase,
            final ListTutorsUseCase listTutorsUseCase,
            final UpdateTutorUseCase updateTutorUseCase,
            final DeleteTutorUseCase deleteTutorUseCase) {
        return new TutorController(
                createTutorUseCase,
                getTutorByIdUseCase,
                listTutorsUseCase,
                updateTutorUseCase,
                deleteTutorUseCase);
    }
}
