package com.jobby.infraestructure.autoconfiguration;

import com.jobby.infraestructure.transaction.context.PersistenceTransactionalContext;
import com.jobby.infraestructure.transaction.orchetration.TransactionalOrchestrator;
import com.jobby.infraestructure.transaction.proxy.JpaErrorTransactionProxy;
import com.jobby.infraestructure.transaction.proxy.TransactionalProxy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionalOrchestratorAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public PersistenceTransactionalContext persistenceTransactionContext() {
        return new PersistenceTransactionalContext();
    }

    @Bean
    @ConditionalOnMissingBean
    public TransactionalProxy defaultTransactionalProxy(){
        return new JpaErrorTransactionProxy();
    }

    @Bean
    @ConditionalOnMissingBean
    public TransactionalOrchestrator transactionalOrchestrator(
            PersistenceTransactionalContext context,
            TransactionalProxy proxy
    ) {
        return new TransactionalOrchestrator(context,proxy);
    }
}
