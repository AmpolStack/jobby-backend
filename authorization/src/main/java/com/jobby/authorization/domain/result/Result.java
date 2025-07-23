package com.jobby.authorization.domain.result;

import java.util.Optional;

public sealed interface Result<T, E> permits Success, Failure {
    boolean isSuccess();

    default boolean isFailure(){
        return !isSuccess();
    };

    Optional<T> getValue();
    Optional<E> getError();
}
