package com.jobby.authorization.domain.ports.in;

import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.Result;

public interface AuthenticateEmployeeUseCase {
    Result<Void, Error> authenticate();
}
