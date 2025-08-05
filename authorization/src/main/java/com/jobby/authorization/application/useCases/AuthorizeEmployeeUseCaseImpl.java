package com.jobby.authorization.application.useCases;

import com.jobby.authorization.domain.model.TokenRegistry;
import com.jobby.authorization.domain.ports.in.AuthorizeEmployeeUseCase;
import com.jobby.authorization.domain.ports.out.repositories.EmployeeRepository;
import com.jobby.authorization.domain.ports.out.repositories.TokenRegistryRepository;
import com.jobby.authorization.domain.ports.out.tokens.RefreshTokenGeneratorService;
import com.jobby.authorization.domain.ports.out.tokens.TokenGeneratorService;
import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.Result;
import com.jobby.authorization.domain.shared.TokenData;
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

    public AuthorizeEmployeeUseCaseImpl(EmployeeRepository employeeRepository, TokenGeneratorService tokenGeneratorService, RefreshTokenGeneratorService refreshTokenGeneratorService) {
        this.employeeRepository = employeeRepository;
        this.tokenGeneratorService = tokenGeneratorService;
        this.refreshTokenGeneratorService = refreshTokenGeneratorService;
    }

    public static String generateSecretKey256() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
            keyGen.init(256); // 256 bits
            SecretKey secretKey = keyGen.generateKey();

            // Codificar la clave en Base64 para poder usarla como string
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("Error generating secret key", e);
        }
    }

    @Override
    public Result<TokenRegistry, Error> byCredentials(String email, String password) {
        return this.employeeRepository
                .findByEmailAndPassword(email, password)
                .flatMap(employee -> {
                   var refreshToken = refreshTokenGeneratorService.generate();
                   var tokenConfig = new TokenData(
                           employee.getEmployeeId(),
                           employee.getEmail(),
                           "com.jobby.employee",
                           "com.jobby.authorization",
                           "3441131",
                           60000);
                   var key = generateSecretKey256();
                   return this.tokenGeneratorService.generate(tokenConfig, key)
                           .map(token ->
                               new TokenRegistry(employee.getEmployeeId(),
                                       token,
                                       refreshToken.getData(),
                                       new Date(),
                                       new Date(new Date().getTime() + tokenConfig.getMsExpirationTime()))
                           );
                });
    }

    @Override
    public Result<TokenRegistry, Error> byTokens(String token, String refreshToken) {
        return null;
    }
}
