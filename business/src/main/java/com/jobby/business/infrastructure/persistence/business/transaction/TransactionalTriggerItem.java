package com.jobby.business.infrastructure.persistence.business.transaction;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class TransactionalTriggerItem<T,R> {
    @Getter
    private TransactionalTrigger<T,R> trigger;
    private final T input;

    public Result<Runnable, Error> pack(){
        return this.trigger.prepare(this.input)
                .map(r -> () -> this.trigger.send(r, this.input));
    }
}
