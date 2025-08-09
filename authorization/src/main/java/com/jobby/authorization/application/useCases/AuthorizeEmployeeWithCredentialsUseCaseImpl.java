package com.jobby.authorization.application.useCases;

import com.jobby.authorization.domain.model.TokenRegistry;
import com.jobby.authorization.domain.ports.in.AuthorizeEmployeeWithCredentialsUseCase;
import com.jobby.authorization.domain.ports.out.CacheService;
import com.jobby.authorization.domain.ports.out.repositories.EmployeeRepository;
import com.jobby.authorization.domain.ports.out.tokens.RefreshTokenGeneratorService;
import com.jobby.authorization.domain.ports.out.tokens.TokenGeneratorService;
import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;
import com.jobby.authorization.domain.shared.TokenData;
import com.jobby.authorization.infraestructure.config.TokenConfig;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
public class AuthorizeEmployeeWithCredentialsUseCaseImpl implements AuthorizeEmployeeWithCredentialsUseCase {

    private final EmployeeRepository employeeRepository;
    private final TokenGeneratorService tokenGeneratorService;
    private final RefreshTokenGeneratorService refreshTokenGeneratorService;
    private final TokenConfig tokenConfig;
    private final CacheService cacheService;

    public AuthorizeEmployeeWithCredentialsUseCaseImpl(
            EmployeeRepository employeeRepository,
            TokenGeneratorService tokenGeneratorService,
            RefreshTokenGeneratorService refreshTokenGeneratorService,
            TokenConfig tokenConfig,
            CacheService cacheService) {
        this.employeeRepository = employeeRepository;
        this.tokenGeneratorService = tokenGeneratorService;
        this.refreshTokenGeneratorService = refreshTokenGeneratorService;
        this.tokenConfig = tokenConfig;
        this.cacheService = cacheService;
    }

    private Result<Void, Error> validateTokenConfig(TokenConfig cfg) {
        if (cfg == null) {
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field("tokenConfig", "is required"));
        }

        if (cfg.getExpirationMs() <= 0) {
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field("tokenConfig.expirationMs", "must be greater than 0"));
        }

        if (cfg.getRefreshExpirationMs() <= 0) {
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field("tokenConfig.refreshExpirationMs", "must be greater than 0"));
        }

        if (cfg.getSecretKey() == null || cfg.getSecretKey().isBlank()) {
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field("tokenConfig.secretKey", "the secret key is required or blank"));
        }

        return Result.success(null);
    }

    @Override
    public Result<TokenRegistry, Error> byCredentials(String email, String password) {
        return validateTokenConfig(this.tokenConfig)
                .flatMap(x -> employeeRepository.findByEmailAndPassword(email, password))
                .map(this::buildTokenData)
                .flatMap(tokenData ->
                        refreshTokenGeneratorService.generate()
                                .flatMap(refreshToken ->
                                        tokenGeneratorService.generate(tokenData, tokenConfig.getSecretKey())
                                                .flatMap(token -> {
                                                    TokenRegistry registry = buildTokenRegistry(tokenData, token, refreshToken);
                                                    return cacheService.put(email, registry, Duration.ofMillis(tokenConfig.getRefreshExpirationMs()))
                                                            .map(ignored -> registry);
                                                })
                                )
                );
    }

    private TokenData buildTokenData(com.jobby.authorization.domain.model.Employee employee) {
        return new TokenData(
                employee.getEmployeeId(),
                employee.getEmail(),
                tokenConfig.getEmployeeSub(),
                tokenConfig.getDefaultIss(),
                this.tokenConfig.getRefreshExpirationMs()
        );
    }

    private TokenRegistry buildTokenRegistry(TokenData tokenData, String token, String refreshToken) {
        Instant now = Instant.now();
        Date issuedAt = Date.from(now);
        Date refreshExpiry = Date.from(now.plusMillis(tokenConfig.getRefreshExpirationMs()));

        return new TokenRegistry(
                tokenData.getId(),
                token,
                refreshToken,
                issuedAt,
                refreshExpiry
        );
    }
}
