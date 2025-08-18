package com.jobby.authorization.domain.ports.out.tokens;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface RefreshTokenGeneratorService {
    Result<String, Error> generate();
}
