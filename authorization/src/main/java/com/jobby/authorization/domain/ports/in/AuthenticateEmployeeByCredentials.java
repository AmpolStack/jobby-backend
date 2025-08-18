package com.jobby.authorization.domain.ports.in;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface AuthenticateEmployeeByCredentials {
    Result<Void, Error> authenticate();
}
