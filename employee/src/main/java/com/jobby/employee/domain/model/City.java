package com.jobby.employee.domain.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City {
    private int id;
    private String name;
    private Country country;
}
