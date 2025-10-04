package com.jobby.business.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City {
    private int id;
    private String name;
    private Country country;
}
