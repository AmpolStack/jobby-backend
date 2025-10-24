package com.jobby.business.application.useCase;

import com.jobby.business.domain.operations.commands.BusinessCreateCommand;
import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.out.messaging.BusinessMessagePublisher;
import com.jobby.business.domain.ports.out.repositories.WriteOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public class BusinessDefaultCreateCommand extends BusinessCreateCommand {
    private final Business business;

    public BusinessDefaultCreateCommand(Business business) {
        this.business = business;
    }

    @Override
    public Result<Business, Error> execute(WriteOnlyBusinessRepository repository, BusinessMessagePublisher publisher) {
        return repository.save(this.business)
                .flatMap(businessSaved ->
                    publisher.sendBusiness(businessSaved)
                            .map(v -> businessSaved)
                );
    }
}