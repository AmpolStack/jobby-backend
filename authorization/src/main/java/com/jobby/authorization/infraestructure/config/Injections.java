package com.jobby.authorization.infraestructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Injections {
    @Bean
    @ConfigurationProperties(prefix = "encrypt")
    public EncryptConfig getEncryptConfig(){
        return new EncryptConfig();
    }
}
