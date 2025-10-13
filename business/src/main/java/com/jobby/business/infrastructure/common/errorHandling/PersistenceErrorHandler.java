package com.jobby.business.infrastructure.common.errorHandling;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface PersistenceErrorHandler {
    <Entity> Result<Optional<Entity>, Error> handleWriting(Function<Entity, Optional<Entity>> function, Entity entity);
    <Entity> Result<Optional<Entity>, Error> handleReading(Supplier<Optional<Entity>> supplier);
}
