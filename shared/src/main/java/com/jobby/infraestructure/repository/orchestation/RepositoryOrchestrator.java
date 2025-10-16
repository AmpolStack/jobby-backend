package com.jobby.infraestructure.repository.orchestation;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface RepositoryOrchestrator<Infra,Domain> {
    Result<Domain, Error> selection(Supplier<Optional<Infra>> supplier);
    <T> Result<T, Error> operation(Supplier<T> supplier);
    <T> Result<T, Error> modification(Domain domain, Function<Infra, T> function);
    Result<Void, Error> exist(Supplier<Boolean> supplier, String name);
}