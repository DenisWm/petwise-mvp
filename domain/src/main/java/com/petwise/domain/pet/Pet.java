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

    /** The pet's name. */
    private String name;

    /** The pet's species (optional). */
    private String species;

    /** The pet's breed (optional). */
    private String breed;

    /** The pet's birth date (optional). */
    private LocalDate birthDate;

    /** Additional notes (optional). */
    private String notes;

    /** The owning tutor ID. */
    private final TutorID tutorId;

    /** Timestamp of when this pet was created. */
    private final Instant createdAt;

    /** Timestamp of the last update to this pet. */
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

    /**
     * Creates a new Pet with a generated ID and the current time.
     *
     * @param aTutorId the tutor ID (required)
     * @param aName the pet name (required)
     * @param aSpecies the pet species (optional)
     * @param aBreed the pet breed (optional)
     * @param aBirthDate the pet birth date (optional)
     * @param aNotes notes about the pet (optional)
     * @return a new Pet instance
     */
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

    /**
     * Creates a Pet from typed objects (for persistence reconstruction).
     *
     * @param anId the pet ID
     * @param aTutorId the tutor ID
     * @param aName the pet name
     * @param aSpecies the pet species
     * @param aBreed the pet breed
     * @param aBirthDate the pet birth date
     * @param aNotes notes about the pet
     * @param aCreatedAt the creation timestamp
     * @param anUpdatedAt the last update timestamp
     * @return a Pet instance
     */
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

    /**
     * Creates a Pet from raw strings (for persistence reconstruction).
     *
     * @param anId the pet ID
     * @param aTutorId the tutor ID
     * @param aName the pet name
     * @param aSpecies the pet species
     * @param aBreed the pet breed
     * @param aBirthDate the pet birth date
     * @param aNotes notes about the pet
     * @param aCreatedAt the creation timestamp
     * @param anUpdatedAt the last update timestamp
     * @return a Pet instance
     */
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

    /**
     * Updates pet information.
     *
     * @param aName the new name
     * @param aSpecies the new species
     * @param aBreed the new breed
     * @param aBirthDate the new birth date
     * @param aNotes new notes
     * @return the updated Pet instance
     */
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
