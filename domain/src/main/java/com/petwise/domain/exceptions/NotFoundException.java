package com.petwise.domain.exceptions;

import com.petwise.domain.Entity;
import com.petwise.domain.Identifier;
import com.petwise.domain.validation.Error;
import java.util.Collections;
import java.util.List;

/**
 * Thrown when a domain object cannot be found. Message pattern: {@code <Type> with ID <id> was not
 * found}.
 */
@SuppressWarnings({"PMD.MissingSerialVersionUID", "PMD.ShortVariable"})
public class NotFoundException extends DomainException {
    protected NotFoundException(final String message, final List<Error> anErrors) {
        super(message, anErrors);
    }

    public static NotFoundException with(
            final Class<? extends Entity<?>> entity, final Identifier<?> anId) {
        final var anError =
                "%s with ID %s was not found".formatted(entity.getSimpleName(), anId.getValue());
        return new NotFoundException(anError, Collections.emptyList());
    }
}
