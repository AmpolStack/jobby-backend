package com.jobby.business.application.services.business;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.in.CreateBusinessCommand;
import com.jobby.business.domain.ports.in.FindBusinessByAddressValueQuery;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.stereotype.Service;

@Service
public class BusinessCommandService {

    private final CreateBusinessCommand createBusinessUseCase;
    private final FindBusinessByAddressValueQuery findBusinessByAddressValueQuery;

    public BusinessCommandService(CreateBusinessCommand createBusinessUseCase, FindBusinessByAddressValueQuery findBusinessByAddressValueQuery) {
        this.createBusinessUseCase = createBusinessUseCase;
        this.findBusinessByAddressValueQuery = findBusinessByAddressValueQuery;
    }

    public Result<Business, Error> createBusiness(Business business){
        return this.createBusinessUseCase.execute(business);
    }

    public Result<Business, Error> getBusiness(String value){
        return this.findBusinessByAddressValueQuery.execute(value);
    }
}
