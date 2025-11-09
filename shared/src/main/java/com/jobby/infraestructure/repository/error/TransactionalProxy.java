package com.jobby.infraestructure.repository.error;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import java.util.function.Supplier;

public interface TransactionalProxy {
    <T> Result<T, Error> handleWriting(Supplier<T> supplier);
    <T> Result<T, Error> handleReading(Supplier<T> supplier);
}
