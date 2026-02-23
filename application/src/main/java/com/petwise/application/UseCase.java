package com.petwise.application;

/**
 * Abstract base class for use cases that take an input and produce an output.
 *
 * <p>This is the standard use-case contract for the application layer. Each concrete use case
 * represents a single, well-defined business operation (e.g., "Create Tutor"). Implementations must
 * be free of infrastructure concerns — they interact with the domain model and delegate persistence
 * to a gateway interface.
 *
 * @param <I> the command or query object that drives the use case
 * @param <O> the result produced by the use case
 */
@SuppressWarnings("PMD.GenericsNaming")
public abstract class UseCase<I, O> {

    /** Protected constructor for subclasses. */
    protected UseCase() {}

    /**
     * Executes the use case with the supplied input.
     *
     * @param anIN the input command or query; must not be {@code null}
     * @return the result of the use case execution
     */
    public abstract O execute(I anIN);
}
