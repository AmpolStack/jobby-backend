package com.jobby.employee.infraestructure.persistence.mongo.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;

@Getter
@Setter
public class MongoSectionalEntity {
    @Name("sectional_id")
    private int id;
    private MongoAddressEntity address;
    private MongoBusinessEntity business;
    private String name;
    private String description;
    @Name("bannerImageUrl")
    private String bannerImageUrl;
}
