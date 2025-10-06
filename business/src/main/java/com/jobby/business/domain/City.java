package com.jobby.business.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City {
    private Integer id;
    private String name;
    private Country country;
}
