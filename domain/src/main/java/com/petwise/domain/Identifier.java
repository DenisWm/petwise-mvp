package com.petwise.domain;

/**
 * Base class for all strongly-typed identifiers in the domain model.
 *
 * <p>Identifiers are Value Objects that wrap a raw value (e.g., a {@link String} UUID) and provide
 * type-safety at the aggregate boundary. Subclasses must supply a concrete {@link #getValue()}
 * implementation and the usual {@code equals}/{@code hashCode} contract.
 *
 * @param <T> the underlying primitive type of the identifier value
 */
public abstract class Identifier<T> extends ValueObject {

    /**
     * Returns the raw underlying value of this identifier.
     *
     * @return the raw identifier value; never {@code null}
     */
    public abstract T getValue();
}
