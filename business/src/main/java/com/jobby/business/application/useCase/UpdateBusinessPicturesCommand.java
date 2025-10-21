package com.jobby.business.application.useCase;

import com.jobby.business.domain.operations.BusinessUpdateCommand;
import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.out.messaging.BusinessMessagePublisher;
import com.jobby.business.domain.ports.out.repositories.WriteOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import lombok.Getter;

@Getter
public class UpdateBusinessPicturesCommand extends BusinessUpdateCommand {
    private final String bannerImageUrl;
    private final String profileImageUrl;
    
    public UpdateBusinessPicturesCommand(int businessId, String bannerImageUrl, String profileImageUrl) {
        super(businessId);
        this.bannerImageUrl = bannerImageUrl;
        this.profileImageUrl = profileImageUrl;
    }

    @Override
    public Result<Business, Error> execute(WriteOnlyBusinessRepository repository, BusinessMessagePublisher publisher) {
        return repository.updatePictures(this.getBusinessId(), bannerImageUrl, profileImageUrl)
                .flatMap(businessSaved ->
                    publisher.sendBusiness(businessSaved)
                            .map(v -> businessSaved)
                );
    }
}
