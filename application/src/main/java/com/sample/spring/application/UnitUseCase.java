package com.sample.spring.application;

public abstract class UnitUseCase<IN> {

    public abstract void execute(IN anIn);
}
