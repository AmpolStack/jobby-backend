package com.jobby.business.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Address {
    private Integer id;
    private City city;
    private String value;
    private String description;
    private Date createdAt;
    private Date modifiedAt;
}
