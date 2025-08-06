package com.jobby.authorization.application.useCases;

import com.jobby.authorization.domain.model.TokenRegistry;
import com.jobby.authorization.domain.ports.in.AuthorizeEmployeeUseCase;
import com.jobby.authorization.domain.ports.out.repositories.EmployeeRepository;
import com.jobby.authorization.domain.ports.out.tokens.RefreshTokenGeneratorService;
import com.jobby.authorization.domain.ports.out.tokens.TokenGeneratorService;
import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.Result;
import com.jobby.authorization.domain.shared.TokenData;
import com.jobby.authorization.infraestructure.config.TokenConfig;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
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

    @Override
    public Result<TokenRegistry, Error> byCredentials(String email, String password) {
        return this.employeeRepository
                .findByEmailAndPassword(email, password)
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
