package com.jobby.authorization.infraestructure.adapters.in.rest;

import com.jobby.authorization.domain.model.TokenRegistry;
import com.jobby.authorization.domain.ports.in.AuthorizeEmployeeUseCase;
import com.jobby.authorization.domain.ports.out.SafeResultValidator;
import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.Result;
import com.jobby.authorization.infraestructure.requests.LoginRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authorize")
public class AuthController {

    private final SafeResultValidator validator;
    private final AuthorizeEmployeeUseCase authorizeEmployeeUseCase;

    public AuthController(SafeResultValidator validator, AuthorizeEmployeeUseCase authorizeEmployeeUseCase) {
        this.validator = validator;
        this.authorizeEmployeeUseCase = authorizeEmployeeUseCase;
    }

    @PostMapping("/withCredentials")
    public Result<TokenRegistry, Error> withCredentials(@RequestBody LoginRequest request) {
        return this.validator.validate(request)
                .flatMap(x -> this.authorizeEmployeeUseCase.byCredentials(request.getEmail(), request.getPassword()));
    }

}
