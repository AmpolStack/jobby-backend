package com.jobby.infraestructure.autoconfiguration;

import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.infraestructure.adapter.DefaultSafeResultValidator;
import jakarta.validation.Validator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({SafeResultValidator.class})
public class SafeValidatorAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    @ConditionalOnMissingBean
    public SafeResultValidator safeResultValidator(Validator validator) {
        return new DefaultSafeResultValidator(validator);
    }
}
