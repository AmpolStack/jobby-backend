package com.jobby.employee.infraestructure.dtos.objects.created;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressCreated {
    private int cityId;
    private String value;
    private String description;
}
