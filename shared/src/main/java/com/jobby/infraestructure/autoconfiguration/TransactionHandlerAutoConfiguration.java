package com.jobby.infraestructure.autoconfiguration;

import com.jobby.infraestructure.repository.error.JpaErrorTransactionProxy;
import com.jobby.infraestructure.repository.error.TransactionalProxy;
import com.jobby.infraestructure.repository.transaction.PersistenceTransactionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionHandlerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public PersistenceTransactionHandler persistenceTransactionHandler() {
        return new PersistenceTransactionHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public TransactionalProxy defaultTransactionalProxy(){
        return new JpaErrorTransactionProxy();
    }
}
