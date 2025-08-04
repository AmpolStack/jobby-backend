package com.jobby.authorization.application.useCases;

import com.jobby.authorization.domain.model.TokenRegistry;
import com.jobby.authorization.domain.ports.in.AuthorizeEmployeeUseCase;
import com.jobby.authorization.domain.ports.out.repositories.EmployeeRepository;
import com.jobby.authorization.domain.ports.out.repositories.TokenRegistryRepository;
import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.Result;
import org.springframework.stereotype.Service;

@Service
public class AuthorizeEmployeeUseCaseImpl implements AuthorizeEmployeeUseCase {

    private final EmployeeRepository employeeRepository;
    private final TokenRegistryRepository tokenRegistryRepository;

    public AuthorizeEmployeeUseCaseImpl(EmployeeRepository employeeRepository, TokenRegistryRepository tokenRegistryRepository) {
        this.employeeRepository = employeeRepository;
        this.tokenRegistryRepository = tokenRegistryRepository;
    }

    @Override
    public Result<TokenRegistry, Error> byCredentials(String email, String password) {
        return null;
    }

    @Override
    public Result<TokenRegistry, Error> byTokens(String token, String refreshToken) {
        return null;
    }
}
