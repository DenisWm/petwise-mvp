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

    /** Default constructor. */
    PetPostgresGatewayTest() {}

    /** The pet repository for direct DB access. */
    @Autowired private PetRepository petRepository;

    /** The tutor repository for setting up FK parent records. */
    @Autowired private TutorRepository tutorRepository;

    /** The gateway under test. */
    @Autowired private PetPostgresGateway petGateway;

    private TutorID persistedTutorId() {
        final var tutor = Tutor.newTutor("Test Tutor", null, null);
        tutorRepository.save(TutorJpaEntity.from(tutor));
        return tutor.getId();
    }

    @Test
    void givenValidPet_whenSave_thenShouldPersistAndReturn() {
        // given
        final var tutorId = persistedTutorId();
        final var pet =
                Pet.newPet(
                        tutorId,
                        "Fluffy",
                        "Cat",
                        "Persian",
                        LocalDate.of(2020, 3, 15),
                        "Some notes");

        // when
        final var result = petGateway.save(pet);

        // then
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
        // given
        final var pet = Pet.newPet(persistedTutorId(), "Buddy", "Dog", null, null, null);
        final var saved = petGateway.save(pet);

        // when
        final var result = petGateway.findById(saved.getId());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(saved.getId());
        assertThat(result.get().getName()).isEqualTo("Buddy");
    }

    @Test
    void givenNonExistingPet_whenFindById_thenShouldReturnEmpty() {
        // when
        final var result = petGateway.findById(PetID.from("non-existing-id"));

        // then
        assertThat(result).isEmpty();
    }

    @Test
    void givenMultiplePets_whenFindAll_thenShouldReturnAllPets() {
        // given
        petGateway.save(Pet.newPet(persistedTutorId(), "Alpha", "Dog", null, null, null));
        petGateway.save(Pet.newPet(persistedTutorId(), "Beta", "Cat", null, null, null));
        petGateway.save(Pet.newPet(persistedTutorId(), "Gamma", "Rabbit", null, null, null));

        // when
        final var result = petGateway.findAll();

        // then
        assertThat(result).hasSize(3);
        assertThat(result)
                .extracting(Pet::getName)
                .containsExactlyInAnyOrder("Alpha", "Beta", "Gamma");
    }

    @Test
    void givenExistingPet_whenDelete_thenShouldRemovePet() {
        // given
        final var pet = Pet.newPet(persistedTutorId(), "ToDelete", null, null, null, null);
        final var saved = petGateway.save(pet);

        // when
        petGateway.deleteById(saved.getId());

        // then
        assertThat(petGateway.findById(saved.getId())).isEmpty();
        assertThat(petRepository.count()).isZero();
    }

    @Test
    void givenPetsWithMatchingName_whenSearchWithTerms_thenShouldReturnMatchingPets() {
        // given
        petGateway.save(Pet.newPet(persistedTutorId(), "Fluffy", "Cat", null, null, null));
        petGateway.save(Pet.newPet(persistedTutorId(), "Fluffball", "Cat", null, null, null));
        petGateway.save(Pet.newPet(persistedTutorId(), "Rocky", "Dog", null, null, null));

        final var query = new SearchQuery(0, 10, "Fluff", "name", "asc");

        // when
        final var result = petGateway.findAll(query);

        // then
        assertThat(result.total()).isEqualTo(2);
        assertThat(result.items())
                .extracting(Pet::getName)
                .containsExactlyInAnyOrder("Fluffy", "Fluffball");
    }

    @Test
    void givenPets_whenSearchWithNoTerms_thenShouldReturnAllPaginated() {
        // given
        petGateway.save(Pet.newPet(persistedTutorId(), "A", null, null, null, null));
        petGateway.save(Pet.newPet(persistedTutorId(), "B", null, null, null, null));

        final var query = new SearchQuery(0, 10, "", "name", "asc");

        // when
        final var result = petGateway.findAll(query);

        // then
        assertThat(result.total()).isEqualTo(2);
    }

    @Test
    void givenPets_whenSearchWithDescSort_thenShouldReturnAllPaginated() {
        // given
        petGateway.save(Pet.newPet(persistedTutorId(), "Alpha", null, null, null, null));
        petGateway.save(Pet.newPet(persistedTutorId(), "Beta", null, null, null, null));

        final var query = new SearchQuery(0, 10, "", "name", "desc");

        // when
        final var result = petGateway.findAll(query);

        // then
        assertThat(result.total()).isEqualTo(2);
    }

    @Test
    void givenExistingPet_whenUpdate_thenShouldPersistChanges() {
        // given
        final var pet = Pet.newPet(persistedTutorId(), "OldName", null, null, null, null);
        final var saved = petGateway.save(pet);

        saved.update("NewName", "Dog", "Labrador", null, "Updated notes");

        // when
        final var updated = petGateway.save(saved);

        // then
        assertThat(updated.getName()).isEqualTo("NewName");
        assertThat(updated.getSpecies()).isEqualTo("Dog");
        assertThat(updated.getBreed()).isEqualTo("Labrador");
    }
}
