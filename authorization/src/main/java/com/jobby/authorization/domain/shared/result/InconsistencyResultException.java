package com.jobby.authorization.domain.shared.result;

public class InconsistencyResultException extends RuntimeException {
    public InconsistencyResultException(String message) {
        super(message);
    }
}
