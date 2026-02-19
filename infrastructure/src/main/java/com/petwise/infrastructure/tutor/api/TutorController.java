package com.petwise.infrastructure.tutor.api;

import com.petwise.application.tutor.create.CreateTutorCommand;
import com.petwise.application.tutor.create.CreateTutorUseCase;
import com.petwise.application.tutor.delete.DeleteTutorUseCase;
import com.petwise.application.tutor.retrieve.getbyid.GetTutorByIdUseCase;
import com.petwise.application.tutor.retrieve.list.ListTutorsUseCase;
import com.petwise.application.tutor.update.UpdateTutorCommand;
import com.petwise.application.tutor.update.UpdateTutorUseCase;
import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import com.petwise.infrastructure.tutor.models.CreateTutorRequest;
import com.petwise.infrastructure.tutor.models.TutorResponse;
import com.petwise.infrastructure.tutor.models.UpdateTutorRequest;
import com.petwise.infrastructure.tutor.presenters.TutorApiPresenter;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementation of TutorAPI. Only @RestController annotation - all other annotations are in the
 * interface.
 */
@RestController
public class TutorController implements TutorAPI {

    private final CreateTutorUseCase createTutorUseCase;
    private final GetTutorByIdUseCase getTutorByIdUseCase;
    private final ListTutorsUseCase listTutorsUseCase;
    private final UpdateTutorUseCase updateTutorUseCase;
    private final DeleteTutorUseCase deleteTutorUseCase;

    public TutorController(
            final CreateTutorUseCase createTutorUseCase,
            final GetTutorByIdUseCase getTutorByIdUseCase,
            final ListTutorsUseCase listTutorsUseCase,
            final UpdateTutorUseCase updateTutorUseCase,
            final DeleteTutorUseCase deleteTutorUseCase) {
        this.createTutorUseCase = createTutorUseCase;
        this.getTutorByIdUseCase = getTutorByIdUseCase;
        this.listTutorsUseCase = listTutorsUseCase;
        this.updateTutorUseCase = updateTutorUseCase;
        this.deleteTutorUseCase = deleteTutorUseCase;
    }

    @Override
    public ResponseEntity<Void> createTutor(final CreateTutorRequest request) {
        final var command =
                CreateTutorCommand.with(request.name(), request.email(), request.phone());

        final var output = this.createTutorUseCase.execute(command);

        return ResponseEntity.created(URI.create("/tutors/" + output.id())).build();
    }

    @Override
    public TutorResponse getTutorById(final String id) {
        final var output = this.getTutorByIdUseCase.execute(id);
        return TutorApiPresenter.present(output);
    }

    @Override
    public Pagination<TutorResponse> listTutors(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction) {
        final var query = new SearchQuery(page, perPage, search, sort, direction);
        final var output = this.listTutorsUseCase.execute(query);
        return output.map(TutorApiPresenter::present);
    }

    @Override
    public ResponseEntity<Void> updateTutor(final String id, final UpdateTutorRequest request) {
        final var command =
                UpdateTutorCommand.with(id, request.name(), request.email(), request.phone());

        final var output = this.updateTutorUseCase.execute(command);

        return ResponseEntity.ok().location(URI.create("/tutors/" + output.id())).build();
    }

    @Override
    public void deleteTutor(final String id) {
        this.deleteTutorUseCase.execute(id);
    }
}
