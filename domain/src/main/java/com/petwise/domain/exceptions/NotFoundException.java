package com.petwise.domain.exceptions;

import com.petwise.domain.AggregateRoot;
import com.petwise.domain.Identifier;
import com.petwise.domain.validation.Error;
import java.util.Collections;
import java.util.List;

/**
 * Exception thrown when a requested aggregate root cannot be found in the persistence store.
 *
 * <p>Use the static factory {@link #with(Class, Identifier)} to create a well-formatted instance.
 * The resulting exception message follows the pattern:
 *
 * <pre>{@code <AggregateName> with ID <id> was not found}</pre>
 */
public class NotFoundException extends DomainException {

    protected NotFoundException(final String message, final List<Error> errors) {
        super(message, errors);
    }

    /**
     * Creates a {@code NotFoundException} for the given aggregate class and identifier.
     *
     * @param aggregate the aggregate root class that was not found
     * @param id the identifier that was searched for
     * @return a {@code NotFoundException} with a descriptive message
     */
    public static NotFoundException with(
            final Class<? extends AggregateRoot<?>> aggregate, final Identifier<?> id) {
        final var anError =
                "%s with ID %s was not found".formatted(aggregate.getSimpleName(), id.getValue());
        return new NotFoundException(anError, Collections.emptyList());
    }
}
