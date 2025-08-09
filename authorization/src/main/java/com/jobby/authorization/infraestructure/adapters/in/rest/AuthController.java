package com.jobby.authorization.infraestructure.adapters.in.rest;

import com.jobby.authorization.domain.model.TokenRegistry;
import com.jobby.authorization.domain.ports.in.AuthorizeEmployeeUseCase;
import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;
import com.jobby.authorization.infraestructure.requests.LoginRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;

@RestController
@RequestMapping("/authorize")
public class AuthController {

    private final Validator validator;
    private final AuthorizeEmployeeUseCase authorizeEmployeeUseCase;

    public AuthController(Validator validator, AuthorizeEmployeeUseCase authorizeEmployeeUseCase) {
        this.validator = validator;
        this.authorizeEmployeeUseCase = authorizeEmployeeUseCase;
    }

    public Result<Void, Error> validateLoginRequest(LoginRequest entity) {
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(entity);

        if (!violations.isEmpty()) {
            Field[] fields = violations.stream()
                    .map(v -> new Field(v.getPropertyPath().toString(), v.getMessage()))
                    .toArray(Field[]::new);
            return Result.failure(ErrorType.VALIDATION_ERROR, fields);
        }

        return Result.success(null);
    }

    @PostMapping("/withCredentials")
    public Result<TokenRegistry, Error> withCredentials(@RequestBody LoginRequest request) {
        return validateLoginRequest(request)
                .flatMap(x -> this.authorizeEmployeeUseCase.byCredentials(request.getEmail(), request.getPassword()));
    }

}
