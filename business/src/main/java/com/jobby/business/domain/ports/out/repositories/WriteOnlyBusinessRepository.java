package com.jobby.business.domain.ports.out.repositories;

import com.jobby.business.domain.entities.Business;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface WriteOnlyBusinessRepository {
    Result<Business, Error> save(Business business);
    Result<Business, Error> update(Business business);
}
