package com.jobby.business.domain.operations.commands;

import com.jobby.business.domain.ports.out.messaging.BusinessMessagePublisher;
import com.jobby.business.domain.ports.out.repositories.WriteOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import lombok.Getter;

@Getter
public abstract class BusinessDeleteCommand {
    private final int businessId;

    public BusinessDeleteCommand(int businessId) {
        this.businessId = businessId;
    }

    public abstract Result<Void, Error> execute(WriteOnlyBusinessRepository repository, BusinessMessagePublisher publisher);
}
