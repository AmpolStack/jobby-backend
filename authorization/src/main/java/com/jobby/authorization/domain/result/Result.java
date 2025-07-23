package com.jobby.authorization.domain.result;

import java.util.Optional;

public interface Result<T, E> {
    boolean isSuccess();

    default boolean isFailure(){
        return !isSuccess();
    };

    Optional<T> getValue();
    Optional<E> getError();
}
