package com.jobby.business.application.useCase;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.in.UpdateBusinessCommand;
import com.jobby.business.domain.ports.out.messaging.BusinessMessagePublisher;
import com.jobby.business.domain.ports.out.repositories.WriteOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.stereotype.Component;

@Component
public class UpdateBusinessCommandImpl implements UpdateBusinessCommand {

    private final WriteOnlyBusinessRepository businessRepository;
    private final BusinessMessagePublisher businessMessagePublisher;


    public UpdateBusinessCommandImpl(WriteOnlyBusinessRepository businessRepository, BusinessMessagePublisher businessMessagePublisher) {
        this.businessRepository = businessRepository;
        this.businessMessagePublisher = businessMessagePublisher;
    }


    @Override
    public Result<Business, Error> updatePictures(int id, String bannerImageUrl, String profileImageUrl) {
        return this.businessRepository.findById(id)
                .flatMap(business -> {
                    business.setBannerImageUrl(bannerImageUrl);
                    business.setProfileImageUrl(profileImageUrl);
                    return this.businessRepository.update(business);
                })
                .flatMap(businessSaved ->
                    this.businessMessagePublisher.sendBusiness(businessSaved)
                            .map(v -> businessSaved)
                );
    }

//    @Override
//    public Result<Business, Error> updateProperties(int id, String name, String description) {
//        return this.businessRepository.findById(id)
//                .flatMap(business -> {
//                    business.setName(name);
//                    business.setDescription(description);
//                    return this.businessRepository.update(business);
//                })
//                .flatMap(businessSaved ->
//                        this.businessMessagePublisher.sendBusiness(businessSaved)
//                                .map(v -> businessSaved)
//                );
//    }

    @Override
    public Result<Business, Error> updateProperties(int id, String name, String description) {
        return this.businessRepository.findById(id)
                .flatMap(business -> {
                    business.setName(name);
                    business.setDescription(description);
                    return this.businessRepository.update(business);
                })
                .flatMap(businessSaved ->
                        this.businessMessagePublisher.sendBusiness(businessSaved)
                                .map(v -> businessSaved)
                );
    }


}
