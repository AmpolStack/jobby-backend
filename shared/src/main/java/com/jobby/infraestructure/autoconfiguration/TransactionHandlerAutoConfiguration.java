package com.jobby.infraestructure.autoconfiguration;

import com.jobby.infraestructure.repository.error.JpaErrorTransactionProxy;
import com.jobby.infraestructure.repository.error.TransactionalProxy;
import com.jobby.infraestructure.repository.transaction.PersistenceTransactionalContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionHandlerAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public PersistenceTransactionalContext persistenceTransactionHandler() {
        return new PersistenceTransactionalContext();
    }

    @Bean
    @ConditionalOnMissingBean
    public TransactionalProxy defaultTransactionalProxy(){
        return new JpaErrorTransactionProxy();
    }
}
