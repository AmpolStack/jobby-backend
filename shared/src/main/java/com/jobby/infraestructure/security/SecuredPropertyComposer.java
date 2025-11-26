package com.jobby.infraestructure.security;

import com.jobby.domain.configurations.EncryptConfig;
import com.jobby.domain.configurations.MacConfig;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.domain.ports.encrypt.EncryptionService;
import com.jobby.domain.ports.hashing.mac.MacService;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class SecuredPropertyComposer {
    private final EncryptionService encryptionService;
    private final EncryptConfig encryptConfig;
    private final MacService macService;
    private final MacConfig macConfig;
    private final SafeResultValidator validator;

    private final List<Runnable> operations = new ArrayList<>();
    private Error error = null;

    public SecuredPropertyComposer(EncryptionService encryptionService, EncryptConfig encryptConfig, MacService macService, MacConfig macConfig, SafeResultValidator validator) {
        this.encryptionService = encryptionService;
        this.encryptConfig = encryptConfig;
        this.macService = macService;
        this.macConfig = macConfig;
        this.validator = validator;
    }


    public SecuredPropertyComposer apply(String property, BiConsumer<String, byte[]> results) {
        if(error != null || property == null || property.isBlank()) {
            return this;
        }

        var resp = this.encryptionService.encrypt(property, this.encryptConfig)
                .flatMap(cipher -> this.macService.generateMac(property, this.macConfig)
                .map(bytes ->
                    operations.add(() -> {
                        results.accept(cipher, bytes);
                    })));

        if(resp.isFailure()) {
            error = resp.getError();
        }

        return this;
    }

    public SecuredPropertyComposer revert(String property, Consumer<String> result) {
        if(error != null) {
            return this;
        }

        var resp = this.encryptionService.decrypt(property, this.encryptConfig)
                        .map(bytes ->
                                operations.add(() -> {
                                    result.accept(bytes);
                                }));

        if(resp.isFailure()) {
            error = resp.getError();
        }

        return this;
    }

    public Result<Void, Error> build() {
        if(error != null) {
            clear();
            return Result.failure(error);
        }

        operations.forEach(Runnable::run);
        clear();
        return Result.success(null);
    }


    private Result<Void, com.jobby.domain.mobility.error.Error> validateConfigs(){
        return this.validator.validate(this.encryptConfig)
                .flatMap(v -> this.validator.validate(this.macConfig));
    }

    private void clear(){
        this.operations.clear();
        this.error = null;
    }
}
