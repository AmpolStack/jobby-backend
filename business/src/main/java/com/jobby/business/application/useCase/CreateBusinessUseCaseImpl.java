package com.jobby.business.application.useCase;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.in.CreateBusinessUseCase;
import com.jobby.business.domain.ports.out.BusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CreateBusinessUseCaseImpl implements CreateBusinessUseCase {

    private final BusinessRepository businessRepository;

    public CreateBusinessUseCaseImpl(@Qualifier("write") BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    public Result<Business, Error> execute(Business business) {
        return this.businessRepository.save(business)
            .flatMap(businessSaved -> this.businessRepository.findById(businessSaved.getId()));
    }
}
