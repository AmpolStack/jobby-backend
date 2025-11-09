package com.jobby.business.feature.address.infrastructure.adapters.in.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatedAddressDto {
    private String value;
    private String description;
    private Integer countryId;
    private Integer cityId;
}
