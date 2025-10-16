package com.jobby.business.application.services.business;

import com.jobby.business.domain.entities.Address;
import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.in.CreateBusinessCommand;
import com.jobby.business.domain.ports.in.FindBusinessByIdQuery;
import com.jobby.business.domain.ports.in.UpdateBusinessCommand;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.mobility.validator.ValidationChain;
import org.springframework.stereotype.Service;

@Service
public class BusinessApplicationService {

    private final CreateBusinessCommand createBusinessUseCase;
    private final UpdateBusinessCommand updateBusinessUseCase;
    private final FindBusinessByIdQuery findBusinessByIdQuery;

    public BusinessApplicationService(CreateBusinessCommand createBusinessUseCase, UpdateBusinessCommand updateBusinessUseCase,
                                      FindBusinessByIdQuery findBusinessByIdQuery) {
        this.createBusinessUseCase = createBusinessUseCase;
        this.updateBusinessUseCase = updateBusinessUseCase;
        this.findBusinessByIdQuery = findBusinessByIdQuery;
    }

    public Result<Business, Error> createBusiness(Business business){
        return this.createBusinessUseCase.execute(business);
    }

    public Result<Business, Error> getBusiness(int id){
        return this.findBusinessByIdQuery.execute(id);
    }

    public Result<Business, Error> updateProperties(int id, String name, String description){
        return ValidationChain.create()
                .validateInternalNotBlank(name, "business name")
                .validateInternalNotNull(description, "business description")
                .build()
                .flatMap(v -> this.findBusinessByIdQuery.execute(id))
                .flatMap(business -> {
                    business.setName(name);
                    business.setDescription(description);
                    return this.updateBusinessUseCase.updateProperties(business);
                });
    }

    public Result<Business, Error> updatePics(int id, String bannerImageUrl, String profileImageUrl){
        return ValidationChain.create()
                .validateInternalNotBlank(bannerImageUrl, "business banner image url")
                .validateInternalNotNull(profileImageUrl, "business profile image url")
                .build()
                .flatMap(v -> this.findBusinessByIdQuery.execute(id))
                .flatMap(business -> {
                    business.setBannerImageUrl(bannerImageUrl);
                    business.setProfileImageUrl(profileImageUrl);
                    return this.updateBusinessUseCase.updateProperties(business);
                });
    }

    public Result<Business, Error> updateAddress(int id, Address address){
        return ValidationChain.create()
                .validateInternalNotNull(address, "business address")
                .build()
                .flatMap(v -> this.findBusinessByIdQuery.execute(id))
                .flatMap(business -> {
                    business.setAddress(address);
                    return this.updateBusinessUseCase.updateProperties(business);
                });
    }

}
