package com.petwise.infrastructure.pet.api;

import com.petwise.application.pet.create.CreatePetCommand;
import com.petwise.application.pet.create.CreatePetUseCase;
import com.petwise.application.pet.delete.DeletePetUseCase;
import com.petwise.application.pet.retrieve.getbyid.GetPetByIdUseCase;
import com.petwise.application.pet.retrieve.list.ListPetsUseCase;
import com.petwise.application.pet.update.UpdatePetCommand;
import com.petwise.application.pet.update.UpdatePetUseCase;
import com.petwise.domain.pagination.Pagination;
import com.petwise.domain.pagination.SearchQuery;
import com.petwise.infrastructure.pet.models.CreatePetRequest;
import com.petwise.infrastructure.pet.models.PetResponse;
import com.petwise.infrastructure.pet.models.UpdatePetRequest;
import com.petwise.infrastructure.pet.presenters.PetApiPresenter;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that implements the {@link PetAPI} contract.
 *
 * <p>Delegates every HTTP operation to the appropriate use case and converts the result via {@link
 * PetApiPresenter}. All Spring MVC and OpenAPI annotations are declared on the interface.
 */
@RestController
@SuppressWarnings({"PMD.LongVariable", "PMD.ShortVariable"})
public class PetController implements PetAPI {
    private static final Logger LOG = LoggerFactory.getLogger(PetController.class);
    private final CreatePetUseCase createPetUseCase;
    private final GetPetByIdUseCase getPetByIdUseCase;
    private final ListPetsUseCase listPetsUseCase;
    private final UpdatePetUseCase updatePetUseCase;
    private final DeletePetUseCase deletePetUseCase;

    /**
     * Constructs the controller with all required use cases.
     *
     * @param aCreateUseCase use case for creating pets
     * @param aGetByIdUseCase use case for retrieving a pet by ID
     * @param aListUseCase use case for listing pets
     * @param anUpdateUseCase use case for updating a pet
     * @param aDeleteUseCase use case for deleting a pet
     */
    public PetController(
            final CreatePetUseCase aCreateUseCase,
            final GetPetByIdUseCase aGetByIdUseCase,
            final ListPetsUseCase aListUseCase,
            final UpdatePetUseCase anUpdateUseCase,
            final DeletePetUseCase aDeleteUseCase) {
        this.createPetUseCase = aCreateUseCase;
        this.getPetByIdUseCase = aGetByIdUseCase;
        this.listPetsUseCase = aListUseCase;
        this.updatePetUseCase = anUpdateUseCase;
        this.deletePetUseCase = aDeleteUseCase;
    }

    /** {@inheritDoc} */
    @Override
    public ResponseEntity<Void> createPet(final CreatePetRequest request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("POST /pets");
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace("Create pet request body: {}", request);
        }
        final var command =
                CreatePetCommand.with(
                        request.tutorId(),
                        request.name(),
                        request.species(),
                        request.breed(),
                        request.birthDate(),
                        request.notes());
        final var output = this.createPetUseCase.execute(command);
        return ResponseEntity.created(URI.create("/pets/" + output.id())).build();
    }

    /** {@inheritDoc} */
    @Override
    public PetResponse getPetById(final String petId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("GET /pets/{}", petId);
        }
        return PetApiPresenter.present(this.getPetByIdUseCase.execute(petId));
    }

    /** {@inheritDoc} */
    @Override
    public Pagination<PetResponse> listPets(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("GET /pets — page={}, perPage={}, search={}", page, perPage, search);
        }
        final var query = new SearchQuery(page, perPage, search, sort, direction);
        return this.listPetsUseCase.execute(query).map(PetApiPresenter::present);
    }

    /** {@inheritDoc} */
    @Override
    public ResponseEntity<Void> updatePet(final String petId, final UpdatePetRequest request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("PUT /pets/{}", petId);
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace("Update pet request body: {}", request);
        }
        final var command =
                UpdatePetCommand.with(
                        petId,
                        request.name(),
                        request.species(),
                        request.breed(),
                        request.birthDate(),
                        request.notes());
        final var output = this.updatePetUseCase.execute(command);
        return ResponseEntity.ok().location(URI.create("/pets/" + output.id())).build();
    }

    /** {@inheritDoc} */
    @Override
    public void deletePet(final String petId) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("DELETE /pets/{}", petId);
        }
        this.deletePetUseCase.execute(petId);
    }
}
