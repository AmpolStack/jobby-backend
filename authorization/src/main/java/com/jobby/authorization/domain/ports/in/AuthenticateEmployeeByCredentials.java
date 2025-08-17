package com.jobby.authorization.domain.ports.in;

import com.jobby.authorization.domain.shared.errors.Error;
import com.jobby.authorization.domain.shared.result.Result;

public interface AuthenticateEmployeeByCredentials {
    Result<Void, Error> authenticate();
}
