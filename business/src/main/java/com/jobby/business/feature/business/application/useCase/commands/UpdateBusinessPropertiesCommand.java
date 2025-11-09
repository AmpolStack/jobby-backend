package com.jobby.business.feature.business.application.useCase.commands;

import com.jobby.business.feature.business.domain.operations.commands.BusinessUpdateCommand;
import com.jobby.business.feature.business.domain.entities.Business;
import com.jobby.business.feature.business.domain.ports.out.messaging.BusinessMessagePublisher;
import com.jobby.business.feature.business.domain.ports.out.repositories.WriteOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import lombok.Getter;

@Getter
public class UpdateBusinessPropertiesCommand extends BusinessUpdateCommand {
    private final String name;
    private final String description;
    
    public UpdateBusinessPropertiesCommand(int businessId, String name, String description) {
        super(businessId);
        this.name = name;
        this.description = description;
    }

    @Override
    public Result<Business, Error> execute(WriteOnlyBusinessRepository repository, BusinessMessagePublisher publisher) {
        return repository.updateNameAndDescription(this.getBusinessId(), name, description)
                .flatMap(businessSaved ->
                    publisher.sendBusiness(businessSaved)
                            .map(v -> businessSaved)
                );
    }
}
