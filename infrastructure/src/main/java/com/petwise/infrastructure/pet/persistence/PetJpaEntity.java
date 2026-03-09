package com.petwise.infrastructure.pet.persistence;

import com.petwise.domain.pet.Pet;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;

/**
 * JPA entity that maps the {@code pets} database table to the {@link Pet} domain entity.
 *
 * <p>Use {@link #from(Pet)} to convert a domain object into a persistable entity and {@link
 * #toAggregate()} to reconstruct the domain object after a query.
 */
@Entity
@Table(name = "pets")
@SuppressWarnings({"PMD.ShortVariable", "PMD.DataClass"})
public class PetJpaEntity {
    private static final int ID_LENGTH = 36;
    private static final int TEXT_LENGTH = 255;

    @Id
    @Column(name = "id", nullable = false, length = ID_LENGTH)
    private String id;

    @Column(name = "tutor_id", nullable = false, length = ID_LENGTH)
    private String tutorId;

    @Column(name = "name", nullable = false, length = TEXT_LENGTH)
    private String name;

    @Column(name = "species", length = TEXT_LENGTH)
    private String species;

    @Column(name = "breed", length = TEXT_LENGTH)
    private String breed;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    /** Additional notes (optional). */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /** Required by JPA. Do not use directly — prefer {@link #from(Pet)}. */
    public PetJpaEntity() {}

    @SuppressWarnings("checkstyle:ParameterNumber")
    private PetJpaEntity(
            final String anId,
            final String aTutorId,
            final String aName,
            final String aSpecies,
            final String aBreed,
            final LocalDate aBirthDate,
            final String aNotes,
            final Instant aCreatedAt,
            final Instant anUpdatedAt) {
        this.id = anId;
        this.tutorId = aTutorId;
        this.name = aName;
        this.species = aSpecies;
        this.breed = aBreed;
        this.birthDate = aBirthDate;
        this.notes = aNotes;
        this.createdAt = aCreatedAt;
        this.updatedAt = anUpdatedAt;
    }

    /**
     * Converts a domain {@link Pet} to a persistable JPA entity.
     *
     * @param pet the domain entity to convert
     * @return a new {@code PetJpaEntity}
     */
    public static PetJpaEntity from(final Pet pet) {
        return new PetJpaEntity(
                pet.getId().getValue(),
                pet.getTutorId().getValue(),
                pet.getName(),
                pet.getSpecies(),
                pet.getBreed(),
                pet.getBirthDate(),
                pet.getNotes(),
                pet.getCreatedAt(),
                pet.getUpdatedAt());
    }

    /**
     * Reconstructs the domain {@link Pet} from this JPA entity.
     *
     * @return the domain entity
     */
    public Pet toAggregate() {
        return Pet.with(
                this.id,
                this.tutorId,
                this.name,
                this.species,
                this.breed,
                this.birthDate,
                this.notes,
                this.createdAt,
                this.updatedAt);
    }

    /**
     * @return the pet ID
     */
    public String getId() {
        return id;
    }

    /**
     * @param anId the pet ID
     */
    public void setId(final String anId) {
        this.id = anId;
    }

    /**
     * @return the tutor ID
     */
    public String getTutorId() {
        return tutorId;
    }

    /**
     * @param aTutorId the tutor ID
     */
    public void setTutorId(final String aTutorId) {
        this.tutorId = aTutorId;
    }

    /**
     * @return the pet name
     */
    public String getName() {
        return name;
    }

    /**
     * @param aName the pet name
     */
    public void setName(final String aName) {
        this.name = aName;
    }

    /**
     * @return the species
     */
    public String getSpecies() {
        return species;
    }

    /**
     * @param aSpecies the species
     */
    public void setSpecies(final String aSpecies) {
        this.species = aSpecies;
    }

    /**
     * @return the breed
     */
    public String getBreed() {
        return breed;
    }

    /**
     * @param aBreed the breed
     */
    public void setBreed(final String aBreed) {
        this.breed = aBreed;
    }

    /**
     * @return the birth date
     */
    public LocalDate getBirthDate() {
        return birthDate;
    }

    /**
     * @param aBirthDate the birth date
     */
    public void setBirthDate(final LocalDate aBirthDate) {
        this.birthDate = aBirthDate;
    }

    /**
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param aNotes the notes
     */
    public void setNotes(final String aNotes) {
        this.notes = aNotes;
    }

    /**
     * @return the creation timestamp
     */
    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * @param aCreatedAt the creation timestamp
     */
    public void setCreatedAt(final Instant aCreatedAt) {
        this.createdAt = aCreatedAt;
    }

    /**
     * @return the last update timestamp
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param anUpdatedAt the last update timestamp
     */
    public void setUpdatedAt(final Instant anUpdatedAt) {
        this.updatedAt = anUpdatedAt;
    }
}
