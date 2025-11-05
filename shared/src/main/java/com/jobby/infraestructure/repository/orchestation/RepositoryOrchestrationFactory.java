package com.jobby.infraestructure.repository.orchestation;

import com.jobby.infraestructure.repository.error.TransactionalPipeline;
import com.jobby.infraestructure.repository.pipeline.AfterPersistProcess;
import com.jobby.infraestructure.repository.pipeline.BeforePersistProcess;
import com.jobby.infraestructure.repository.transaction.PersistenceTransactionHandler;

public class RepositoryOrchestrationFactory<Infra, Domain> {
    private AfterPersistProcess<Infra, Domain> afterPersistProcess;
    private BeforePersistProcess<Infra, Domain> beforePersistProcess;
    private TransactionalPipeline persistenceErrorHandler;
    private PersistenceTransactionHandler persistenceTransactionHandler;

    public RepositoryOrchestrationFactory<Infra, Domain> setProcessPipeline(
            AfterPersistProcess<Infra, Domain> after,
            BeforePersistProcess<Infra, Domain> before){
        this.afterPersistProcess = after;
        this.beforePersistProcess = before;
        return this;
    }

    public RepositoryOrchestrationFactory<Infra, Domain> setPersistenceErrorHandler
            (TransactionalPipeline persistenceErrorHandler) {
        this.persistenceErrorHandler = persistenceErrorHandler;
        return this;
    }

    public RepositoryOrchestrationFactory<Infra, Domain> setPersistenceTransactionHandler
            (PersistenceTransactionHandler persistenceTransactionHandler) {
        this.persistenceTransactionHandler = persistenceTransactionHandler;
        return this;
    }

    public RepositoryOrchestratorImpl<Infra, Domain> build(){
        return new RepositoryOrchestratorImpl<>(
                this.afterPersistProcess,
                this.beforePersistProcess,
                this.persistenceErrorHandler,
                this.persistenceTransactionHandler);
    }

}
