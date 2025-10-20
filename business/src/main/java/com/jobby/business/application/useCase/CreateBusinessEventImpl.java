package com.jobby.business.application.useCase;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.in.CreateBusinessEvent;
import com.jobby.business.domain.ports.out.repositories.ReadOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.stereotype.Service;

@Service
public class CreateBusinessEventImpl implements CreateBusinessEvent {

    private final ReadOnlyBusinessRepository readBusinessRepository;

    public CreateBusinessEventImpl(ReadOnlyBusinessRepository readBusinessRepository) {
        this.readBusinessRepository = readBusinessRepository;
    }

    @Override
    public Result<Void, Error> execute(Business business) {
        return this.readBusinessRepository.save(business);
    }
}
