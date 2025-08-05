package com.jobby.authorization.domain.ports.out.tokens;

import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.Result;
import com.jobby.authorization.domain.shared.TokenData;

public interface TokenGeneratorService {
    Result<String, Error> generate(TokenData data, String base64Key);
    Result<TokenData, Error> obtainData(String token , String base64Key);
    Result<Boolean, Error> isValid(String token, String base64Key);
}
