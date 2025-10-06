package com.jobby.infraestructure.autoconfiguration;

import com.jobby.infraestructure.common.transaction.TransactionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionHandlerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public TransactionHandler transactionHandler() {
        return new TransactionHandler();
    }
}
