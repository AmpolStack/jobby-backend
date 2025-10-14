package com.jobby.infraestructure.autoconfiguration;

import com.jobby.domain.configurations.EncryptConfig;
import com.jobby.domain.configurations.MacConfig;
import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.domain.ports.encrypt.EncryptionService;
import com.jobby.domain.ports.hashing.HashingService;
import com.jobby.domain.ports.hashing.mac.MacService;
import com.jobby.infraestructure.common.security.encryption.DecryptionPropertyInitializer;
import com.jobby.infraestructure.common.security.encryption.EncryptionPropertyInitializer;
import com.jobby.infraestructure.common.security.hashing.HashedPropertyInitializer;
import com.jobby.infraestructure.common.security.mac.MacPropertyInitializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitializersAutoConfiguration {
    @Bean()
    @ConditionalOnMissingBean
    public EncryptionPropertyInitializer encryptionPropertyInitializer(
            EncryptConfig encryptConfig,
            EncryptionService encryptionService,
            SafeResultValidator safeResultValidator
    ){
        return new EncryptionPropertyInitializer(encryptConfig, encryptionService, safeResultValidator);
    }

    @Bean
    @ConditionalOnMissingBean
    public DecryptionPropertyInitializer decryptionPropertyInitializer(
            EncryptConfig encryptConfig,
            EncryptionService encryptionService,
            SafeResultValidator safeResultValidator
    ){
        return new DecryptionPropertyInitializer(encryptConfig, encryptionService, safeResultValidator);
    }

    @Bean
    @ConditionalOnMissingBean
    public MacPropertyInitializer macPropertyInitializer(
            MacService macService,
            MacConfig macConfig,
            SafeResultValidator safeResultValidator) {
        return new MacPropertyInitializer(macService, macConfig, safeResultValidator);
    }


    @Bean
    @ConditionalOnMissingBean
    public HashedPropertyInitializer hashedPropertyInitializer(
            HashingService hashingService) {
        return new HashedPropertyInitializer(hashingService);
    }


}
