package com.jobby.business.feature.business.domain.entities;

import com.jobby.business.feature.address.domain.Address;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class Business {
    private Integer id;
    private Address address;
    private String name;
    private String bannerImageUrl;
    private String profileImageUrl;
    private String description;
    private Instant createdAt;
    private Instant modifiedAt;
}
