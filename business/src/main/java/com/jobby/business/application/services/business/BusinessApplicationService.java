package com.jobby.business.application.services.business;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.in.CreateBusinessCommand;
import com.jobby.business.domain.ports.in.FindBusinessByIdQuery;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.stereotype.Service;

@Service
public class BusinessApplicationService {

    private final CreateBusinessCommand createBusinessUseCase;
    private final FindBusinessByIdQuery findBusinessByIdQuery;

    public BusinessApplicationService(CreateBusinessCommand createBusinessUseCase,
                                      FindBusinessByIdQuery findBusinessByIdQuery) {
        this.createBusinessUseCase = createBusinessUseCase;
        this.findBusinessByIdQuery = findBusinessByIdQuery;
    }

    public Result<Business, Error> createBusiness(Business business){
        return this.createBusinessUseCase.execute(business);
    }

    public Result<Business, Error> getBusiness(int id){
        return this.findBusinessByIdQuery.execute(id);
    }
}
