package com.jobby.infraestructure.autoconfiguration;

import com.jobby.domain.ports.SafeResultValidator;
import com.jobby.domain.ports.hashing.mac.MacService;
import com.jobby.domain.ports.hashing.mac.MacBuilder;
import com.jobby.infraestructure.adapter.hashing.mac.DefaultMacBuilder;
import com.jobby.infraestructure.adapter.hashing.mac.HmacSha256Service;
import com.jobby.infraestructure.enrichment.mac.MacPropertyInitializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({MacService.class, MacPropertyInitializer.class})
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

