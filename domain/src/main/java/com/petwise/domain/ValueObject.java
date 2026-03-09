package com.petwise.domain;

/**
 * Marker base class for all Value Objects in the domain model.
 *
 * <p>Value Objects are immutable objects defined entirely by their attributes. They carry no
 * conceptual identity — two Value Objects with the same attributes are considered equal. Subclasses
 * must implement {@code equals} and {@code hashCode} based solely on their fields.
 *
 * @see <a href="https://martinfowler.com/bliki/ValueObject.html">Martin Fowler – Value Object</a>
 */
@SuppressWarnings({"PMD.AbstractClassWithoutAnyMethod", "PMD.AbstractClassWithoutAbstractMethod"})
public abstract class ValueObject {
    protected ValueObject() {}
}
