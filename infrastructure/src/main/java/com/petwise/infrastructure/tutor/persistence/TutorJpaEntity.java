package com.petwise.infrastructure.tutor.persistence;

import com.petwise.domain.tutor.Tutor;
import jakarta.persistence.*;
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
public class TutorJpaEntity {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /** Required by JPA. Do not use directly — prefer {@link #from(Tutor)}. */
    public TutorJpaEntity() {}

    private TutorJpaEntity(
            final String id,
            final String name,
            final String email,
            final String phone,
            final Instant createdAt,
            final Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static TutorJpaEntity from(final Tutor tutor) {
        return new TutorJpaEntity(
                tutor.getId().getValue(),
                tutor.getName(),
                tutor.getEmail() != null ? tutor.getEmail().getValue() : null,
                tutor.getPhone() != null ? tutor.getPhone().getValue() : null,
                tutor.getCreatedAt(),
                tutor.getUpdatedAt());
    }

    public Tutor toAggregate() {
        return Tutor.with(
                this.id, this.name, this.email, this.phone, this.createdAt, this.updatedAt);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
