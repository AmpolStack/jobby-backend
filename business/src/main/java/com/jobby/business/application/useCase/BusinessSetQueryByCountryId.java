package com.jobby.business.application.useCase;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.operations.BusinessSetQuery;
import com.jobby.business.domain.ports.out.repositories.ReadOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

import java.util.Set;

public class BusinessSetQueryByCountryId extends BusinessSetQuery {
    private final int countryId;


    public BusinessSetQueryByCountryId(int countryId) {
        this.countryId = countryId;
    }

    @Override
    public Result<Set<Business>, Error> execute(ReadOnlyBusinessRepository repository) {
        return repository.findByCountryId(countryId);
    }
}
