package com.jobby.infraestructure.common;

import com.jobby.domain.configurations.EncryptConfig;
import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.domain.ports.encrypt.EncryptionService;

public class EncryptionPropertyInitializer extends ReflexiveSetterProcessor<EncryptedProperty, EncryptConfig> {

    public EncryptionPropertyInitializer(EncryptConfig encryptConfig,
                                         EncryptionService encryptionService,
                                         SafeResultValidator safeResultValidator) {
        super(encryptConfig, safeResultValidator,
                (sourceValue) -> encryptionService.encrypt(sourceValue, encryptConfig),
                new com.jobby.domain.mobility.error.Field("EncryptionPropertyInitializer", "Invalid encryption configuration"),
                EncryptedProperty.class);
    }

}
