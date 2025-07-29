package com.jobby.authorization.domain.result;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class Failure<T,E> implements Result<T,E>{
    private final E errors;

    public Failure(final E errors) {
        this.errors = errors;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public T getData() {
        return null;
    }

    @Override
    public E getError() {
        return this.errors;
    }
}
