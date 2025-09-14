package com.jobby.authorization.infraestructure.adapters.in.rest;

import com.jobby.authorization.domain.ports.in.AuthorizeEmployeeByCredentials;
import com.jobby.authorization.domain.ports.in.AuthorizeEmployeeByTokens;
import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.domain.ports.encrypt.EncryptionService;
import com.jobby.domain.configurations.EncryptConfig;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.mobility.validator.ValidationChain;
import com.jobby.infraestructure.response.definition.ApiResponseMapper;
import com.jobby.authorization.infraestructure.dto.mappers.TokenRegistryResponseMapper;
import com.jobby.authorization.infraestructure.dto.requests.LoginRequest;
import com.jobby.authorization.infraestructure.dto.requests.TokenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/authorize")
public class AuthorizationController {

    private final EncryptionService encryptionService;
    private final EncryptConfig encrypt;
    private final SafeResultValidator validator;
    private final TokenRegistryResponseMapper tokenRegistryMapper;
    private final AuthorizeEmployeeByCredentials authorizeByCredentialsUseCase;
    private final AuthorizeEmployeeByTokens authorizeByTokensUseCase;
    private final ApiResponseMapper apiResponseMapper;

    public AuthorizationController(
            EncryptionService encryptionService, EncryptConfig encryptConfig, SafeResultValidator validator,
            TokenRegistryResponseMapper responseMapper,
            AuthorizeEmployeeByCredentials authorizeEmployeeWithCredentialsUseCase,
            AuthorizeEmployeeByTokens authorizeByTokensUseCase, ApiResponseMapper apiResponseMapper) {
        this.encryptionService = encryptionService;
        this.encrypt = encryptConfig;
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

    @GetMapping("/testLength")
    public Result<Integer, Error> testLength(@RequestParam int repetitions) {
        int[] codePoints = new int[repetitions];
        Arrays.fill(codePoints, 0x20000); // fill with U+20000

        String input = new String(codePoints, 0, codePoints.length);

        int actualCodePoints = input.codePointCount(0, input.length());
        if (actualCodePoints != repetitions) {
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("Error in String building", "is invalid"));
        }

        var resp = this.encryptionService.encrypt(input, this.encrypt);
        var decrypt = this.encryptionService.decrypt(resp.getData(), this.encrypt);
        return resp.map(String::length);
    }


}
