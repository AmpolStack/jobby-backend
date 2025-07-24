package com.jobby.authorization.domain.result;

import java.util.List;

public sealed interface Result<T,E> permits Success, Failure {

    boolean isSuccess();

    default boolean isFailure() {
        return !isSuccess();
    }

    T getData();

    E getError();

    static <T, E> Result<T, E> success(T data) {
        return new Success<>(data);
    }

    static <T, E> Result<T, E> failure(E error) {
        return new Failure<>(error);
    }

    static <T> Result<T, Error> failure(ErrorType errorCode, Field[] field) {
        return new Failure<>(new Error(errorCode, field));
    }

    static <T> Result<T, Error> failure(ErrorType errorCode, Field field) {
        return new Failure<>(new Error(errorCode, new Field[]{field}));
    }

    static <T,U,E> Result<U, E> mapError(Result<T, E> result) {
        if(result.isFailure()) {
            return new Failure<>(result.getError());
        }
        throw new InconsistencyResultException("The result is failure");
    }

}
