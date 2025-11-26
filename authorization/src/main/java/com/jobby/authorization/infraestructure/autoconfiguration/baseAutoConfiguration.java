package com.jobby.authorization.infraestructure.autoconfiguration;

import com.jobby.domain.configurations.EncryptConfig;
import com.jobby.authorization.infraestructure.config.TokenConfig;
import com.jobby.domain.configurations.MacConfig;
import com.jobby.infraestructure.response.definition.ApiResponseMapper;
import com.jobby.infraestructure.response.implementation.problemdetails.ProblemDetailsResultMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class baseAutoConfiguration {
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
    @ConfigurationProperties(prefix = "mac")
    public MacConfig getMacConfig(){
        return new MacConfig();
    }

    @Bean
    public ApiResponseMapper getRestResponseMapper(){
        return new ProblemDetailsResultMapper();
    }
}
