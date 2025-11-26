package com.jobby.business.feature.business.application.useCase.events;

import com.jobby.business.feature.business.domain.operations.events.SaveBusinessEvent;
import com.jobby.business.feature.business.domain.entities.Business;
import com.jobby.business.feature.business.domain.ports.out.repositories.ReadOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public class DefaultSaveBusinessEvent extends SaveBusinessEvent {

    private final Business business;

    public DefaultSaveBusinessEvent(Business business) {
        this.business = business;
    }

    @Override
    public Result<Void, Error> execute(ReadOnlyBusinessRepository readBusinessRepository) {
        return readBusinessRepository.save(this.business);
    }
}
