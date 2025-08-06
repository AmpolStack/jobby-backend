package com.jobby.authorization.application.useCases;

import com.jobby.authorization.domain.model.TokenRegistry;
import com.jobby.authorization.domain.ports.in.AuthorizeEmployeeUseCase;
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
import java.util.Date;

@Service
public class AuthorizeEmployeeUseCaseImpl implements AuthorizeEmployeeUseCase {

    private final EmployeeRepository employeeRepository;
    private final TokenGeneratorService tokenGeneratorService;
    private final RefreshTokenGeneratorService refreshTokenGeneratorService;
    private final TokenConfig tokenConfig;

    public AuthorizeEmployeeUseCaseImpl(EmployeeRepository employeeRepository, TokenGeneratorService tokenGeneratorService, RefreshTokenGeneratorService refreshTokenGeneratorService, TokenConfig tokenConfig) {
        this.employeeRepository = employeeRepository;
        this.tokenGeneratorService = tokenGeneratorService;
        this.refreshTokenGeneratorService = refreshTokenGeneratorService;
        this.tokenConfig = tokenConfig;
    }

    private Result<Void, Error> validateTokenConfig(TokenConfig tokenConfig) {
        if(tokenConfig == null){
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field("tokenConfig", "is required"));
        }

        if(tokenConfig.getExpirationMs() <= 0){
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field("tokenConfig.expirationMs", "is must be at least 0"));
        }

        if(tokenConfig.getRefreshExpirationMs() <= 0){
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field("tokenConfig.refresh-expiration-ms", "is must be at least 0"));
        }

        if(tokenConfig.getSecretKey() == null || tokenConfig.getSecretKey().isBlank()){
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field("tokenConfig.secret-key", "the secret key is required or blank"));
        }
        return Result.success(null);
    }

    @Override
    public Result<TokenRegistry, Error> byCredentials(String email, String password) {
        return validateTokenConfig(this.tokenConfig)
                .flatMap(x -> this.employeeRepository.findByEmailAndPassword(email, password))
                .map(employee -> new TokenData(
                        employee.getEmployeeId(),
                        employee.getEmail(),
                        "com.jobby.employee",
                        "com.jobby.authorization",
                        "3441131",
                        this.tokenConfig.getRefreshExpirationMs()))
                .flatMap(tokenData ->
                   refreshTokenGeneratorService.generate()
                           .flatMap(refreshToken ->
                              this.tokenGeneratorService.generate(tokenData, tokenConfig.getSecretKey())
                                      .map(token -> new TokenRegistry(
                                              tokenData.getId(),
                                              token,
                                              refreshToken,
                                              new Date(),
                                              new Date(new Date().getTime() + tokenConfig.getRefreshExpirationMs())
                                      ))
                           )
                );
    }

    @Override
    public Result<TokenRegistry, Error> byTokens(String token, String refreshToken) {
        return null;
    }
}
