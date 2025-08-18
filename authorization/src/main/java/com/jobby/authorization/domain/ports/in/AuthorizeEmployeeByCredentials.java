package com.jobby.authorization.domain.ports.in;

import com.jobby.authorization.domain.model.TokenRegistry;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface AuthorizeEmployeeByCredentials {
    Result<TokenRegistry, Error> execute(String email, String password);
}
