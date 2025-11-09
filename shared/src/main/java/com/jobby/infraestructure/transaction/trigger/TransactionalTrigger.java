package com.jobby.infraestructure.transaction.trigger;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface TransactionalTrigger<T, R> {
    Result<R, Error> prepare(T input);
    void send(R input1, T input2);
}
