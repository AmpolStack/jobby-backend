package com.jobby.authorization.domain.result;

public class InconsistencyResultException extends RuntimeException {
    public InconsistencyResultException(String message) {
        super(message);
    }
}
