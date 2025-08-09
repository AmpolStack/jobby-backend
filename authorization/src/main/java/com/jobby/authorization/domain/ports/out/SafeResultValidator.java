package com.jobby.authorization.domain.ports.out;

import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.Result;

public interface SafeResultValidator {
    <T> Result<Void, Error> validate(T entity);
}
