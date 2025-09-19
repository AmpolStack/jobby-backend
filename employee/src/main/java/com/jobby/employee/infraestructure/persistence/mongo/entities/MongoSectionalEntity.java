package com.jobby.employee.infraestructure.persistence.mongo.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;

import java.time.Instant;

@Getter
@Setter
public class MongoSectionalEntity {
    @Name("sectional_id")
    private int id;
    private MongoAddressEntity address;
    private MongoBusinessEntity business;
    private String name;
    private String description;
    @Name("banner_image_url")
    private String bannerImageUrl;
    @Name("created_at")
    private Instant createdAt;
    @Name("modified_at")
    private Instant modifiedAt;
}
