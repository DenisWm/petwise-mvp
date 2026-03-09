package com.petwise.application;

/**
 * Use case that takes an input but produces no output (fire-and-forget).
 *
 * @param <I> the command object
 */
@SuppressWarnings("PMD.GenericsNaming")
public abstract class UnitUseCase<I> {
    protected UnitUseCase() {}

    public abstract void execute(I anIn);
}
