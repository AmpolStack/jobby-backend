package com.jobby.authorization.domain.result;

import java.util.Optional;

public final class Failure<T> implements Result<T, Error[]>{
    private Error[] errors;

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public Optional<T> getValue() {
        return Optional.empty();
    }

    @Override
    public Optional<Error[]> getError() {
        return Optional.of(errors);
    }
}
