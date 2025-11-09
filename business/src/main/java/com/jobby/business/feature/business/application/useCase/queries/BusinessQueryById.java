package com.jobby.business.feature.business.application.useCase.queries;

import com.jobby.business.feature.business.domain.operations.queries.BusinessQuery;
import com.jobby.business.feature.business.domain.entities.Business;
import com.jobby.business.feature.business.domain.ports.out.repositories.ReadOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public class BusinessQueryById extends BusinessQuery {
    private final int businessId;

    public BusinessQueryById(int businessId) {
        this.businessId = businessId;
    }

    @Override
    public Result<Business, Error> execute(ReadOnlyBusinessRepository repository) {
        return repository.findById(businessId);
    }
}
