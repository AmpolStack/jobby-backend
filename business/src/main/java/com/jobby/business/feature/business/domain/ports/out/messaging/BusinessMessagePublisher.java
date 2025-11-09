package com.jobby.business.feature.business.domain.ports.out.messaging;

import com.jobby.business.feature.business.domain.entities.Business;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface BusinessMessagePublisher {
    Result<Void, Error> sendBusiness(Business business);
}
