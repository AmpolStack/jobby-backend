package com.jobby.business.application.services;

import com.jobby.business.domain.operations.BusinessSaveEvent;
import com.jobby.business.domain.ports.out.repositories.ReadOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.stereotype.Service;

@Service
public class BusinessEventExecutor {

    private final ReadOnlyBusinessRepository repository;

    public BusinessEventExecutor(ReadOnlyBusinessRepository repository) {
        this.repository = repository;
    }

    public Result<Void, Error> execute(BusinessSaveEvent event) {
        return event.execute(this.repository);
    }
}
