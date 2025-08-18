package com.jobby.authorization.domain.ports.out.tokens;

import com.jobby.domain.mobility.Error;
import com.jobby.domain.result.Result;

public interface RefreshTokenGeneratorService {
    Result<String, Error> generate();
}
