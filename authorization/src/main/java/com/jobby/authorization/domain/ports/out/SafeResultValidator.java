package com.jobby.authorization.domain.ports.out;

import com.jobby.domain.mobility.Error;
import com.jobby.domain.result.Result;

public interface SafeResultValidator {
    <T> Result<Void, Error> validate(T entity);
}
