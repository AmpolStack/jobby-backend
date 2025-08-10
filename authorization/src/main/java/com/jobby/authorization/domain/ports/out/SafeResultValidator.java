package com.jobby.authorization.domain.ports.out;

import com.jobby.authorization.domain.shared.result.Error;
import com.jobby.authorization.domain.shared.result.Result;

public interface SafeResultValidator {
    <T> Result<Void, Error> validate(T entity);
}
