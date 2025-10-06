package com.jobby.infraestructure.common.transaction;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import java.util.function.Supplier;

public class TransactionHandler {
    @Transactional
    public <E> Result<E, Error> execute(Supplier<Result<E, Error>> supplier) {
        var response = supplier.get();

        if(response.isFailure()){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return response;
    }

    @Transactional(readOnly = true)
    public <E> Result<E, Error> executeInRead(Supplier<Result<E, Error>> supplier){
        return supplier.get();
    }
}
