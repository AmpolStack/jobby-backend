package com.jobby.business.application.useCase;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.in.CreateBusinessCommand;
import com.jobby.business.domain.ports.out.BusinessPublisher;
import com.jobby.business.domain.ports.out.BusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.stereotype.Service;

@Service
public class CreateBusinessCommandImpl implements CreateBusinessCommand {

    private final BusinessRepository businessRepository;
    private final BusinessPublisher businessPublisher;

    public CreateBusinessCommandImpl(BusinessRepository businessRepository, BusinessPublisher businessPublisher) {
        this.businessRepository = businessRepository;
        this.businessPublisher = businessPublisher;
    }

    @Override
    public Result<Business, Error> execute(Business business) {
        var resp = this.businessRepository.save(business);

        resp.fold(
                this.businessPublisher::sendBusiness,
                null
        );

        return resp;
    }
}
