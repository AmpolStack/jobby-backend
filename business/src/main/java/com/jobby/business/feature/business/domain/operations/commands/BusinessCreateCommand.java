package com.jobby.business.feature.business.domain.operations.commands;

import com.jobby.business.feature.business.domain.entities.Business;
import com.jobby.business.feature.business.domain.ports.out.messaging.BusinessMessagePublisher;
import com.jobby.business.feature.business.domain.ports.out.repositories.WriteOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public abstract class BusinessCreateCommand{
    public abstract Result<Business, Error> execute(WriteOnlyBusinessRepository repository, BusinessMessagePublisher publisher);
}
