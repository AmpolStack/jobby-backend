package com.jobby.infraestructure.security;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.mobility.validator.ValidationChain;
import com.jobby.domain.ports.hashing.HashingService;

public class SecuredPasswordTransformer {
    private final HashingService hashingService;

    public SecuredPasswordTransformer(HashingService hashingService) {
        this.hashingService = hashingService;
    }

    public Result<Void, Error> apply(SecuredPassword password){
        return ValidationChain.create()
                .validateNotNull(password, "password")
                .build()
                .flatMap(v -> ValidationChain.create()
                        .validateNotBlank(password.getRawValue(), "password")
                        .build())
                .flatMap(v -> hashingService.hash(password.getRawValue()))
                .map(hash ->{
                    password.setHashedValue(hash);
                    return null;
                });
    }

    public Result<Boolean, Error> matches(SecuredPassword password, String comparable){
        return ValidationChain.create()
                .validateNotNull(password, "password")
                .build()
                .flatMap(v -> ValidationChain.create()
                        .validateNotBlank(password.getHashedValue(), "database password")
                        .validateNotBlank(comparable, "password")
                        .build())
                .flatMap(v -> hashingService.matches(password.getHashedValue(), comparable));
    }
}
