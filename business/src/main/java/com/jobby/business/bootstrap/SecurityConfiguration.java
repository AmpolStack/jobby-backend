package com.jobby.business.bootstrap;

import com.jobby.domain.configurations.EncryptConfig;
import com.jobby.domain.configurations.MacConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "app.security.encryption")
    public EncryptConfig encryptConfig() {
        return new EncryptConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "app.security.hashing.mac")
    public MacConfig macConfig() {
        return new MacConfig();
    }
}
