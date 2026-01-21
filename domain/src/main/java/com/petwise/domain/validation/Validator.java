package com.petwise.domain.validation;

import java.util.Objects;

public abstract class Validator {

    private final ValidationHandler validationHandler;

    public Validator(final ValidationHandler validationHandler) {
        this.validationHandler =
                Objects.requireNonNull(validationHandler, "validationHandler should not be null");
    }

    public abstract void validate();

    public ValidationHandler getValidationHandler() {
        return validationHandler;
    }
}
