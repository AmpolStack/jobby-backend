package com.jobby.employee.infraestructure.autoconfiguration;

import com.jobby.domain.configurations.EncryptConfig;
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
    public EncryptConfig encryptConfig() {
        return new EncryptConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "hashing.mac")
    public MacConfig macConfig() {
        return new MacConfig();
    }

    @Bean
    public ApiResponseMapper getApiResponseMapper() {
        return new ProblemDetailsResultMapper();
    }

}
