package com.jobby.infraestructure.repository.orchestation;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface RepositoryOrchestrator<Infra,Domain> {
    Result<Domain, Error> onSelect(Supplier<Optional<Infra>> supplier);
    <T> Result<T, Error> onModify(Domain domain, Function<Infra, T> function);
    <T> Result<T, Error> onModify(Supplier<T> supplier);
    <T> Result<T, Error> onOperation(Supplier<T> supplier);
}