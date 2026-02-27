package com.petwise.infrastructure.pet.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.petwise.domain.pet.Pet;
import com.petwise.domain.tutor.TutorID;
import java.time.Instant;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/** Unit tests for {@link PetJpaEntity} getters, setters, and conversions. */
@SuppressWarnings({
    "PMD.MethodNamingConventions",
    "PMD.UnitTestContainsTooManyAsserts",
    "PMD.UnitTestAssertionsShouldIncludeMessage",
    "PMD.JUnit5TestShouldBePackagePrivate",
    "PMD.TooManyMethods",
    "PMD.AvoidDuplicateLiterals"
})
class PetJpaEntityTest {

    /** Default constructor. */
    PetJpaEntityTest() {}

    @Test
    void givenPet_whenFrom_thenShouldMapAllFields() {
        final var tutorId = TutorID.unique();
        final var pet =
                Pet.newPet(tutorId, "Fluffy", "Cat", "Persian", LocalDate.of(2020, 3, 15), "Notes");

        final var entity = PetJpaEntity.from(pet);

        assertThat(entity.getId()).isEqualTo(pet.getId().getValue());
        assertThat(entity.getTutorId()).isEqualTo(tutorId.getValue());
        assertThat(entity.getName()).isEqualTo("Fluffy");
        assertThat(entity.getSpecies()).isEqualTo("Cat");
        assertThat(entity.getBreed()).isEqualTo("Persian");
        assertThat(entity.getBirthDate()).isEqualTo(LocalDate.of(2020, 3, 15));
        assertThat(entity.getNotes()).isEqualTo("Notes");
        assertThat(entity.getCreatedAt()).isNotNull();
        assertThat(entity.getUpdatedAt()).isNotNull();
    }

    @Test
    void givenEntity_whenToAggregate_thenShouldReconstructPet() {
        final var tutorId = TutorID.unique();
        final var pet = Pet.newPet(tutorId, "Buddy", "Dog", null, null, null);

        final var aggregate = PetJpaEntity.from(pet).toAggregate();

        assertThat(aggregate.getId().getValue()).isEqualTo(pet.getId().getValue());
        assertThat(aggregate.getTutorId().getValue()).isEqualTo(tutorId.getValue());
        assertThat(aggregate.getName()).isEqualTo("Buddy");
        assertThat(aggregate.getSpecies()).isEqualTo("Dog");
        assertThat(aggregate.getBreed()).isNull();
    }

    @Test
    void givenDefaultConstructor_whenSettersUsed_thenGettersShouldReturnValues() {
        final var now = Instant.now();
        final var birthDate = LocalDate.of(2019, 6, 1);
        final var entity = new PetJpaEntity();
        entity.setId("pet-1");
        entity.setTutorId("tutor-1");
        entity.setName("Rocky");
        entity.setSpecies("Dog");
        entity.setBreed("Labrador");
        entity.setBirthDate(birthDate);
        entity.setNotes("Playful");
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        assertThat(entity.getId()).isEqualTo("pet-1");
        assertThat(entity.getTutorId()).isEqualTo("tutor-1");
        assertThat(entity.getName()).isEqualTo("Rocky");
        assertThat(entity.getSpecies()).isEqualTo("Dog");
        assertThat(entity.getBreed()).isEqualTo("Labrador");
        assertThat(entity.getBirthDate()).isEqualTo(birthDate);
        assertThat(entity.getNotes()).isEqualTo("Playful");
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
    }
}
