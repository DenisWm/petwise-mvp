package com.sample.spring.domain;

public abstract class Identifier<T> extends ValueObject {

    public abstract T getValue();
}
