package com.petwise.domain.tutor;

import com.petwise.domain.AggregateRoot;
import com.petwise.domain.validation.ValidationHandler;
import java.time.Instant;
import java.util.Objects;

/** Tutor aggregate root. Represents a pet owner or guardian in the PetWise system. */
public class Tutor extends AggregateRoot<TutorID> {

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

    /**
     * Creates a new Tutor with generated ID.
     *
     * @param aName the tutor name (required)
     * @param anEmail the tutor email (optional)
     * @param aPhone the tutor phone (optional)
     * @return a new Tutor instance
     */
    public static Tutor newTutor(final String aName, final String anEmail, final String aPhone) {
        final var id = TutorID.unique();
        final var now = Instant.now();
        final var email = Email.from(anEmail);
        final var phone = Phone.from(aPhone);
        return new Tutor(id, aName, email, phone, now, now);
    }

    /**
     * Creates a Tutor with all properties (for reconstruction from persistence).
     *
     * @param anId the tutor ID
     * @param aName the tutor name
     * @param anEmail the tutor email
     * @param aPhone the tutor phone
     * @param aCreatedAt the creation timestamp
     * @param anUpdatedAt the last update timestamp
     * @return a Tutor instance
     */
    public static Tutor with(
            final TutorID anId,
            final String aName,
            final Email anEmail,
            final Phone aPhone,
            final Instant aCreatedAt,
            final Instant anUpdatedAt) {
        return new Tutor(anId, aName, anEmail, aPhone, aCreatedAt, anUpdatedAt);
    }

    /**
     * Creates a Tutor with string values (for reconstruction from persistence).
     *
     * @param anId the tutor ID
     * @param aName the tutor name
     * @param anEmail the tutor email string (optional)
     * @param aPhone the tutor phone string (optional)
     * @param aCreatedAt the creation timestamp
     * @param anUpdatedAt the last update timestamp
     * @return a Tutor instance
     */
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
