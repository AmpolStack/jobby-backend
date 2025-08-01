package com.jobby.authorization.domain.result;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class Success<T, E> implements Result<T,E>{
    private final T data;

    public Success(final T data) {
        this.data = data;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public T getData() {
        return this.data;
    }

    @Override
    public E getError() {
        return null;
    }
}
