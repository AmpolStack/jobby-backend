package com.jobby.infraestructure.repository.orchestation;

import com.jobby.infraestructure.repository.error.PersistenceErrorHandler;
import com.jobby.infraestructure.repository.pipeline.PipelinePersistenceProcess;
import com.jobby.infraestructure.repository.transaction.PersistenceTransactionHandler;

public class RepositoryOrchestrationFactory<Infra, Domain> {
    private PipelinePersistenceProcess<Infra, Domain> pipelinePersistenceProcess;
    private PersistenceErrorHandler persistenceErrorHandler;
    private PersistenceTransactionHandler persistenceTransactionHandler;

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

    public RepositoryOrchestrationFactory<Infra, Domain> setPersistenceTransactionHandler
            (PersistenceTransactionHandler persistenceTransactionHandler) {
        this.persistenceTransactionHandler = persistenceTransactionHandler;
        return this;
    }

    public GenericRepositoryOrchestrator<Infra, Domain> build(){
        return new GenericRepositoryOrchestrator<>(
                this.pipelinePersistenceProcess,
                this.pipelinePersistenceProcess,
                this.persistenceErrorHandler,
                this.persistenceTransactionHandler);
    }

}
