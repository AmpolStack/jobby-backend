package com.jobby.infraestructure.common.security.encryption;

import com.jobby.domain.configurations.EncryptConfig;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.domain.ports.encrypt.EncryptionService;
import com.jobby.infraestructure.common.security.EntityEnrichmentProcessor;

public class DecryptionPropertyInitializer extends EntityEnrichmentProcessor<Encrypted> {

    private final EncryptionService encryptionService;
    private final EncryptConfig encryptConfig;

    public DecryptionPropertyInitializer(EncryptConfig encryptConfig,
                                         EncryptionService encryptionService,
                                         SafeResultValidator safeResultValidator) {
        super(encryptConfig, Encrypted.class, safeResultValidator);
        this.encryptionService = encryptionService;
        this.encryptConfig = encryptConfig;
    }

    @Override
    protected Result<?, Error> enrichmentOperation(String inputExtracted) {
        return this.encryptionService.decrypt(inputExtracted, encryptConfig);
    }
}
