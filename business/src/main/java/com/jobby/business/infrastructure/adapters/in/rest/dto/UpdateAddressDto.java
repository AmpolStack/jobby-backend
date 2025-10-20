package com.jobby.business.infrastructure.adapters.in.rest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAddressDto {
    private String value;
    private String description;
    private Integer cityId;
}
