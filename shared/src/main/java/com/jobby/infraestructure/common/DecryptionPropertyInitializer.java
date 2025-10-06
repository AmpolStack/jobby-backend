package com.jobby.infraestructure.common;

import com.jobby.domain.configurations.EncryptConfig;
import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.domain.ports.encrypt.EncryptionService;

public class DecryptionPropertyInitializer extends ReflexiveSetterProcessor<EncryptedProperty, EncryptConfig>{
    public DecryptionPropertyInitializer(EncryptConfig encryptConfig,
                                         EncryptionService encryptionService,
                                         SafeResultValidator safeResultValidator) {
        super(encryptConfig, safeResultValidator,
                (sourceValue) -> encryptionService.decrypt(sourceValue, encryptConfig),
                new com.jobby.domain.mobility.error.Field("EncryptionPropertyInitializer", "Invalid encryption configuration"),
                EncryptedProperty.class);
    }
}
