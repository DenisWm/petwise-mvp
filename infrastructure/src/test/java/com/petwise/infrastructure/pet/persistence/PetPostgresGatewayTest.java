package com.petwise.infrastructure.pet.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.PostgresGatewayTest;
import com.petwise.domain.pagination.SearchQuery;
import com.petwise.domain.pet.Pet;
import com.petwise.domain.pet.PetID;
import com.petwise.domain.tutor.Tutor;
import com.petwise.domain.tutor.TutorID;
import com.petwise.infrastructure.tutor.persistence.TutorJpaEntity;
import com.petwise.infrastructure.tutor.persistence.TutorRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/** Integration tests for {@link PetPostgresGateway}. */
@PostgresGatewayTest
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.AvoidDuplicateLiterals",
    "PMD.TooManyMethods"
})
class PetPostgresGatewayTest {
    PetPostgresGatewayTest() {}

    @Autowired private PetRepository petRepository;
    @Autowired private TutorRepository tutorRepository;
    @Autowired private PetPostgresGateway petGateway;

    private TutorID persistedTutorId() {
        final var tutor = Tutor.newTutor("Test Tutor", null, null);
        tutorRepository.save(TutorJpaEntity.from(tutor));
        return tutor.getId();
    }

    @Test
    void givenValidPet_whenSave_thenShouldPersistAndReturn() {
        final var tutorId = persistedTutorId();
        final var pet =
                Pet.newPet(
                        tutorId,
                        "Fluffy",
                        "Cat",
                        "Persian",
                        LocalDate.of(2020, 3, 15),
                        "Some notes");
        final var result = petGateway.save(pet);
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("Fluffy");
        assertThat(result.getSpecies()).isEqualTo("Cat");
        assertThat(result.getBreed()).isEqualTo("Persian");
        assertThat(result.getBirthDate()).isEqualTo(LocalDate.of(2020, 3, 15));
        assertThat(result.getTutorId()).isEqualTo(tutorId);
    }

    @Test
    void givenExistingPet_whenFindById_thenShouldReturnPet() {
        final var pet = Pet.newPet(persistedTutorId(), "Buddy", "Dog", null, null, null);
        final var saved = petGateway.save(pet);
        final var result = petGateway.findById(saved.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(saved.getId());
        assertThat(result.get().getName()).isEqualTo("Buddy");
    }

    @Test
    void givenNonExistingPet_whenFindById_thenShouldReturnEmpty() {
        final var result = petGateway.findById(PetID.from("non-existing-id"));
        assertThat(result).isEmpty();
    }

    @Test
    void givenMultiplePets_whenFindAll_thenShouldReturnAllPets() {
        petGateway.save(Pet.newPet(persistedTutorId(), "Alpha", "Dog", null, null, null));
        petGateway.save(Pet.newPet(persistedTutorId(), "Beta", "Cat", null, null, null));
        petGateway.save(Pet.newPet(persistedTutorId(), "Gamma", "Rabbit", null, null, null));
        final var result = petGateway.findAll();
        assertThat(result).hasSize(3);
        assertThat(result)
                .extracting(Pet::getName)
                .containsExactlyInAnyOrder("Alpha", "Beta", "Gamma");
    }

    @Test
    void givenExistingPet_whenDelete_thenShouldRemovePet() {
        final var pet = Pet.newPet(persistedTutorId(), "ToDelete", null, null, null, null);
        final var saved = petGateway.save(pet);
        petGateway.deleteById(saved.getId());
        assertThat(petGateway.findById(saved.getId())).isEmpty();
        assertThat(petRepository.count()).isZero();
    }

    @Test
    void givenPetsWithMatchingName_whenSearchWithTerms_thenShouldReturnMatchingPets() {
        petGateway.save(Pet.newPet(persistedTutorId(), "Fluffy", "Cat", null, null, null));
        petGateway.save(Pet.newPet(persistedTutorId(), "Fluffball", "Cat", null, null, null));
        petGateway.save(Pet.newPet(persistedTutorId(), "Rocky", "Dog", null, null, null));

        final var query = new SearchQuery(0, 10, "Fluff", "name", "asc");
        final var result = petGateway.findAll(query);
        assertThat(result.total()).isEqualTo(2);
        assertThat(result.items())
                .extracting(Pet::getName)
                .containsExactlyInAnyOrder("Fluffy", "Fluffball");
    }

    @Test
    void givenPets_whenSearchWithNoTerms_thenShouldReturnAllPaginated() {
        petGateway.save(Pet.newPet(persistedTutorId(), "A", null, null, null, null));
        petGateway.save(Pet.newPet(persistedTutorId(), "B", null, null, null, null));

        final var query = new SearchQuery(0, 10, "", "name", "asc");
        final var result = petGateway.findAll(query);
        assertThat(result.total()).isEqualTo(2);
    }

    @Test
    void givenPets_whenSearchWithDescSort_thenShouldReturnAllPaginated() {
        petGateway.save(Pet.newPet(persistedTutorId(), "Alpha", null, null, null, null));
        petGateway.save(Pet.newPet(persistedTutorId(), "Beta", null, null, null, null));

        final var query = new SearchQuery(0, 10, "", "name", "desc");
        final var result = petGateway.findAll(query);
        assertThat(result.total()).isEqualTo(2);
    }

    @Test
    void givenExistingPet_whenUpdate_thenShouldPersistChanges() {
        final var pet = Pet.newPet(persistedTutorId(), "OldName", null, null, null, null);
        final var saved = petGateway.save(pet);

        saved.update("NewName", "Dog", "Labrador", null, "Updated notes");
        final var updated = petGateway.save(saved);
        assertThat(updated.getName()).isEqualTo("NewName");
        assertThat(updated.getSpecies()).isEqualTo("Dog");
        assertThat(updated.getBreed()).isEqualTo("Labrador");
    }
}
