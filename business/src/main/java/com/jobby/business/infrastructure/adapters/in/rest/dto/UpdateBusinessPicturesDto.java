package com.jobby.business.infrastructure.adapters.in.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateBusinessPicturesDto {
    private String bannerImageUrl;
    private String bannerImageBlobString;
    private String profileImageUrl;
    private String profileImageBlobString;
}
