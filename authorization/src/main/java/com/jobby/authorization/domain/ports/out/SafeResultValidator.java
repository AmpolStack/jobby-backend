package com.jobby.authorization.domain.ports.out;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface SafeResultValidator {
    <T> Result<Void, Error> validate(T entity);
}
