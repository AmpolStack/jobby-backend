package com.jobby.infraestructure.autoconfiguration;

import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.domain.ports.MacService;
import com.jobby.domain.ports.encrypt.MacBuilder;
import com.jobby.infraestructure.adapter.encrypt.DefaultMacBuilder;
import com.jobby.infraestructure.adapter.encrypt.HmacSha256Service;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({MacService.class})
public class MacAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MacBuilder macBuilder() {
        return new DefaultMacBuilder();
    }

    @Bean
    @ConditionalOnMissingBean
    public MacService macService(
            SafeResultValidator safeResultValidator,
            MacBuilder macBuilder) {
        return new HmacSha256Service(safeResultValidator, macBuilder);
    }
}
