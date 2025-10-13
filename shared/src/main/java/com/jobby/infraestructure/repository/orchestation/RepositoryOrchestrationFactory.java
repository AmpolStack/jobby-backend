package com.jobby.infraestructure.repository.orchestation;

import com.jobby.infraestructure.repository.error.PersistenceErrorHandler;
import com.jobby.infraestructure.repository.pipeline.PipelinePersistenceProcess;

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

    public GenericRepositoryOrchestrator<Infra, Domain> build(){
        return new GenericRepositoryOrchestrator<>(
                this.pipelinePersistenceProcess,
                this.pipelinePersistenceProcess,
                this.persistenceErrorHandler);
    }

}
