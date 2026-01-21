package com.petwise.domain;

public abstract class Identifier<T> extends ValueObject {

    public abstract T getValue();
}
