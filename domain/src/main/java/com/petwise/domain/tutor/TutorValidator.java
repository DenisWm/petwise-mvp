package com.petwise.domain.tutor;

import com.petwise.domain.validation.Error;
import com.petwise.domain.validation.ValidationHandler;
import com.petwise.domain.validation.Validator;

/** Validator for Tutor aggregate. */
@SuppressWarnings("PMD.OnlyOneReturn")
public final class TutorValidator extends Validator {

    /** Maximum allowed length for tutor name. */
    private static final int NAME_MAX_LENGTH = 255;

    /** The tutor being validated. */
    private final Tutor tutor;

    /**
     * Constructs a TutorValidator for the given tutor and handler.
     *
     * @param aTutor the tutor to validate
     * @param handler the handler to collect errors
     */
    public TutorValidator(final Tutor aTutor, final ValidationHandler handler) {
        super(handler);
        this.tutor = aTutor;
    }

    /**
     * Validates the tutor aggregate invariants.
     *
     * <p>Subclasses may override to add additional validation rules.
     */
    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.tutor.getName();
        if (name == null) {
            this.getValidationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if (name.isBlank()) {
            this.getValidationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        final int length = name.trim().length();
        if (length > NAME_MAX_LENGTH) {
            this.getValidationHandler()
                    .append(new Error("'name' must be between 1 and 255 characters"));
        }
    }
}
