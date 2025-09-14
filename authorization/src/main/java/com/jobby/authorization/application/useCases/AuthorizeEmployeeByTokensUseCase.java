package com.jobby.authorization.application.useCases;

import com.jobby.authorization.domain.model.Employee;
import com.jobby.authorization.domain.model.TokenRegistry;
import com.jobby.authorization.domain.ports.in.AuthorizeEmployeeByTokens;
import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.authorization.domain.ports.out.repositories.EmployeeRepository;
import com.jobby.authorization.domain.ports.out.repositories.TokenRegistryRepository;
import com.jobby.authorization.domain.ports.out.tokens.RefreshTokenGeneratorService;
import com.jobby.authorization.domain.ports.out.tokens.TokenGeneratorService;
import com.jobby.authorization.domain.shared.TokenData;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.authorization.infraestructure.config.TokenConfig;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;

@Service
public class AuthorizeEmployeeByTokensUseCase implements AuthorizeEmployeeByTokens {

    private final TokenRegistryRepository tokenRegistryRepository;
    private final EmployeeRepository employeeRepository;
    private final TokenConfig tokenConfig;
    private final SafeResultValidator validator;
    private final TokenGeneratorService tokenGeneratorService;
    private final RefreshTokenGeneratorService refreshTokenGeneratorService;

    public AuthorizeEmployeeByTokensUseCase(TokenRegistryRepository tokenRegistryRepository,
                                            EmployeeRepository employeeRepository,
                                            TokenConfig tokenConfig,
                                            SafeResultValidator validator,
                                            TokenGeneratorService tokenGeneratorService,
                                            RefreshTokenGeneratorService refreshTokenGeneratorService) {
        this.tokenRegistryRepository = tokenRegistryRepository;
        this.employeeRepository = employeeRepository;
        this.tokenConfig = tokenConfig;
        this.validator = validator;
        this.tokenGeneratorService = tokenGeneratorService;
        this.refreshTokenGeneratorService = refreshTokenGeneratorService;
    }

    @Override
    public Result<TokenRegistry, Error> execute(String token, String refreshToken, int id) {
        return this.validator.validate(this.tokenConfig)
                .flatMap(v -> this.tokenRegistryRepository.getTokenRegistry(token, refreshToken, id))
                .flatMap(tokenRegistry -> this.employeeRepository.findById(tokenRegistry.getId()))
                .map(this::buildTokenData)
                .flatMap(tokenData -> refreshTokenGeneratorService.generate()
                        .flatMap(generatedRefreshToken ->
                                tokenGeneratorService.generate(tokenData, tokenConfig.getSecretKey())
                                        .flatMap(generatedToken -> {
                                            TokenRegistry registry = buildTokenRegistry(tokenData, generatedToken, generatedRefreshToken);
                                            return this.tokenRegistryRepository.saveTokenRegistry(registry)
                                                    .map(v2 -> registry);
                                        })
                        ));
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


    private TokenData buildTokenData(Employee employee) {
        return new TokenData(
                employee.getEmployeeId(),
                employee.getEmail(),
                tokenConfig.getEmployeeSub(),
                tokenConfig.getDefaultIss(),
                this.tokenConfig.getRefreshExpirationMs()
        );
    }
}
