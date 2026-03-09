package com.petwise.infrastructure.tutor.persistence;

import com.petwise.domain.tutor.Tutor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * JPA entity that maps the {@code tutors} database table to the {@link Tutor} aggregate.
 *
 * <p>This class is an infrastructure concern and must never leak into the domain or application
 * layers. Use {@link #from(Tutor)} to convert a domain aggregate into a persistable entity and
 * {@link #toAggregate()} to reconstruct the domain object after a query.
 */
@Entity
@Table(name = "tutors")
@SuppressWarnings({"PMD.ShortVariable", "PMD.DataClass"})
public class TutorJpaEntity {
    private static final int ID_LENGTH = 36;
    private static final int TEXT_LENGTH = 255;
    private static final int PHONE_LENGTH = 20;

    @Id
    @Column(name = "id", nullable = false, length = ID_LENGTH)
    private String id;

    @Column(name = "name", nullable = false, length = TEXT_LENGTH)
    private String name;

    @Column(name = "email", length = TEXT_LENGTH)
    private String email;

    @Column(name = "phone", length = PHONE_LENGTH)
    private String phone;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /** Required by JPA. Do not use directly — prefer {@link #from(Tutor)}. */
    public TutorJpaEntity() {}

    private TutorJpaEntity(
            final String anId,
            final String aName,
            final String anEmail,
            final String aPhone,
            final Instant aCreatedAt,
            final Instant anUpdatedAt) {
        this.id = anId;
        this.name = aName;
        this.email = anEmail;
        this.phone = aPhone;
        this.createdAt = aCreatedAt;
        this.updatedAt = anUpdatedAt;
    }

    /**
     * Converts a domain {@link Tutor} to a persistable JPA entity.
     *
     * @param tutor the domain aggregate to convert
     * @return a new {@code TutorJpaEntity}
     */
    public static TutorJpaEntity from(final Tutor tutor) {
        return new TutorJpaEntity(
                tutor.getId().getValue(),
                tutor.getName(),
                tutor.getEmail() != null ? tutor.getEmail().getValue() : null,
                tutor.getPhone() != null ? tutor.getPhone().getValue() : null,
                tutor.getCreatedAt(),
                tutor.getUpdatedAt());
    }

    /**
     * Reconstructs the domain {@link Tutor} aggregate from this entity.
     *
     * @return the domain aggregate
     */
    public Tutor toAggregate() {
        return Tutor.with(
                this.id, this.name, this.email, this.phone, this.createdAt, this.updatedAt);
    }

    public String getId() {
        return id;
    }

    /**
     * Sets the tutor ID.
     *
     * @param anId the ID string to set
     */
    public void setId(final String anId) {
        this.id = anId;
    }

    public String getName() {
        return name;
    }

    /**
     * Sets the tutor name.
     *
     * @param aName the name to set
     */
    public void setName(final String aName) {
        this.name = aName;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Sets the tutor email.
     *
     * @param anEmail the email to set
     */
    public void setEmail(final String anEmail) {
        this.email = anEmail;
    }

    public String getPhone() {
        return phone;
    }

    /**
     * Sets the tutor phone.
     *
     * @param aPhone the phone to set
     */
    public void setPhone(final String aPhone) {
        this.phone = aPhone;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param aCreatedAt the timestamp to set
     */
    public void setCreatedAt(final Instant aCreatedAt) {
        this.createdAt = aCreatedAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the last-updated timestamp.
     *
     * @param anUpdatedAt the timestamp to set
     */
    public void setUpdatedAt(final Instant anUpdatedAt) {
        this.updatedAt = anUpdatedAt;
    }
}
