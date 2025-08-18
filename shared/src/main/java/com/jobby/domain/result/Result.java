package com.jobby.domain.result;

import com.jobby.domain.mobility.Error;
import com.jobby.domain.mobility.ErrorType;
import com.jobby.domain.mobility.Field;
import java.util.Arrays;
import java.util.function.Function;

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

    static <T,U,E> Result<U, E> renewFailure(Result<T, E> result) {
        return new Failure<>(result.getError());
    }

    static <T,U> Result<U, Error> renewFailure(Result<T, Error> result, String fieldName) {
        Error oldErr = result.getError();

        Field[] updated = Arrays.stream(oldErr.getFields())
                .map(f -> {
                    if (!oldErr.getCode().name().startsWith("ITN_")) {
                        return new Field(fieldName, f.getReason());
                    } else {
                        return f;
                    }
                })
                .toArray(Field[]::new);

        Error newErr = new Error(oldErr.getCode(), updated);
        return Result.failure(newErr);
    }


    default  <U> Result<U, E> map(Function<T, U> fn) {
        if (this.isSuccess()) {
            return Result.success(fn.apply(this.getData()));
        } else {
            return Result.failure(this.getError());
        }
    }

    default <U> Result<U, E> flatMap(Function<T, Result<U, E>> fn) {
        if (this.isSuccess()) {
            return fn.apply(this.getData());
        } else {
            return Result.failure(this.getError());
        }
    }

    static <T,U,E> Result<U, E> mapError(Result<T, E> result) {
        if(result.isFailure()) {
            return new Failure<>(result.getError());
        }
        throw new InconsistencyResultException("The result is failure");
    }

}
