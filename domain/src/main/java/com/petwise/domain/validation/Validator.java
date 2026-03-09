package com.petwise.domain.validation;

import java.util.Objects;

/**
 * Base class for domain validators. Appends {@link Error}s to an injected {@link ValidationHandler}
 * rather than throwing, so every broken rule is reported at once.
 */
public abstract class Validator {
    private final ValidationHandler validationHandler;

    public Validator(final ValidationHandler handler) {
        this.validationHandler =
                Objects.requireNonNull(handler, "validationHandler should not be null");
    }

    public abstract void validate();

    public ValidationHandler getValidationHandler() {
        return validationHandler;
    }
}
