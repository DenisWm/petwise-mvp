package com.petwise.domain.appointment;

import com.petwise.domain.AggregateRoot;
import com.petwise.domain.exceptions.DomainException;
import com.petwise.domain.pet.PetID;
import com.petwise.domain.validation.Error;
import com.petwise.domain.validation.ValidationHandler;
import java.time.Instant;
import java.util.Objects;

/** Appointment aggregate root. Represents a scheduled service for a pet. */
@SuppressWarnings({
    "PMD.UseObjectForClearerAPI",
    "PMD.ShortVariable",
    "PMD.ClassWithOnlyPrivateConstructorsShouldBeFinal"
})
public final class Appointment extends AggregateRoot<AppointmentID> {
    private final PetID petId;
    private final ServiceType serviceType;

    /** Appointment status. */
    private AppointmentStatus status;

    /** Start timestamp. */
    private final Instant startAt;

    /** End timestamp. */
    private final Instant endAt;

    /** Optional notes. */
    private final String notes;

    private final Instant createdAt;
    private Instant updatedAt;

    private Appointment(
            final AppointmentID anId,
            final PetID aPetId,
            final ServiceType aServiceType,
            final AppointmentStatus aStatus,
            final Instant aStartAt,
            final Instant anEndAt,
            final String aNotes,
            final Instant aCreatedAt,
            final Instant anUpdatedAt) {
        super(anId);
        this.petId = aPetId;
        this.serviceType = aServiceType;
        this.status = aStatus;
        this.startAt = aStartAt;
        this.endAt = anEndAt;
        this.notes = aNotes;
        this.createdAt = Objects.requireNonNull(aCreatedAt, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(anUpdatedAt, "'updatedAt' should not be null");
    }

    public static Appointment newAppointment(
            final PetID aPetId,
            final ServiceType aServiceType,
            final Instant aStartAt,
            final Instant anEndAt,
            final String aNotes) {
        final var anId = AppointmentID.unique();
        final var now = Instant.now();
        return new Appointment(
                anId,
                aPetId,
                aServiceType,
                AppointmentStatus.PENDING,
                aStartAt,
                anEndAt,
                aNotes,
                now,
                now);
    }

    /** Reconstructs an Appointment from typed objects (persistence). */
    public static Appointment with(
            final AppointmentID anId,
            final PetID aPetId,
            final ServiceType aServiceType,
            final AppointmentStatus aStatus,
            final Instant aStartAt,
            final Instant anEndAt,
            final String aNotes,
            final Instant aCreatedAt,
            final Instant anUpdatedAt) {
        return new Appointment(
                anId,
                aPetId,
                aServiceType,
                aStatus,
                aStartAt,
                anEndAt,
                aNotes,
                aCreatedAt,
                anUpdatedAt);
    }

    /** Reconstructs an Appointment from raw strings (persistence). */
    public static Appointment with(
            final String anId,
            final String aPetId,
            final ServiceType aServiceType,
            final AppointmentStatus aStatus,
            final Instant aStartAt,
            final Instant anEndAt,
            final String aNotes,
            final Instant aCreatedAt,
            final Instant anUpdatedAt) {
        return new Appointment(
                AppointmentID.from(anId),
                PetID.from(aPetId),
                aServiceType,
                aStatus,
                aStartAt,
                anEndAt,
                aNotes,
                aCreatedAt,
                anUpdatedAt);
    }

    public Appointment changeStatus(final AppointmentStatus aStatus) {
        if (aStatus == null) {
            throw DomainException.with(new Error("'status' should not be null"));
        }
        if (!this.status.canTransitionTo(aStatus)) {
            throw DomainException.with(
                    new Error("'status' cannot transition from " + this.status + " to " + aStatus));
        }
        this.status = aStatus;
        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new AppointmentValidator(this, handler).validate();
    }

    public PetID getPetId() {
        return petId;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public Instant getStartAt() {
        return startAt;
    }

    public Instant getEndAt() {
        return endAt;
    }

    public String getNotes() {
        return notes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
