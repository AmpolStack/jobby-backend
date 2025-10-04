package com.jobby.business.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class Business {
    private int id;
    private Address address;
    private String name;
    private String bannerImageUrl;
    private String profileImageUrl;
    private String description;
    private Instant createdAt;
    private Instant modifiedAt;
}
