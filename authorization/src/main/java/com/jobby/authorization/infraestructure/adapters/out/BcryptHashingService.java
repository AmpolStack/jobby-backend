package com.jobby.authorization.infraestructure.adapters.out;

import com.jobby.authorization.domain.ports.out.HashingService;
import com.jobby.authorization.domain.shared.validators.NumberValidator;
import com.jobby.authorization.domain.shared.errors.Error;
import com.jobby.authorization.domain.shared.result.Result;
import com.jobby.authorization.domain.shared.validators.ValidationChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;

@Component
public class BcryptHashingService implements HashingService {

    private static final int VALID_LIMIT_OF_INPUT_BYTES = 72;

    @Override
    public Result<String, Error> hash(String input) {
        return ValidationChain.create()
                .validateInternalNotBlank(input, "hash-input")
                .validateInternalSmallerThan(
                        input.getBytes(StandardCharsets.UTF_8).length,
                        VALID_LIMIT_OF_INPUT_BYTES,
                        "hash-input-bytes")
                .build()
                .map(x -> {
                    var encoder = new BCryptPasswordEncoder();
                    return encoder.encode(input);
                });
    }

    @Override
    public Result<Boolean, Error> matches(String plain, String hash) {
        return ValidationChain.create()
                .validateInternalNotBlank(plain, "plain-input")
                .validateInternalNotBlank(hash, "hash-input")
                .build()
                .map(x -> {
                    var encoder = new BCryptPasswordEncoder();
                     return encoder.matches(plain, hash);
                });
    }

}
