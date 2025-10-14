package com.jobby.infraestructure.repository.orchestation;

import com.jobby.infraestructure.repository.error.PersistenceErrorHandler;
import com.jobby.infraestructure.repository.pipeline.AfterPersistProcess;
import com.jobby.infraestructure.repository.pipeline.BeforePersistProcess;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.infraestructure.repository.transaction.PersistenceTransactionHandler;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class GenericRepositoryOrchestrator<Infra, Domain>  implements RepositoryOrchestrator<Infra,Domain> {
    private final AfterPersistProcess<Infra, Domain> afterPersistProcess;
    private final BeforePersistProcess<Infra, Domain> beforePersistProcess;
    private final PersistenceErrorHandler persistenceErrorHandler;
    private final PersistenceTransactionHandler persistenceTransactionHandler;

    public GenericRepositoryOrchestrator(
            AfterPersistProcess<Infra, Domain> afterPersistProcess,
            BeforePersistProcess<Infra, Domain> beforePersistProcess,
            PersistenceErrorHandler persistenceErrorHandler, PersistenceTransactionHandler persistenceTransactionHandler) {

        this.afterPersistProcess = afterPersistProcess;
        this.beforePersistProcess = beforePersistProcess;
        this.persistenceErrorHandler = persistenceErrorHandler;
        this.persistenceTransactionHandler = persistenceTransactionHandler;
    }

    @Override
    public Result<Domain, Error> selection(Supplier<Optional<Infra>> supplier){
        return this.persistenceTransactionHandler.executeInRead(() ->
                        this.persistenceErrorHandler.handleReading(supplier)
                                .flatMap(this.afterPersistProcess::after)
                );
    }

    @Override
    public <T> Result<T, Error> operation(Supplier<T> supplier) {
        return this.persistenceTransactionHandler.executeInRead(() ->
                this.persistenceErrorHandler.handleReading(supplier)
        );
    }

    @Override
    public Result<Integer, Error> modification(Domain domain,
                                              Function<Infra, Integer> function){
        return this.beforePersistProcess.before(domain)
                .flatMap(infra ->
                        this.persistenceErrorHandler.handleWriting(function, infra)
                );
    }
}
