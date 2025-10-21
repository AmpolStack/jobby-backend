package com.jobby.business.domain.operations;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.out.messaging.BusinessMessagePublisher;
import com.jobby.business.domain.ports.out.repositories.WriteOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import lombok.Getter;

@Getter
public abstract class BusinessUpdateCommand {
    private final int businessId;
    
    public BusinessUpdateCommand(int businessId) {
        this.businessId = businessId;
    }

    public abstract Result<Business, Error> execute(WriteOnlyBusinessRepository repository, BusinessMessagePublisher publisher);
}
