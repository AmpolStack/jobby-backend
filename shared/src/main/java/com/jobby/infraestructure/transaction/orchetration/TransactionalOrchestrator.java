package com.jobby.infraestructure.transaction.orchetration;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.infraestructure.transaction.context.PersistenceTransactionalContext;
import com.jobby.infraestructure.transaction.trigger.TransactionalTriggerComposer;
import com.jobby.infraestructure.transaction.proxy.TransactionalProxy;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Supplier;

@Component
public class TransactionalOrchestrator {
    private final Queue<Runnable> triggers = new ArrayDeque<>();
    private Result<Runnable, Error> errorResult = null;

    private final PersistenceTransactionalContext transactionHandler;
    private final TransactionalProxy transactionalProxy;

    public TransactionalOrchestrator(PersistenceTransactionalContext transactionHandler, TransactionalProxy transactionalProxy) {
        this.transactionHandler = transactionHandler;
        this.transactionalProxy = transactionalProxy;
    }

    public TransactionalTriggerComposer triggers(){
        return new TransactionalTriggerComposer(this);
    }


    public void addTrigger(Result<Runnable, Error> service){

        if(errorResult != null){
            return;
        }

        service.fold(
                (onSuccess) -> this.triggers.add(service.getData()),
                (onFailure) -> this.errorResult = service
        );

    }

    public TransactionalOrchestrator addTrigger(Runnable runnable){
        if(errorResult != null){
            return this;
        }

        this.triggers.add(runnable);
        return this;
    }

    public <T> Result<T,Error> write(Supplier<T> supplier){
        var resp = internalStepWrite(supplier);
        clear();
        return resp;
    }

    public Result<Void,Error> write(Runnable runnable){
        Result<Void,Error> resp = internalStepWrite(()-> {runnable.run(); return null;});
        clear();
        return resp;
    }

    public Result<Void,Error> read(Runnable runnable){
        Result<Void,Error> resp = internalStepRead(()-> {runnable.run(); return null;});
        clear();
        return resp;
    }


    public <T> Result<T,Error> read(Supplier<T> supplier){
        var resp = internalStepRead(supplier);
        clear();
        return resp;
    }

    private <T> Result<T,Error> internalStepWrite(Supplier<T> supplier){
        if(errorResult != null){
            return Result.renewFailure(errorResult);
        }

        return this.transactionHandler.execute(()->
                this.transactionalProxy.handleWriting(() -> {
                    var x = supplier.get();
                    executeTriggers(this.triggers);
                    return x;
                })
        );
    }

    private <T> Result<T,Error> internalStepRead(Supplier<T> supplier){
        if(errorResult != null){
            return Result.renewFailure(errorResult);
        }

        return this.transactionHandler.executeInRead(()->
                this.transactionalProxy.handleReading(()->{
                    var x = supplier.get();
                    executeTriggers(this.triggers);
                    return x;
                })
        );
    }

    private void executeTriggers(Queue<Runnable> triggers){
        for(var trigger : triggers){
            if(trigger == null){
                continue;
            }

            trigger.run();
        }
    }

    private void clear(){
        this.triggers.clear();
        this.errorResult = null;
    }

}
