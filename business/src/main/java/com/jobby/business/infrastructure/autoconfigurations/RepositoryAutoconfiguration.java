package com.jobby.business.infrastructure.autoconfigurations;

import com.jobby.business.domain.entities.Business;
import com.jobby.infraestructure.repository.error.TransactionalProxy;
import com.jobby.infraestructure.repository.orchestation.RepositoryOrchestrationFactory;
import com.jobby.infraestructure.repository.orchestation.RepositoryOrchestrator;
import com.jobby.business.infrastructure.persistence.business.jpa.entities.JpaBusinessEntity;
import com.jobby.business.infrastructure.persistence.business.mongo.entities.MongoBusinessEntity;
import com.jobby.infraestructure.repository.pipeline.AfterPersistProcess;
import com.jobby.infraestructure.repository.pipeline.BeforePersistProcess;
import com.jobby.infraestructure.repository.transaction.PersistenceTransactionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryAutoconfiguration {

    @Bean
    public RepositoryOrchestrator<JpaBusinessEntity, Business> getJpaRepositoryOrchestration(
            AfterPersistProcess<JpaBusinessEntity, Business> afterPersistProcess,
            BeforePersistProcess<JpaBusinessEntity, Business> beforePersistProcess,
            @Qualifier("JpaErrorTransactionProxy") TransactionalProxy persistenceErrorHandler,
            PersistenceTransactionHandler persistenceTransactionHandler) {

        return new RepositoryOrchestrationFactory<JpaBusinessEntity, Business>()
                .setProcessPipeline(
                        afterPersistProcess,
                        beforePersistProcess
                )
                .setPersistenceErrorHandler(persistenceErrorHandler)
                .setPersistenceTransactionHandler(persistenceTransactionHandler)
                .build();
    }

    @Bean
    public RepositoryOrchestrator<MongoBusinessEntity, Business> getMongoRepositoryOrchestration(
            AfterPersistProcess<MongoBusinessEntity, Business> afterPersistProcess,
            BeforePersistProcess<MongoBusinessEntity, Business> beforePersistProcess,
            @Qualifier("MongoErrorTransactionProxy") TransactionalProxy persistenceErrorHandler,
            PersistenceTransactionHandler persistenceTransactionHandler) {

        return new RepositoryOrchestrationFactory<MongoBusinessEntity, Business>()
                .setProcessPipeline(
                        afterPersistProcess,
                        beforePersistProcess
                )
                .setPersistenceErrorHandler(persistenceErrorHandler)
                .setPersistenceTransactionHandler(persistenceTransactionHandler)
                .build();
    }
}
