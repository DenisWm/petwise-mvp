package com.petwise.domain.validation;

/**
 * Immutable record that holds a single human-readable validation or domain error message.
 *
 * <p>Instances are produced by validators and collected by a {@link ValidationHandler}. They are
 * also carried inside domain exceptions so that the presentation layer can display meaningful
 * feedback to the caller.
 *
 * @param message a non-null, human-readable description of the constraint violation
 */
public record Error(String message) {}
