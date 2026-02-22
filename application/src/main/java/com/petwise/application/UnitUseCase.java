package com.petwise.application;

/**
 * Abstract base class for use cases that take an input but produce no output.
 *
 * <p>Use this for fire-and-forget operations where the caller only needs to know that the command
 * succeeded (e.g., "Delete Tutor"). The absence of a return value signals that the operation is
 * purely side-effectful.
 *
 * @param <I> the command object that drives the use case
 */
@SuppressWarnings("PMD.GenericsNaming")
public abstract class UnitUseCase<I> {

    /** Protected constructor for subclasses. */
    protected UnitUseCase() {}

    /**
     * Executes the use case with the supplied input.
     *
     * @param anIn the input command; must not be {@code null}
     */
    public abstract void execute(I anIn);
}
