package com.jobby.authorization.infraestructure.adapters.out;

import com.jobby.authorization.domain.ports.out.HashingService;
import com.jobby.authorization.domain.shared.validators.NumberValidator;
import com.jobby.authorization.domain.shared.validators.StringValidator;
import com.jobby.authorization.domain.shared.result.Error;
import com.jobby.authorization.domain.shared.result.Result;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;

@Component
public class BcryptHashingService implements HashingService {

    private static final int VALID_LIMIT_OF_INPUT_BYTES = 72;

    @Override
    public Result<String, Error> hash(String input) {
        return StringValidator.validateNotBlankString(input, "hash-input")
                .flatMap(x -> {
                    var bytes = input.getBytes(StandardCharsets.UTF_8);
                    return NumberValidator.validateSmallerNotEqualsInteger(bytes.length, VALID_LIMIT_OF_INPUT_BYTES, "hash-input-bytes");
                })
                .map(x -> {
                    var encoder = new BCryptPasswordEncoder();
                    return encoder.encode(input);
                });
    }

    @Override
    public Result<Boolean, Error> matches(String plain, String hash) {
        return StringValidator.validateNotBlankString(plain, "plain-input")
                .flatMap(x -> StringValidator.validateNotBlankString(hash, "hash-input"))
                .map(x -> {
                    var encoder = new BCryptPasswordEncoder();
                     return encoder.matches(plain, hash);
                });
    }

}
