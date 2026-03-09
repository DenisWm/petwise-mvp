package com.petwise.infrastructure.appointment.persistence;

import com.petwise.domain.appointment.Appointment;
import com.petwise.domain.appointment.AppointmentStatus;
import com.petwise.domain.appointment.ServiceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * JPA entity that maps the {@code appointments} database table to the {@link Appointment}
 * aggregate.
 *
 * <p>Use {@link #from(Appointment)} to convert a domain object into a persistable entity and {@link
 * #toAggregate()} to reconstruct the domain object after a query.
 */
@Entity
@Table(name = "appointments")
@SuppressWarnings({"PMD.ShortVariable", "PMD.DataClass"})
public class AppointmentJpaEntity {
    private static final int ID_LENGTH = 36;
    private static final int ENUM_LENGTH = 50;
    private static final int STATUS_LENGTH = 20;

    @Id
    @Column(name = "id", nullable = false, length = ID_LENGTH)
    private String id;

    @Column(name = "pet_id", nullable = false, length = ID_LENGTH)
    private String petId;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false, length = ENUM_LENGTH)
    private ServiceType serviceType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = STATUS_LENGTH)
    private AppointmentStatus status;

    @Column(name = "start_at", nullable = false)
    private Instant startAt;

    @Column(name = "end_at", nullable = false)
    private Instant endAt;

    /** Optional notes. */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /** Required by JPA. Do not use directly — prefer {@link #from(Appointment)}. */
    public AppointmentJpaEntity() {}

    @SuppressWarnings("PMD.ExcessiveParameterList")
    private AppointmentJpaEntity(
            final String anId,
            final String aPetId,
            final ServiceType aServiceType,
            final AppointmentStatus aStatus,
            final Instant aStartAt,
            final Instant anEndAt,
            final String aNotes,
            final Instant aCreatedAt,
            final Instant anUpdatedAt) {
        this.id = anId;
        this.petId = aPetId;
        this.serviceType = aServiceType;
        this.status = aStatus;
        this.startAt = aStartAt;
        this.endAt = anEndAt;
        this.notes = aNotes;
        this.createdAt = aCreatedAt;
        this.updatedAt = anUpdatedAt;
    }

    /**
     * Converts a domain {@link Appointment} to a persistable JPA entity.
     *
     * @param appointment the domain aggregate to convert
     * @return a new {@code AppointmentJpaEntity}
     */
    public static AppointmentJpaEntity from(final Appointment appointment) {
        return new AppointmentJpaEntity(
                appointment.getId().getValue(),
                appointment.getPetId().getValue(),
                appointment.getServiceType(),
                appointment.getStatus(),
                appointment.getStartAt(),
                appointment.getEndAt(),
                appointment.getNotes(),
                appointment.getCreatedAt(),
                appointment.getUpdatedAt());
    }

    /**
     * Reconstructs the domain {@link Appointment} aggregate from this entity.
     *
     * @return the domain aggregate
     */
    public Appointment toAggregate() {
        return Appointment.with(
                this.id,
                this.petId,
                this.serviceType,
                this.status,
                this.startAt,
                this.endAt,
                this.notes,
                this.createdAt,
                this.updatedAt);
    }

    /**
     * @return the appointment ID
     */
    public String getId() {
        return id;
    }

    /**
     * @param anId the appointment ID
     */
    public void setId(final String anId) {
        this.id = anId;
    }

    /**
     * @return the pet ID
     */
    public String getPetId() {
        return petId;
    }

    /**
     * @param aPetId the pet ID
     */
    public void setPetId(final String aPetId) {
        this.petId = aPetId;
    }

    /**
     * @return the service type
     */
    public ServiceType getServiceType() {
        return serviceType;
    }

    /**
     * @param aServiceType the service type
     */
    public void setServiceType(final ServiceType aServiceType) {
        this.serviceType = aServiceType;
    }

    /**
     * @return the status
     */
    public AppointmentStatus getStatus() {
        return status;
    }

    /**
     * @param aStatus the status
     */
    public void setStatus(final AppointmentStatus aStatus) {
        this.status = aStatus;
    }

    /**
     * @return the start timestamp
     */
    public Instant getStartAt() {
        return startAt;
    }

    /**
     * @param aStartAt the start timestamp
     */
    public void setStartAt(final Instant aStartAt) {
        this.startAt = aStartAt;
    }

    /**
     * @return the end timestamp
     */
    public Instant getEndAt() {
        return endAt;
    }

    /**
     * @param anEndAt the end timestamp
     */
    public void setEndAt(final Instant anEndAt) {
        this.endAt = anEndAt;
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
