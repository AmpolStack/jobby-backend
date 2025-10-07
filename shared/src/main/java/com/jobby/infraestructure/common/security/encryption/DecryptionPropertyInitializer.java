package com.jobby.infraestructure.common.security.encryption;

import com.jobby.domain.configurations.EncryptConfig;
import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.domain.ports.encrypt.EncryptionService;
import com.jobby.infraestructure.common.security.EntityEnrichmentProcessor;

public class DecryptionPropertyInitializer extends EntityEnrichmentProcessor<Encrypted, EncryptConfig> {
    public DecryptionPropertyInitializer(EncryptConfig encryptConfig,
                                         EncryptionService encryptionService,
                                         SafeResultValidator safeResultValidator) {
        super(encryptConfig, safeResultValidator,
                (sourceValue) -> encryptionService.decrypt(sourceValue, encryptConfig),
                new com.jobby.domain.mobility.error.Field("EncryptionPropertyInitializer", "Invalid encryption configuration"),
                Encrypted.class);
    }
}
