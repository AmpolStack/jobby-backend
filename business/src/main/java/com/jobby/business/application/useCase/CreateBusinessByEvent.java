package com.jobby.business.application.useCase;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.out.BusinessPublisher;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.stereotype.Service;

@Service
public class CreateBusinessByEvent {

    private final BusinessPublisher businessPublisher;

    public CreateBusinessByEvent(BusinessPublisher businessPublisher) {
        this.businessPublisher = businessPublisher;
    }

    public Result<Void, Error> execute(Business business){
        return this.businessPublisher.sendBusiness(business);
    }
}
