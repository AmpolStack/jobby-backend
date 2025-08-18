package com.jobby.authorization.infraestructure.config;

import com.jobby.authorization.infraestructure.response.definition.ApiResponseMapper;
import com.jobby.authorization.infraestructure.response.implementation.problem.ProblemDetailsResultMapper;
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
