package com.jobby.business.infrastructure.persistence.business.mongo.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Setter
@Getter
@Document(collection = "business_info")
public class MongoBusinessEntity {
    @Name("_id")
    @Id
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
