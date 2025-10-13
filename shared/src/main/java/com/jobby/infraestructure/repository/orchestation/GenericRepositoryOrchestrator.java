package com.jobby.infraestructure.repository.orchestation;

import com.jobby.infraestructure.repository.error.PersistenceErrorHandler;
import com.jobby.infraestructure.repository.pipeline.AfterPersistProcess;
import com.jobby.infraestructure.repository.pipeline.BeforePersistProcess;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public class GenericRepositoryOrchestrator<Infra, Domain>  implements RepositoryOrchestrator<Infra,Domain> {
    private final AfterPersistProcess<Infra, Domain> afterPersistProcess;
    private final BeforePersistProcess<Infra, Domain> beforePersistProcess;
    private final PersistenceErrorHandler persistenceErrorHandler;

    public GenericRepositoryOrchestrator(
            AfterPersistProcess<Infra, Domain> afterPersistProcess,
            BeforePersistProcess<Infra, Domain> beforePersistProcess,
            PersistenceErrorHandler persistenceErrorHandler) {

        this.afterPersistProcess = afterPersistProcess;
        this.beforePersistProcess = beforePersistProcess;
        this.persistenceErrorHandler = persistenceErrorHandler;
    }

    @Override
    public Result<Domain, Error> selection(Supplier<Optional<Infra>> supplier){
        return this.persistenceErrorHandler.handleReading(supplier)
                .flatMap(this.afterPersistProcess::after);
    }

    @Override
    public Result<Domain, Error> modification(Domain domain,
                                              Function<Infra, Optional<Infra>> function){
        return this.beforePersistProcess.before(domain)
                .flatMap(infra -> this.persistenceErrorHandler.handleWriting(function, infra))
                .flatMap(this.afterPersistProcess::after);
    }

}
