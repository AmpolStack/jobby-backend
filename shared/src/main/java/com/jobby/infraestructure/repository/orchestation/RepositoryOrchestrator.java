package com.jobby.infraestructure.repository.orchestation;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface RepositoryOrchestrator<Infra,Domain> {
    Result<Domain, com.jobby.domain.mobility.error.Error> selection(Supplier<Optional<Infra>> supplier);
    <T> Result<T, com.jobby.domain.mobility.error.Error> operation(Supplier<T> supplier);
    Result<Integer, Error> modification(Domain domain, Function<Infra, Integer> function);
}
