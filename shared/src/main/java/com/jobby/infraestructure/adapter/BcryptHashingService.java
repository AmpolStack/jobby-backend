package com.jobby.infraestructure.adapter;

import com.jobby.domain.ports.HashingService;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.mobility.validator.ValidationChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.nio.charset.StandardCharsets;

public class BcryptHashingService implements HashingService {

    private static final int VALID_LIMIT_OF_INPUT_BYTES = 72;


    @Override
    public Result<String, Error> hash(String input) {
        //noinspection DataFlowIssue
        return ValidationChain.create()
                .validateInternalNotBlank(input, "hash-input")
                .validateIf(input != null, () ->
                        ValidationChain.create()
                                .validateInternalSmallerThan(
                                        input.getBytes(StandardCharsets.UTF_8).length,
                                        VALID_LIMIT_OF_INPUT_BYTES,
                                        "hash-input-bytes")
                                .build())
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
