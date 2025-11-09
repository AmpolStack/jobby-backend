package com.jobby.business.feature.city.domain;

import com.jobby.business.feature.country.domain.Country;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City {
    private Integer id;
    private String name;
    private Country country;
}
