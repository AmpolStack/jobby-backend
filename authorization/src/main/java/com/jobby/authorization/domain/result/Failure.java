package com.jobby.authorization.domain.result;

import java.util.Optional;

public class Failure<T, E> implements Result<T, E>{
    private E error;

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public Optional<T> getValue() {
        return Optional.empty();
    }

    @Override
    public Optional<E> getError() {
        return Optional.of(error);
    }
}
