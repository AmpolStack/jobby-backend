package com.jobby.business.application.services.business;

import com.jobby.business.application.useCase.CreateBusinessByEvent;
import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.in.CreateBusinessUseCase;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.stereotype.Service;

@Service
public class BusinessCommandService {

    private final CreateBusinessUseCase createBusinessUseCase;
    private final CreateBusinessByEvent createBusinessByEvent;

    public BusinessCommandService(CreateBusinessUseCase createBusinessUseCase, CreateBusinessByEvent createBusinessByEvent) {
        this.createBusinessUseCase = createBusinessUseCase;
        this.createBusinessByEvent = createBusinessByEvent;
    }

    public Result<Business, Error> createBusiness(Business business){
        var resp = this.createBusinessUseCase.execute(business);

        resp.fold(
                this.createBusinessByEvent::execute,
                null
        );

        return resp;
    }
}
