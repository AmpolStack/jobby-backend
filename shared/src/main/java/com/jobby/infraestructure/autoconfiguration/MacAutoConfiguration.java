package com.jobby.infraestructure.autoconfiguration;

import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.domain.ports.MacService;
import com.jobby.infraestructure.adapter.encrypt.DefaultMacBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({MacService.class})
public class MacAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DefaultMacBuilder macBuilder() {
        return new DefaultMacBuilder();
    }

    @Bean
    @ConditionalOnMissingBean
    public MacService macService(
            SafeResultValidator safeResultValidator,
            DefaultMacBuilder macBuilder) {
        return new DefaultMacService(safeResultValidator, macBuilder);
    }
}
