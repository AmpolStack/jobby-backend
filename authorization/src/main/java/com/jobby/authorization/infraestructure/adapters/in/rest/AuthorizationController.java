package com.jobby.authorization.infraestructure.adapters.in.rest;

import com.jobby.authorization.domain.ports.in.AuthorizeEmployeeByCredentials;
import com.jobby.authorization.domain.ports.in.AuthorizeEmployeeByTokens;
import com.jobby.authorization.domain.ports.out.SafeResultValidator;
import com.jobby.authorization.domain.shared.validators.ValidationChain;
import com.jobby.authorization.infraestructure.response.definition.ApiResponseMapper;
import com.jobby.authorization.infraestructure.dto.mappers.TokenRegistryResponseMapper;
import com.jobby.authorization.infraestructure.dto.requests.LoginRequest;
import com.jobby.authorization.infraestructure.dto.requests.TokenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authorize")
public class AuthorizationController {

    private final SafeResultValidator validator;
    private final TokenRegistryResponseMapper tokenRegistryMapper;
    private final AuthorizeEmployeeByCredentials authorizeByCredentialsUseCase;
    private final AuthorizeEmployeeByTokens authorizeByTokensUseCase;
    private final ApiResponseMapper apiResponseMapper;

    public AuthorizationController(
            SafeResultValidator validator,
            TokenRegistryResponseMapper responseMapper,
            AuthorizeEmployeeByCredentials authorizeEmployeeWithCredentialsUseCase,
            AuthorizeEmployeeByTokens authorizeByTokensUseCase, ApiResponseMapper apiResponseMapper) {
        this.validator = validator;
        this.tokenRegistryMapper = responseMapper;
        this.authorizeByCredentialsUseCase = authorizeEmployeeWithCredentialsUseCase;
        this.authorizeByTokensUseCase = authorizeByTokensUseCase;
        this.apiResponseMapper = apiResponseMapper;
    }

    @PostMapping("/byCredentials")
    public ResponseEntity<?> withCredentials(@RequestBody LoginRequest request) {
        var resp = this.validator.validate(request)
                .flatMap(v -> ValidationChain.create()
                        .validateEmail(request.getEmail(), "email")
                        .build())
                .flatMap(x -> this.authorizeByCredentialsUseCase
                        .execute(request.getEmail(), request.getPassword()))
                .map(this.tokenRegistryMapper::toDto);

        return this.apiResponseMapper.map(resp);
    }

    @PostMapping("/byTokens")
    public ResponseEntity<?> withTokens(@RequestBody TokenRequest request) {
        var resp = this.validator.validate(request)
                .flatMap(x -> this.authorizeByTokensUseCase.execute(request.getToken(), request.getRefreshToken(), request.getId()))
                .map(this.tokenRegistryMapper::toDto);

        return this.apiResponseMapper.map(resp);
    }


}
