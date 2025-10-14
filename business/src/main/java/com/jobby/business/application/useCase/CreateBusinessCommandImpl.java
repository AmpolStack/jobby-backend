package com.jobby.business.application.useCase;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.in.CreateBusinessCommand;
import com.jobby.business.domain.ports.out.messaging.BusinessMessagePublisher;
import com.jobby.business.domain.ports.out.repositories.WriteOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.stereotype.Service;

@Service
public class CreateBusinessCommandImpl implements CreateBusinessCommand {

    private final WriteOnlyBusinessRepository businessRepository;
    private final BusinessMessagePublisher businessMessagePublisher;

    public CreateBusinessCommandImpl(WriteOnlyBusinessRepository businessRepository, BusinessMessagePublisher businessMessagePublisher) {
        this.businessRepository = businessRepository;
        this.businessMessagePublisher = businessMessagePublisher;
    }

    @Override
    public Result<Business, Error> execute(Business business) {
        return this.businessRepository.save(business)
                .flatMap(businessSaved ->
                        this.businessMessagePublisher.sendBusiness(businessSaved)
                        .map(v -> businessSaved));
    }
}
