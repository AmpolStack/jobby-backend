package com.jobby.infraestructure.autoconfiguration;

import com.jobby.domain.configurations.EncryptConfig;
import com.jobby.domain.configurations.MacConfig;
import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.domain.ports.encrypt.EncryptionService;
import com.jobby.domain.ports.hashing.mac.MacService;
import com.jobby.infraestructure.security.SecuredPropertyTransformer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({EncryptConfig.class,
        EncryptionService.class,
        MacService.class,
        MacConfig.class,
        SafeResultValidator.class})
@ConditionalOnBean({EncryptConfig.class,
        EncryptionService.class,
        MacService.class,
        MacConfig.class,
        SafeResultValidator.class})
public class SecurityTransformerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public SecuredPropertyTransformer securedPropertyTransformer(
            EncryptionService encryptionService,
            EncryptConfig encryptConfig,
            MacService macService,
            MacConfig macConfig,
            SafeResultValidator safeResultValidator
    ){
        return new SecuredPropertyTransformer(encryptionService,
                macService,
                encryptConfig,
                macConfig,
                safeResultValidator);
    }
}
