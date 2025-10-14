package com.jobby.business.domain.ports.out;

import com.jobby.business.domain.entities.Business;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface BusinessMessagePublisher {
    Result<Void, Error> sendBusiness(Business business);
}
