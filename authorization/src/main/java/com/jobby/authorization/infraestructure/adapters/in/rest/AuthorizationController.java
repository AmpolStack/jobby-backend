package com.jobby.authorization.infraestructure.adapters.in.rest;

import com.jobby.authorization.domain.ports.in.AuthorizeEmployeeWithCredentialsUseCase;
import com.jobby.authorization.domain.ports.out.SafeResultValidator;
import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.Result;
import com.jobby.authorization.infraestructure.dto.mappers.TokenRegistryResponseMapper;
import com.jobby.authorization.infraestructure.dto.requests.LoginRequest;
import com.jobby.authorization.infraestructure.dto.responses.TokenRegistryResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authorize")
public class AuthorizationController {

    private final SafeResultValidator validator;
    private final TokenRegistryResponseMapper responseMapper;
    private final AuthorizeEmployeeWithCredentialsUseCase authorizeEmployeeWithCredentialsUseCase;

    public AuthorizationController(SafeResultValidator validator, TokenRegistryResponseMapper responseMapper, AuthorizeEmployeeWithCredentialsUseCase authorizeEmployeeWithCredentialsUseCase) {
        this.validator = validator;
        this.responseMapper = responseMapper;
        this.authorizeEmployeeWithCredentialsUseCase = authorizeEmployeeWithCredentialsUseCase;
    }

    @PostMapping("/withCredentials")
    public Result<TokenRegistryResponse, Error> withCredentials(@RequestBody LoginRequest request) {
        return this.validator.validate(request)
                .flatMap(x -> this.authorizeEmployeeWithCredentialsUseCase
                        .byCredentials(request.getEmail(), request.getPassword()))
                .map(this.responseMapper::toDto);
    }


}
