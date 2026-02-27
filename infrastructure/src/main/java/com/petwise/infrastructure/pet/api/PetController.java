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

    /** Use case for creating a pet. */
    private final CreatePetUseCase createPetUseCase;

    /** Use case for retrieving a pet by ID. */
    private final GetPetByIdUseCase getPetByIdUseCase;

    /** Use case for listing pets. */
    private final ListPetsUseCase listPetsUseCase;

    /** Use case for updating a pet. */
    private final UpdatePetUseCase updatePetUseCase;

    /** Use case for deleting a pet. */
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
        final var query = new SearchQuery(page, perPage, search, sort, direction);
        return this.listPetsUseCase.execute(query).map(PetApiPresenter::present);
    }

    /** {@inheritDoc} */
    @Override
    public ResponseEntity<Void> updatePet(final String petId, final UpdatePetRequest request) {
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
        this.deletePetUseCase.execute(petId);
    }
}
