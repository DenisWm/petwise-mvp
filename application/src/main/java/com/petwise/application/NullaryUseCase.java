package com.petwise.application;

/**
 * Abstract base class for use cases that require no input and produce an output.
 *
 * <p>Use this when the operation is fully self-contained and driven by context rather than
 * caller-supplied data (e.g., "Get current system health").
 *
 * @param <OUT> the result produced by the use case
 */
public abstract class NullaryUseCase<OUT> {

    /**
     * Executes the use case.
     *
     * @return the result of the use case execution
     */
    public abstract OUT execute();
}
