package com.petwise.application;

/**
 * Use case that requires no input and produces an output.
 *
 * @param <O> the result produced
 */
@SuppressWarnings("PMD.GenericsNaming")
public abstract class NullaryUseCase<O> {
    protected NullaryUseCase() {}

    public abstract O execute();
}
