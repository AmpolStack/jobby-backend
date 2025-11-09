package com.jobby.infraestructure.transaction.trigger;

import com.jobby.infraestructure.transaction.orchetration.TransactionalOrchestrator;

import java.util.ArrayDeque;
import java.util.Queue;

public class TransactionalTriggerComposer {
    private final Queue<TransactionalTriggerItem<?,?>> items = new ArrayDeque<>();
    private final TransactionalOrchestrator orchestrator;

    public TransactionalTriggerComposer(TransactionalOrchestrator orchestrator){
        this.orchestrator = orchestrator;
    }

    public <T,R> TransactionalTriggerComposer add(TransactionalTrigger<T,R> trigger, T input){
        var item = new TransactionalTriggerItem<>(trigger, input);
        items.offer(item);
        return this;
    }

    public <T,R> TransactionalTriggerComposer add(Runnable r){
        this.orchestrator.addTrigger(r);
        return this;
    }

    public TransactionalOrchestrator build(){
        for (TransactionalTriggerItem<?,?> item : items) {
            var packaging = item.pack();
            this.orchestrator.addTrigger(packaging);
        }

        clear();
        return this.orchestrator;
    }

    private void clear(){
        this.items.clear();
    }
}
