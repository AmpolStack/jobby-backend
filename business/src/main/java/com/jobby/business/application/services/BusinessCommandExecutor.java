package com.jobby.business.application.services;

import com.jobby.business.domain.operations.BusinessCreateCommand;
import com.jobby.business.domain.operations.BusinessDeleteCommand;
import com.jobby.business.domain.operations.BusinessUpdateCommand;
import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.out.messaging.BusinessMessagePublisher;
import com.jobby.business.domain.ports.out.repositories.WriteOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.stereotype.Service;

@Service
public class BusinessCommandExecutor {
    
    private final WriteOnlyBusinessRepository businessRepository;
    private final BusinessMessagePublisher businessMessagePublisher;
    
    public BusinessCommandExecutor(WriteOnlyBusinessRepository businessRepository, 
                                   BusinessMessagePublisher businessMessagePublisher) {
        this.businessRepository = businessRepository;
        this.businessMessagePublisher = businessMessagePublisher;
    }
    
    public Result<Business, Error> execute(BusinessUpdateCommand command) {
        return command.execute(businessRepository, businessMessagePublisher);
    }

    public Result<Business, Error> execute(BusinessCreateCommand command) {
        return command.execute(businessRepository, businessMessagePublisher);
    }

    public Result<Void, Error> execute(BusinessDeleteCommand command) {
        return command.execute(businessRepository, businessMessagePublisher);
    }
}
