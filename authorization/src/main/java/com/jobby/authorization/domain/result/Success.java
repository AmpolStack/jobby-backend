package com.jobby.authorization.domain.result;

import java.util.Optional;

public final class Success<T, E> implements Result<T, E> {
    private T result;

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public Optional<T> getValue() {
        return Optional.of(result);
    }

    @Override
    public Optional<E> getError() {
        return Optional.empty();
    }
}
