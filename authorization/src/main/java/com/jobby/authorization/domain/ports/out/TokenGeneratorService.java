package com.jobby.authorization.domain.ports.out;

import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.Result;

public interface TokenGeneratorService {
    Result<String, Error> generateToken(String[] claimData, String Key);
}
