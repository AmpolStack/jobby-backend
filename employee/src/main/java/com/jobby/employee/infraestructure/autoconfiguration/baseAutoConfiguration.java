package com.jobby.employee.infraestructure.autoconfiguration;

import com.jobby.infraestructure.response.definition.ApiResponseMapper;
import com.jobby.infraestructure.response.implementation.problemdetails.ProblemDetailsResultMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class baseAutoConfiguration {

    @Bean
    public ApiResponseMapper getApiResponseMapper() {
        return new ProblemDetailsResultMapper();
    }

}
