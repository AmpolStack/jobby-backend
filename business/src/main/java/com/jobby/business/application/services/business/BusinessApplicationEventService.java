package com.jobby.business.application.services.business;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.in.CreateBusinessEvent;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.stereotype.Service;

@Service
public class BusinessApplicationEventService {
    private final CreateBusinessEvent createBusinessEvent;

    public BusinessApplicationEventService(CreateBusinessEvent createBusinessEvent) {
        this.createBusinessEvent = createBusinessEvent;
    }

    public Result<Void, Error> createBusiness(Business business) {
        return this.createBusinessEvent.execute(business);
    }
}
