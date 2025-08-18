package com.jobby.authorization.domain.ports.in;

import com.jobby.authorization.domain.model.TokenRegistry;
import com.jobby.domain.mobility.Error;
import com.jobby.domain.result.Result;

public interface AuthorizeEmployeeByTokens {
    Result<TokenRegistry, Error> execute(String token, String refreshToken, int id);
}
