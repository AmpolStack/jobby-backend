package com.jobby.business.feature.business.application.useCase.commands;

import com.jobby.business.feature.business.domain.operations.commands.BusinessDeleteCommand;
import com.jobby.business.feature.business.domain.ports.out.messaging.BusinessMessagePublisher;
import com.jobby.business.feature.business.domain.ports.out.repositories.WriteOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public class BusinessDeleteByIdCommand extends BusinessDeleteCommand {

    public BusinessDeleteByIdCommand(int businessId) {
        super(businessId);
    }

    @Override
    public Result<Void, Error> execute(WriteOnlyBusinessRepository repository, BusinessMessagePublisher publisher) {
        return repository.delete(this.getBusinessId());
    }
}
