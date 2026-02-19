package com.petwise.domain.exceptions;

import com.petwise.domain.AggregateRoot;
import com.petwise.domain.Identifier;
import com.petwise.domain.validation.Error;
import java.util.Collections;
import java.util.List;

/** Exception thrown when an aggregate root is not found. */
public class NotFoundException extends DomainException {

    protected NotFoundException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    public static NotFoundException with(
            final Class<? extends AggregateRoot<?>> aggregate, final Identifier<?> id) {
        final var anError =
                "%s with ID %s was not found".formatted(aggregate.getSimpleName(), id.getValue());
        return new NotFoundException(anError, Collections.emptyList());
    }
}
