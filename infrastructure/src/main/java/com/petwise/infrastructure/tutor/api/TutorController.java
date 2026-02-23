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
 * REST controller that implements the {@link TutorAPI} contract.
 *
 * <p>Delegates every HTTP operation to the appropriate use case and converts the result via {@link
 * TutorApiPresenter}. All Spring MVC and OpenAPI annotations are declared on the interface to keep
 * this class focused on orchestration.
 */
@RestController
@SuppressWarnings({"PMD.LongVariable", "PMD.ShortVariable"})
public class TutorController implements TutorAPI {

    /** Use case for creating a tutor. */
    private final CreateTutorUseCase createTutorUseCase;

    /** Use case for retrieving a tutor by ID. */
    private final GetTutorByIdUseCase getTutorByIdUseCase;

    /** Use case for listing tutors. */
    private final ListTutorsUseCase listTutorsUseCase;

    /** Use case for updating a tutor. */
    private final UpdateTutorUseCase updateTutorUseCase;

    /** Use case for deleting a tutor. */
    private final DeleteTutorUseCase deleteTutorUseCase;

    /**
     * Constructs the controller with all required use cases.
     *
     * @param aCreateUseCase use case for creating tutors
     * @param aGetByIdUseCase use case for retrieving a tutor by ID
     * @param aListUseCase use case for listing tutors
     * @param anUpdateUseCase use case for updating a tutor
     * @param aDeleteUseCase use case for deleting a tutor
     */
    public TutorController(
            final CreateTutorUseCase aCreateUseCase,
            final GetTutorByIdUseCase aGetByIdUseCase,
            final ListTutorsUseCase aListUseCase,
            final UpdateTutorUseCase anUpdateUseCase,
            final DeleteTutorUseCase aDeleteUseCase) {
        this.createTutorUseCase = aCreateUseCase;
        this.getTutorByIdUseCase = aGetByIdUseCase;
        this.listTutorsUseCase = aListUseCase;
        this.updateTutorUseCase = anUpdateUseCase;
        this.deleteTutorUseCase = aDeleteUseCase;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Subclasses may override to customise creation behaviour.
     */
    @Override
    public ResponseEntity<Void> createTutor(final CreateTutorRequest request) {
        final var command =
                CreateTutorCommand.with(request.name(), request.email(), request.phone());
        final var output = this.createTutorUseCase.execute(command);
        return ResponseEntity.created(URI.create("/tutors/" + output.id())).build();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Subclasses may override to customise retrieval behaviour.
     */
    @Override
    public TutorResponse getTutorById(final String tutorId) {
        final var output = this.getTutorByIdUseCase.execute(tutorId);
        return TutorApiPresenter.present(output);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Subclasses may override to customise listing behaviour.
     */
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

    /**
     * {@inheritDoc}
     *
     * <p>Subclasses may override to customise update behaviour.
     */
    @Override
    public ResponseEntity<Void> updateTutor(
            final String tutorId, final UpdateTutorRequest request) {
        final var command =
                UpdateTutorCommand.with(tutorId, request.name(), request.email(), request.phone());
        final var output = this.updateTutorUseCase.execute(command);
        return ResponseEntity.ok().location(URI.create("/tutors/" + output.id())).build();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Subclasses may override to customise deletion behaviour.
     */
    @Override
    public void deleteTutor(final String tutorId) {
        this.deleteTutorUseCase.execute(tutorId);
    }
}
