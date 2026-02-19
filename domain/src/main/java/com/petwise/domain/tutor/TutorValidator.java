package com.petwise.domain.tutor;

import com.petwise.domain.validation.Error;
import com.petwise.domain.validation.ValidationHandler;
import com.petwise.domain.validation.Validator;

/** Validator for Tutor aggregate. */
public class TutorValidator extends Validator {

    private static final int NAME_MAX_LENGTH = 255;
    private final Tutor tutor;

    public TutorValidator(final Tutor tutor, final ValidationHandler validationHandler) {
        super(validationHandler);
        this.tutor = tutor;
    }

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
