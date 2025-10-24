package com.jobby.business.application.useCase;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.operations.queries.BusinessSetQuery;
import com.jobby.business.domain.ports.out.repositories.ReadOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

import java.util.Set;

public class BusinessSetQueryByCityId extends BusinessSetQuery {
    private final int cityId;


    public BusinessSetQueryByCityId(int cityId) {
        this.cityId = cityId;
    }

    @Override
    public Result<Set<Business>, Error> execute(ReadOnlyBusinessRepository repository) {
        return repository.findByCityId(cityId);
    }
}
