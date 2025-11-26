package com.jobby.business.feature.business.application.services;

import com.jobby.business.feature.business.domain.operations.commands.CreateBusinessCommand;
import com.jobby.business.feature.business.domain.operations.commands.DeleteBusinessCommand;
import com.jobby.business.feature.business.domain.operations.commands.UpdateBusinessCommand;
import com.jobby.business.feature.business.domain.entities.Business;
import com.jobby.business.feature.business.domain.ports.out.messaging.BusinessMessagePublisher;
import com.jobby.business.feature.business.domain.ports.out.repositories.WriteOnlyBusinessRepository;
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
    
    public Result<Business, Error> execute(UpdateBusinessCommand command) {
        return command.execute(businessRepository, businessMessagePublisher);
    }

    public Result<Business, Error> execute(CreateBusinessCommand command) {
        return command.execute(businessRepository, businessMessagePublisher);
    }

    public Result<Void, Error> execute(DeleteBusinessCommand command) {
        return command.execute(businessRepository, businessMessagePublisher);
    }
}
