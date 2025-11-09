package com.jobby.business.feature.business.domain.operations.queries;

import com.jobby.business.feature.business.domain.entities.Business;
import com.jobby.business.feature.business.domain.ports.out.repositories.ReadOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

import java.util.Set;

public abstract class BusinessSetQuery {
    public abstract Result<Set<Business>, Error> execute(ReadOnlyBusinessRepository repository);
}
