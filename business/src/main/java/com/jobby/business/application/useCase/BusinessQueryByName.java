package com.jobby.business.application.useCase;

import com.jobby.business.domain.operations.BusinessQuery;
import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.out.repositories.ReadOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public class BusinessQueryByName extends BusinessQuery {
    private final String name;

    BusinessQueryByName(String name) {
        this.name = name;
    }

    @Override
    public Result<Business, Error> execute(ReadOnlyBusinessRepository repository) {
        return repository.findById(1);
    }
}
