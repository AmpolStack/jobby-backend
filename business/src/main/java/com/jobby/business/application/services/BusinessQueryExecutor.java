package com.jobby.business.application.services;

import com.jobby.business.domain.operations.queries.BusinessQuery;
import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.operations.queries.BusinessSetQuery;
import com.jobby.business.domain.ports.out.repositories.ReadOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class BusinessQueryExecutor {

    private final ReadOnlyBusinessRepository repository;

    public BusinessQueryExecutor(ReadOnlyBusinessRepository repository) {
        this.repository = repository;
    }

    public Result<Business, Error> execute(BusinessQuery query) {
        return query.execute(this.repository);
    }
    public Result<Set<Business>, Error> execute(BusinessSetQuery query) {
        return query.execute(this.repository);
    }
}
