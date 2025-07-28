package com.jobby.authorization.infraestructure.adapters.out;

import com.jobby.authorization.application.ports.out.HashingService;
import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class BcryptHashingService implements HashingService {
    @Override
    public Result<String, Error> hash(String input) {
        var bytes = input.getBytes(StandardCharsets.UTF_8);
        if(bytes.length == 0 || bytes.length > 72) {
            Result.failure(
                    ErrorType.VALIDATION_ERROR,
                    new Field(
                            "input",
                            "the input are invalid, because exceeds the 72 bytes of length"
                    )
            );
        }
        var encoder = new BCryptPasswordEncoder();
        var resp = encoder.encode(input);
        return Result.success(resp);
    }

    @Override
    public Result<Boolean, Error> matches(String plain, String hash) {
        if(plain.isBlank()) {
            return Result.failure(
                    ErrorType.VALIDATION_ERROR,
                    new Field(
                            "plain",
                            "The input are null or blank"
                    )
            );
        }

        if(hash.isBlank()) {
            return Result.failure(
                    ErrorType.VALIDATION_ERROR,
                    new Field(
                            "hash",
                            "The input are null or blank"
                    )
            );
        }
        var encoder = new BCryptPasswordEncoder();
        var resp = encoder.matches(plain, hash);
        return Result.success(resp);
    }

}
