package com.jobby.employee.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class Sectional {
    private int id;
    private Address address;
    private Business business;
    private String name;
    private String description;
    private String bannerImageUrl;
    private Instant createdAt;
    private Instant modifiedAt;
}
