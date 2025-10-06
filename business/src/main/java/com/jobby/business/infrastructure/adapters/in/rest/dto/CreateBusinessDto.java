package com.jobby.business.infrastructure.adapters.in.rest.dto;

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
