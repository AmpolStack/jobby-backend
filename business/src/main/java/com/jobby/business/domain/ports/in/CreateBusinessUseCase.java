package com.jobby.business.domain.ports.in;

import com.jobby.business.domain.Business;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface CreateBusinessUseCase {
    Result<Business, Error> execute(Business business);
}
