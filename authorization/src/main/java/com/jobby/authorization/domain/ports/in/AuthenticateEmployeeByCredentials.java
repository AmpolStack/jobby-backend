package com.jobby.authorization.domain.ports.in;

import com.jobby.domain.mobility.Error;
import com.jobby.authorization.domain.shared.result.Result;

public interface AuthenticateEmployeeByCredentials {
    Result<Void, Error> authenticate();
}
