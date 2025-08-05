package com.jobby.authorization.domain.ports.out.tokens;

import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.Result;
import com.jobby.authorization.domain.shared.TokenData;

public interface TokenGeneratorService {
    Result<String, Error> generateToken(TokenData data, String Key, int expirationTime);
}
