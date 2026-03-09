package com.petwise.domain.pet;

import com.petwise.domain.Entity;
import com.petwise.domain.tutor.TutorID;
import com.petwise.domain.validation.ValidationHandler;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Objects;

/** Pet entity. Represents an animal under care and linked to a tutor. */
@SuppressWarnings({
    "PMD.UseObjectForClearerAPI",
    "PMD.ShortVariable",
    "PMD.ClassWithOnlyPrivateConstructorsShouldBeFinal",
    "PMD.ShortClassName"
})
public final class Pet extends Entity<PetID> {
    private String name;
    private String species;
    private String breed;
    private LocalDate birthDate;

    /** Additional notes (optional). */
    private String notes;

    private final TutorID tutorId;
    private final Instant createdAt;
    private Instant updatedAt;

    private Pet(
            final PetID anId,
            final TutorID aTutorId,
            final String aName,
            final String aSpecies,
            final String aBreed,
            final LocalDate aBirthDate,
            final String aNotes,
            final Instant aCreatedAt,
            final Instant anUpdatedAt) {
        super(anId, Collections.emptyList());
        this.tutorId = aTutorId;
        this.name = aName;
        this.species = aSpecies;
        this.breed = aBreed;
        this.birthDate = aBirthDate;
        this.notes = aNotes;
        this.createdAt = Objects.requireNonNull(aCreatedAt, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(anUpdatedAt, "'updatedAt' should not be null");
    }

    public static Pet newPet(
            final TutorID aTutorId,
            final String aName,
            final String aSpecies,
            final String aBreed,
            final LocalDate aBirthDate,
            final String aNotes) {
        final var anId = PetID.unique();
        final var now = Instant.now();
        return new Pet(anId, aTutorId, aName, aSpecies, aBreed, aBirthDate, aNotes, now, now);
    }

    /** Reconstructs a Pet from typed objects (persistence). */
    public static Pet with(
            final PetID anId,
            final TutorID aTutorId,
            final String aName,
            final String aSpecies,
            final String aBreed,
            final LocalDate aBirthDate,
            final String aNotes,
            final Instant aCreatedAt,
            final Instant anUpdatedAt) {
        return new Pet(
                anId,
                aTutorId,
                aName,
                aSpecies,
                aBreed,
                aBirthDate,
                aNotes,
                aCreatedAt,
                anUpdatedAt);
    }

    /** Reconstructs a Pet from raw strings (persistence). */
    public static Pet with(
            final String anId,
            final String aTutorId,
            final String aName,
            final String aSpecies,
            final String aBreed,
            final LocalDate aBirthDate,
            final String aNotes,
            final Instant aCreatedAt,
            final Instant anUpdatedAt) {
        return new Pet(
                PetID.from(anId),
                TutorID.from(aTutorId),
                aName,
                aSpecies,
                aBreed,
                aBirthDate,
                aNotes,
                aCreatedAt,
                anUpdatedAt);
    }

    public Pet update(
            final String aName,
            final String aSpecies,
            final String aBreed,
            final LocalDate aBirthDate,
            final String aNotes) {
        this.name = aName;
        this.species = aSpecies;
        this.breed = aBreed;
        this.birthDate = aBirthDate;
        this.notes = aNotes;
        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new PetValidator(this, handler).validate();
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public String getBreed() {
        return breed;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getNotes() {
        return notes;
    }

    public TutorID getTutorId() {
        return tutorId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
