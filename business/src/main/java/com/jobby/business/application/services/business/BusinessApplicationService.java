package com.jobby.business.application.services.business;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.in.CreateBusinessCommand;
import com.jobby.business.domain.ports.in.DeleteBusinessCommand;
import com.jobby.business.domain.ports.in.FindBusinessByIdQuery;
import com.jobby.business.domain.ports.in.UpdateBusinessCommand;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.stereotype.Service;

@Service
public class BusinessApplicationService {

    private final CreateBusinessCommand createBusinessUseCase;
    private final UpdateBusinessCommand updateBusinessUseCase;
    private final FindBusinessByIdQuery findBusinessByIdQuery;
    private final DeleteBusinessCommand deleteBusinessUseCase;

    public BusinessApplicationService(CreateBusinessCommand createBusinessUseCase, UpdateBusinessCommand updateBusinessUseCase,
                                      FindBusinessByIdQuery findBusinessByIdQuery, DeleteBusinessCommand deleteBusinessUseCase) {
        this.createBusinessUseCase = createBusinessUseCase;
        this.updateBusinessUseCase = updateBusinessUseCase;
        this.findBusinessByIdQuery = findBusinessByIdQuery;
        this.deleteBusinessUseCase = deleteBusinessUseCase;
    }

    public Result<Business, Error> createBusiness(Business business){
        return this.createBusinessUseCase.execute(business);
    }

    public Result<Business, Error> getBusiness(int id){
        return this.findBusinessByIdQuery.execute(id);
    }

    public Result<Business, Error> updateProperties(int id, String name, String description){
        return this.updateBusinessUseCase.updateProperties(id, name, description);
    }

    public Result<Business, Error> updatePictures(int id, String bannerImageUrl, String profileImageUrl){
        return this.updateBusinessUseCase.updatePictures(id, bannerImageUrl, profileImageUrl);
    }

    public Result<Void, Error> deleteById(int id){
        return this.deleteBusinessUseCase.delete(id);
    }
}
