package com.jobby.infraestructure.repository.error;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import java.util.function.Function;
import java.util.function.Supplier;

public interface PersistenceErrorHandler {
    <Entity, R> Result<R, Error> handleWriting(Function<Entity, R> function, Entity entity);
    <Entity> Result<Entity, Error> handleReading(Supplier<Entity> supplier);
}
