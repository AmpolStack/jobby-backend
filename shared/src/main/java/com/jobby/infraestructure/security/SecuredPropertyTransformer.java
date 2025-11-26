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

public class SecuredPropertyTransformer {
    private final EncryptionService encryptionService;
    private final EncryptConfig encryptConfig;
    private final MacService macService;
    private final MacConfig macConfig;
    private final SafeResultValidator validator;

    private Error error = null;

    private final List<SecuredProperty> properties = new ArrayList<>();

    public SecuredPropertyTransformer(EncryptionService encryptionService,
                                      MacService macService,
                                      EncryptConfig encryptConfig,
                                      MacConfig macConfig,
                                      SafeResultValidator validator) {
        this.encryptionService = encryptionService;
        this.macService = macService;
        this.encryptConfig = encryptConfig;
        this.macConfig = macConfig;
        this.validator = validator;
    }


    public SecuredPropertyTransformer addProperty(SecuredProperty property) {
        properties.add(property);
        return this;
    }

    public Result<Void, Error> apply(){
        var response = validateConfigs()
                .flatMap(v -> this.midStepApply());
        clear();
        return response;
    }

    public Result<Void, Error> revert(){
        var response = validateConfigs()
                .flatMap(v -> this.midStepRevert());
        clear();
        return response;
    }

    private Result<Void, Error> validateConfigs(){
        return this.validator.validate(this.encryptConfig)
                .flatMap(v -> this.validator.validate(this.macConfig));
    }

    private void clear(){
        this.properties.clear();
        this.error = null;
    }



    private Result<Void, Error> midStepApply(){
        if(properties.isEmpty()){
            return Result.success(null);
        }

        for(SecuredProperty property : properties){
            if(this.error != null){
                return Result.failure(this.error);
            }

            var raw = property.getRawValue();
            if(raw == null || raw.isBlank()){
                continue;
            }

            this.encryptionService.encrypt(raw, this.encryptConfig)
                    .flatMap(encryptedValue -> {
                        property.setEncryptedValue(encryptedValue);
                        return this.macService.generateMac(raw, this.macConfig);
                    })
                    .fold(
                            property::setHashedValue,
                            (onFailure) -> this.error = onFailure
                    );
        }

        return Result.success(null);
    }

    private Result<Void, Error> midStepRevert(){
        if(properties.isEmpty()){
            return Result.success(null);
        }

        for(SecuredProperty property : properties){
            if(this.error != null){
                return Result.failure(this.error);
            }

            var encrypted = property.getEncryptedValue();
            if(encrypted == null || encrypted.isBlank()){
                continue;
            }

            this.encryptionService.decrypt(encrypted, this.encryptConfig)
                    .fold(
                            property::setRawValue,
                            (onFailure) -> this.error = onFailure
                    );
        }

        return Result.success(null);
    }
}
