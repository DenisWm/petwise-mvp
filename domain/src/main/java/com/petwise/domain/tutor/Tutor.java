package com.petwise.domain.tutor;

import com.petwise.domain.AggregateRoot;
import com.petwise.domain.validation.ValidationHandler;
import java.time.Instant;
import java.util.Objects;

/** Tutor aggregate root. Represents a pet owner or guardian in the PetWise system. */
@SuppressWarnings({
    "PMD.UseObjectForClearerAPI",
    "PMD.ShortVariable",
    "PMD.ClassWithOnlyPrivateConstructorsShouldBeFinal"
})
public final class Tutor extends AggregateRoot<TutorID> {
    private String name;
    private Email email;
    private Phone phone;
    private final Instant createdAt;
    private Instant updatedAt;

    private Tutor(
            final TutorID anId,
            final String aName,
            final Email anEmail,
            final Phone aPhone,
            final Instant aCreatedAt,
            final Instant anUpdatedAt) {
        super(anId);
        this.name = aName;
        this.email = anEmail;
        this.phone = aPhone;
        this.createdAt = Objects.requireNonNull(aCreatedAt, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(anUpdatedAt, "'updatedAt' should not be null");
    }

    public static Tutor newTutor(final String aName, final String anEmail, final String aPhone) {
        final var anId = TutorID.unique();
        final var now = Instant.now();
        final var anEmailObj = Email.from(anEmail);
        final var aPhoneObj = Phone.from(aPhone);
        return new Tutor(anId, aName, anEmailObj, aPhoneObj, now, now);
    }

    /** Reconstructs a Tutor from typed objects (persistence). */
    public static Tutor with(
            final TutorID anId,
            final String aName,
            final Email anEmail,
            final Phone aPhone,
            final Instant aCreatedAt,
            final Instant anUpdatedAt) {
        return new Tutor(anId, aName, anEmail, aPhone, aCreatedAt, anUpdatedAt);
    }

    /** Reconstructs a Tutor from raw strings (persistence). */
    public static Tutor with(
            final String anId,
            final String aName,
            final String anEmail,
            final String aPhone,
            final Instant aCreatedAt,
            final Instant anUpdatedAt) {
        return new Tutor(
                TutorID.from(anId),
                aName,
                Email.from(anEmail),
                Phone.from(aPhone),
                aCreatedAt,
                anUpdatedAt);
    }

    /**
     * Updates the tutor information.
     *
     * @param aName the new name
     * @param anEmail the new email (optional)
     * @param aPhone the new phone (optional)
     * @return the updated Tutor instance
     */
    public Tutor update(final String aName, final String anEmail, final String aPhone) {
        this.name = aName;
        this.email = Email.from(anEmail);
        this.phone = Phone.from(aPhone);
        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new TutorValidator(this, handler).validate();
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public Phone getPhone() {
        return phone;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
