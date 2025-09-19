package com.jobby.employee.infraestructure.persistence.mongo.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;

import java.time.Instant;

@Setter
@Getter
public class MongoBusinessEntity {
    @Name("business_id")
    private int id;
    private MongoAddressEntity address;
    private String name;
    @Name("banner_image_url")
    private String bannerImageUrl;
    @Name("profile_image_url")
    private String profileImageUrl;
    private String description;
    @Name("created_at")
    private Instant createdAt;
    @Name("modified_at")
    private Instant modifiedAt;
}
