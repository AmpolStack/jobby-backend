package com.jobby.business.application.useCase.events;

import com.jobby.business.domain.operations.events.BusinessSaveEvent;
import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.out.repositories.ReadOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public class BusinessDefaultSaveEvent extends BusinessSaveEvent {

    private final Business business;

    public BusinessDefaultSaveEvent(Business business) {
        this.business = business;
    }

    @Override
    public Result<Void, Error> execute(ReadOnlyBusinessRepository readBusinessRepository) {
        return readBusinessRepository.save(this.business);
    }
}
