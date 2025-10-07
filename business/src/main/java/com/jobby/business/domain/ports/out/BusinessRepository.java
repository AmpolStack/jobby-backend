package com.jobby.business.domain.ports.out;

import com.jobby.business.domain.entities.Business;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface BusinessRepository {
    Result<Business, Error> save(Business business);
    Result<Business, Error> findById(int id);
    Result<Business, Error> findByAddress_ValueSearchable(byte[] addressValueSearchable);
}
