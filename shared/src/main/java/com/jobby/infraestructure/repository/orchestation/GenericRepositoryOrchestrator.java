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
            PersistenceErrorHandler persistenceErrorHandler,
            PersistenceTransactionHandler persistenceTransactionHandler) {

        this.afterPersistProcess = afterPersistProcess;
        this.beforePersistProcess = beforePersistProcess;
        this.persistenceErrorHandler = persistenceErrorHandler;
        this.persistenceTransactionHandler = persistenceTransactionHandler;
    }

    @Override
    public Result<Domain, Error> onSelect(Supplier<Optional<Infra>> supplier){
        return this.persistenceTransactionHandler.executeInRead(() ->
                this.persistenceErrorHandler.handleReading(supplier)
                        .flatMap(this.afterPersistProcess::exist)
                        .flatMap(infra ->
                            this.afterPersistProcess.mutate(infra)
                                    .map(v -> this.afterPersistProcess.map(infra))
                        ));
    }

    @Override
    public Result<Infra, Error> onRawSelect(Supplier<Optional<Infra>> supplier){
        return this.persistenceTransactionHandler.executeInRead(() ->
                this.persistenceErrorHandler.handleReading(supplier)
                        .flatMap(this.afterPersistProcess::exist));
    }

    @Override
    public <T> Result<T, Error> onModify(Domain domain,
                                             Function<Infra, T> function){
        var mapped = this.beforePersistProcess.map(domain);
        return this.beforePersistProcess.mutate(mapped)
                .flatMap(v -> this.persistenceErrorHandler.handleWriting(function, mapped));
    }

    @Override
    public <T> Result<Domain, Error> onRawModify(Infra infra,
                                         Function<Infra, T> function){
        return this.persistenceErrorHandler.handleWriting(function, infra)
                .flatMap(v -> this.afterPersistProcess.mutate(infra))
                .map(v -> this.afterPersistProcess.map(infra));
    }

    @Override
    public <T> Result<T, Error> onOperation(Supplier<T> supplier) {
        return this.persistenceTransactionHandler.executeInRead(() ->
                this.persistenceErrorHandler.handleReading(supplier)
        );
    }
}
