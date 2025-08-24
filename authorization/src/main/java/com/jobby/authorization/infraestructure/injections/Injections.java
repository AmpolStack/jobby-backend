package com.jobby.authorization.infraestructure.config;

import com.jobby.infraestructure.response.definition.ApiResponseMapper;
import com.jobby.infraestructure.response.implementation.problemdetails.ProblemDetailsResultMapper;
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

    @Bean
    @ConfigurationProperties(prefix = "token")
    public TokenConfig getTokenConfig(){
        return new TokenConfig();
    }

    @Bean
    public ApiResponseMapper getRestResponseMapper(){
        return new ProblemDetailsResultMapper();
    }
}
