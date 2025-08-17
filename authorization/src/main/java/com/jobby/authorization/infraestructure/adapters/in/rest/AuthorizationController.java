package com.jobby.authorization.infraestructure.adapters.in.rest;

import com.jobby.authorization.domain.ports.in.AuthorizeEmployeeByCredentials;
import com.jobby.authorization.domain.ports.in.AuthorizeEmployeeByTokens;
import com.jobby.authorization.domain.ports.out.SafeResultValidator;
import com.jobby.authorization.domain.shared.errors.Error;
import com.jobby.authorization.domain.shared.result.Result;
import com.jobby.authorization.infraestructure.dto.mappers.TokenRegistryResponseMapper;
import com.jobby.authorization.infraestructure.dto.requests.LoginRequest;
import com.jobby.authorization.infraestructure.dto.requests.TokenRequest;
import com.jobby.authorization.infraestructure.dto.responses.TokenRegistryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authorize")
public class AuthorizationController {

    private final SafeResultValidator validator;
    private final TokenRegistryResponseMapper responseMapper;
    private final AuthorizeEmployeeByCredentials authorizeByCredentialsUseCase;
    private final AuthorizeEmployeeByTokens authorizeByTokensUseCase;

    public AuthorizationController(
            SafeResultValidator validator,
            TokenRegistryResponseMapper responseMapper,
            AuthorizeEmployeeByCredentials authorizeEmployeeWithCredentialsUseCase,
            AuthorizeEmployeeByTokens authorizeByTokensUseCase) {
        this.validator = validator;
        this.responseMapper = responseMapper;
        this.authorizeByCredentialsUseCase = authorizeEmployeeWithCredentialsUseCase;
        this.authorizeByTokensUseCase = authorizeByTokensUseCase;
    }

    @PostMapping("/byCredentials")
    public ResponseEntity<Result<TokenRegistryResponse, Error>> withCredentials(@RequestBody LoginRequest request) {
        var resp = this.validator.validate(request)
                .flatMap(x -> this.authorizeByCredentialsUseCase
                        .execute(request.getEmail(), request.getPassword()))
                .map(this.responseMapper::toDto);

        if(resp.isSuccess()) {
            return ResponseEntity.ok(resp);
        }

        return ResponseEntity.badRequest().body(resp);
    }

    @PostMapping("/byTokens")
    public ResponseEntity<Result<TokenRegistryResponse, Error>> withTokens(@RequestBody TokenRequest request) {
        var resp = this.validator.validate(request)
                .flatMap(x -> this.authorizeByTokensUseCase.execute(request.getToken(), request.getRefreshToken(), request.getId()))
                .map(this.responseMapper::toDto);

        if(resp.isSuccess()) {
            return ResponseEntity.ok(resp);
        }

        return ResponseEntity.badRequest().body(resp);
    }


}
