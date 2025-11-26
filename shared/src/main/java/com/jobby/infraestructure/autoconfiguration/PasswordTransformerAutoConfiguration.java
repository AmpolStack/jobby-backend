package com.jobby.infraestructure.autoconfiguration;


import com.jobby.domain.ports.hashing.HashingService;
import com.jobby.infraestructure.security.SecuredPasswordTransformer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordTransformerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public SecuredPasswordTransformer securedPasswordTransformer(HashingService hashingService) {
        return new SecuredPasswordTransformer(hashingService);
    }
}
