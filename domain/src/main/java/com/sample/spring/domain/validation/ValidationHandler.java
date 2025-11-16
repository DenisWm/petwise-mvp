package com.sample.spring.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(Error anError);

    ValidationHandler append(ValidationHandler aValidationHandler);

    <T> T validate(Validation<T> aValidation);

    default Error firstError() {
        if(getErrors() != null && !getErrors().isEmpty()) {
            return getErrors().get(0);
        } else {
            return null;
        }
    }

    default boolean hasErrors() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    List<Error> getErrors();

    interface Validation<T> {
        T validate();
    }
}
