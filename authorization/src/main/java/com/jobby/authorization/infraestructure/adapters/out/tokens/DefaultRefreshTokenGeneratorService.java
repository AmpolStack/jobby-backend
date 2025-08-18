package com.jobby.authorization.infraestructure.adapters.out.tokens;

import com.jobby.authorization.domain.ports.out.tokens.RefreshTokenGeneratorService;
import com.jobby.domain.mobility.Error;
import com.jobby.domain.result.Result;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DefaultRefreshTokenGeneratorService implements RefreshTokenGeneratorService {
    @Override
    public Result<String, Error> generate() {
        var random = UUID.randomUUID();
        return Result.success(random.toString());
    }
}
