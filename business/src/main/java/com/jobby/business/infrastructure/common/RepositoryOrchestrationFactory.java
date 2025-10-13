package com.jobby.business.infrastructure.common;

import com.jobby.business.infrastructure.common.repository.error.PersistenceErrorHandler;
import com.jobby.business.infrastructure.common.repository.pipeline.PipelinePersistenceProcess;

public class RepositoryOrchestrationFactory<Infra, Domain> {
    private PipelinePersistenceProcess<Infra, Domain> pipelinePersistenceProcess;
    private PersistenceErrorHandler persistenceErrorHandler;

    public RepositoryOrchestrationFactory<Infra, Domain> setPipelinePersistenceProcess
            (PipelinePersistenceProcess<Infra, Domain> pipelinePersistenceProcess) {
        this.pipelinePersistenceProcess = pipelinePersistenceProcess;
        return this;
    }

    public RepositoryOrchestrationFactory<Infra, Domain> setPersistenceErrorHandler
            (PersistenceErrorHandler persistenceErrorHandler) {
        this.persistenceErrorHandler = persistenceErrorHandler;
        return this;
    }

    public RepositoryOrchestration<Infra, Domain> build(){
        return new RepositoryOrchestration<>(
                this.pipelinePersistenceProcess,
                this.pipelinePersistenceProcess,
                this.persistenceErrorHandler);
    }

}
