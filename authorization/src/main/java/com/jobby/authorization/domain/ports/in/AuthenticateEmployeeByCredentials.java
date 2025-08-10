package com.jobby.authorization.domain.ports.in;

import com.jobby.authorization.domain.shared.result.Error;
import com.jobby.authorization.domain.shared.result.Result;

public interface AuthenticateEmployeeWithCredentialsUseCase {
    Result<Void, Error> authenticate();
}
