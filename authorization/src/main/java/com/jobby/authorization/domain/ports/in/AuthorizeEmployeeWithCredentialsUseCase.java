package com.jobby.authorization.domain.ports.in;

import com.jobby.authorization.domain.model.TokenRegistry;
import com.jobby.authorization.domain.shared.result.Error;
import com.jobby.authorization.domain.shared.result.Result;

public interface AuthorizeEmployeeWithCredentialsUseCase {
    Result<TokenRegistry, Error> byCredentials(String email, String password);
}
