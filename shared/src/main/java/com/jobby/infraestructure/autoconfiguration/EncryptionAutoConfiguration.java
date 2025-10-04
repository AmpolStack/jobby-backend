package com.jobby.infraestructure.autoconfiguration;

import com.jobby.domain.configurations.EncryptConfig;
import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.domain.ports.encrypt.EncryptBuilder;
import com.jobby.domain.ports.encrypt.EncryptionService;
import com.jobby.infraestructure.adapter.encrypt.AESEncryptionService;
import com.jobby.infraestructure.adapter.encrypt.DefaultEncryptBuilder;
import com.jobby.infraestructure.common.EncryptionPropertyInitializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({EncryptBuilder.class})
public class EncryptionAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EncryptBuilder encryptBuilder() {
        return new DefaultEncryptBuilder();
    }

    @Bean
    @ConditionalOnMissingBean
    public EncryptionService encryptionService(
            SafeResultValidator safeResultValidator,
            EncryptBuilder encryptBuilder) {
        return new AESEncryptionService(safeResultValidator, encryptBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    public EncryptionPropertyInitializer encryptionPropertyInitializer(
            EncryptConfig encryptConfig,
            EncryptionService encryptionService,
            SafeResultValidator safeResultValidator
    ){
        return new EncryptionPropertyInitializer(encryptConfig, encryptionService, safeResultValidator);
    }


}
