package com.petwise.application;

/**
 * Use case that takes an input and produces an output.
 *
 * @param <I> the command or query object
 * @param <O> the result produced
 */
@SuppressWarnings("PMD.GenericsNaming")
public abstract class UseCase<I, O> {
    protected UseCase() {}

    public abstract O execute(I anIN);
}
