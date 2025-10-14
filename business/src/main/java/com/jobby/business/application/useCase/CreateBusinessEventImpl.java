package com.jobby.business.application.useCase;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.in.CreateBusinessEvent;
import com.jobby.business.domain.ports.out.BusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.stereotype.Service;

@Service
public class CreateBusinessEventImpl implements CreateBusinessEvent {

    private final BusinessRepository readBusinessRepository;

    public CreateBusinessEventImpl(BusinessRepository readBusinessRepository) {
        this.readBusinessRepository = readBusinessRepository;
    }

    @Override
    public Result<Business, Error> execute(Business business) {
        return this.readBusinessRepository.save(business);
    }
}
