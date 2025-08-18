package com.jobby.authorization.domain.ports.in;

import com.jobby.domain.mobility.Error;
import com.jobby.domain.result.Result;

public interface AuthenticateEmployeeByCredentials {
    Result<Void, Error> authenticate();
}
