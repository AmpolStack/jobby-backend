package com.jobby.business.feature.business.infrastructure.adapters.in.rest.dto;

import com.jobby.business.feature.address.infrastructure.adapters.in.rest.dto.CreatedAddressDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBusinessDto {
    private CreatedAddressDto address;
    private String name;
    private String bannerImageUrl;
    private String profileImageUrl;
    private String description;
}
