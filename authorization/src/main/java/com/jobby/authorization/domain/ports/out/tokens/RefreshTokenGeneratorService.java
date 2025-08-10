package com.jobby.authorization.domain.ports.out.tokens;

import com.jobby.authorization.domain.shared.result.Error;
import com.jobby.authorization.domain.shared.result.Result;

public interface RefreshTokenGeneratorService {
    Result<String, Error> generate();
}
