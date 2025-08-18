package com.jobby.authorization.application.useCases;

import com.jobby.authorization.domain.model.Employee;
import com.jobby.authorization.domain.model.TokenRegistry;
import com.jobby.authorization.domain.ports.in.AuthorizeEmployeeByCredentials;
import com.jobby.authorization.domain.ports.out.SafeResultValidator;
import com.jobby.authorization.domain.ports.out.repositories.EmployeeRepository;
import com.jobby.authorization.domain.ports.out.repositories.TokenRegistryRepository;
import com.jobby.authorization.domain.ports.out.tokens.RefreshTokenGeneratorService;
import com.jobby.authorization.domain.ports.out.tokens.TokenGeneratorService;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.authorization.domain.shared.TokenData;
import com.jobby.authorization.infraestructure.config.TokenConfig;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;

@Service
public class AuthorizeEmployeeByCredentialsUseCase implements AuthorizeEmployeeByCredentials {

    private final EmployeeRepository employeeRepository;
    private final TokenRegistryRepository tokenRegistryRepository;
    private final TokenGeneratorService tokenGeneratorService;
    private final RefreshTokenGeneratorService refreshTokenGeneratorService;
    private final TokenConfig tokenConfig;
    private final SafeResultValidator validator;

    public AuthorizeEmployeeByCredentialsUseCase(
            EmployeeRepository employeeRepository, TokenRegistryRepository tokenRegistryRepository,
            TokenGeneratorService tokenGeneratorService,
            RefreshTokenGeneratorService refreshTokenGeneratorService,
            TokenConfig tokenConfig,
            SafeResultValidator validator) {
        this.employeeRepository = employeeRepository;
        this.tokenRegistryRepository = tokenRegistryRepository;
        this.tokenGeneratorService = tokenGeneratorService;
        this.refreshTokenGeneratorService = refreshTokenGeneratorService;
        this.tokenConfig = tokenConfig;
        this.validator = validator;
    }

    @Override
    public Result<TokenRegistry, Error> execute(String email, String password) {
        return this.validator.validate(this.tokenConfig)
                .flatMap(x -> employeeRepository.findByEmailAndPassword(email, password))
                .flatMap(this::processEmployeeAuthorization);
    }

    private Result<TokenRegistry, Error> processEmployeeAuthorization(Employee employee) {
        TokenData tokenData = buildTokenData(employee);
        
        return refreshTokenGeneratorService.generate()
                .flatMap(refreshToken -> generateAndSaveTokens(tokenData, refreshToken));
    }

    private Result<TokenRegistry, Error> generateAndSaveTokens(TokenData tokenData, String refreshToken) {
        return tokenGeneratorService.generate(tokenData, tokenConfig.getSecretKey())
                .flatMap(token -> {
                    TokenRegistry registry = buildTokenRegistry(tokenData, token, refreshToken);
                    return this.tokenRegistryRepository.saveTokenRegistry(registry)
                            .map(ignored -> registry);
                });
    }

    private TokenData buildTokenData(Employee employee) {
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
