package com.jobby.authorization.domain.ports.in;

import com.jobby.authorization.domain.model.TokenRegistry;
import com.jobby.authorization.domain.shared.errors.Error;
import com.jobby.authorization.domain.shared.result.Result;

public interface AuthorizeEmployeeByCredentials {
    Result<TokenRegistry, Error> execute(String email, String password);
}
