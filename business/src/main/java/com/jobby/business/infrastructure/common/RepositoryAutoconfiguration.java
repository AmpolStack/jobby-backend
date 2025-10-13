package com.jobby.business.infrastructure.common;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.infrastructure.common.repository.error.PersistenceErrorHandler;
import com.jobby.business.infrastructure.common.repository.orchestation.RepositoryOrchestrationFactory;
import com.jobby.business.infrastructure.common.repository.orchestation.RepositoryOrchestrator;
import com.jobby.business.infrastructure.common.repository.pipeline.PipelinePersistenceProcess;
import com.jobby.business.infrastructure.persistence.jpa.entities.JpaBusinessEntity;
import com.jobby.business.infrastructure.persistence.mongo.entities.MongoBusinessEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryAutoconfiguration {

    @Bean
    public RepositoryOrchestrator<JpaBusinessEntity, Business> getJpaRepositoryOrchestration(
            PipelinePersistenceProcess<JpaBusinessEntity, Business> pipelinePersistenceProcess,
            @Qualifier("JpaPersistenceErrorHandler") PersistenceErrorHandler persistenceErrorHandler) {

        var factory = new RepositoryOrchestrationFactory<JpaBusinessEntity, Business>()
                .setPipelinePersistenceProcess(pipelinePersistenceProcess)
                .setPersistenceErrorHandler(persistenceErrorHandler);

        return factory.build();
    }

    @Bean
    public RepositoryOrchestrator<MongoBusinessEntity, Business> getMongoRepositoryOrchestration(
            PipelinePersistenceProcess<MongoBusinessEntity, Business> pipelinePersistenceProcess,
            @Qualifier("MongoPersistenceErrorHandler") PersistenceErrorHandler persistenceErrorHandler) {

        var factory = new RepositoryOrchestrationFactory<MongoBusinessEntity, Business>()
                .setPipelinePersistenceProcess(pipelinePersistenceProcess)
                .setPersistenceErrorHandler(persistenceErrorHandler);

        return factory.build();
    }
}
