package com.jobby.infraestructure.autoconfiguration;

import com.jobby.domain.ports.CacheService;
import com.jobby.domain.ports.HashingService;
import com.jobby.infraestructure.adapter.BcryptHashingService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({HashingService.class})
public class HashingAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public HashingService hashingService() {
        return new BcryptHashingService();
    }
}
