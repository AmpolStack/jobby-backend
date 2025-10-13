package com.jobby.business.infrastructure.repository.error;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

public class PersistenceTransactionHandler {
    @Transactional
    public <E> void execute(Result<E, Error> result) {
        if(result.isFailure()){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Transactional(readOnly = true)
    public <E> void executeInRead(Result<E, Error> result){
    }
}
