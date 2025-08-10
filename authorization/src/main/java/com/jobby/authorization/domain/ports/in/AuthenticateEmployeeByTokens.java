package com.jobby.authorization.domain.ports.in;

import com.jobby.authorization.domain.model.TokenRegistry;
import com.jobby.authorization.domain.shared.result.Error;
import com.jobby.authorization.domain.shared.result.Result;

public interface AuthenticateEmployeeByTokens {
    Result<TokenRegistry, Error> execute(String token, String refreshToken, int id);
}
