package com.jobby.business.domain.ports.out.repositories;

import com.jobby.business.domain.entities.Business;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

import java.util.Set;

public interface ReadOnlyBusinessRepository {
    Result<Business, Error> findById(int id);
    Result<Boolean, Error> existByUsername(String name);
    Result<Void, Error> save(Business business);
    Result<Void, Error> update(Business business);
    Result<Set<Business>, Error> findByCityId(int cityId);
    Result<Set<Business>, Error> findByCountryId(int countryId);
}
