package com.jobby.business.infrastructure.common;

import com.jobby.business.infrastructure.common.errorHandling.PersistenceErrorHandler;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class RepositoryOrchestration<Infra, Domain> {
    private final AfterPersistProcess<Infra, Domain> afterPersistProcess;
    private final BeforePersistProcess<Infra, Domain> beforePersistProcess;
    private final PersistenceErrorHandler persistenceErrorHandler;

    public RepositoryOrchestration(
            AfterPersistProcess<Infra, Domain> afterPersistProcess,
            BeforePersistProcess<Infra, Domain> beforePersistProcess,
            PersistenceErrorHandler persistenceErrorHandler) {

        this.afterPersistProcess = afterPersistProcess;
        this.beforePersistProcess = beforePersistProcess;
        this.persistenceErrorHandler = persistenceErrorHandler;
    }

    public Result<Domain, Error> selection(Supplier<Optional<Infra>> supplier){
        return this.persistenceErrorHandler.handleReading(supplier)
                .flatMap(this.afterPersistProcess::use);
    }

    public Result<Domain, Error> modification(Domain domain,
                                              Function<Infra, Optional<Infra>> function){
        return this.beforePersistProcess.use(domain)
                .flatMap(infra -> this.persistenceErrorHandler.handleWriting(function, infra))
                .flatMap(this.afterPersistProcess::use);
    }

}
