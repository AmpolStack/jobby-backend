package com.jobby.business.infrastructure.persistence.mongo.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;

@Getter
@Setter
public class MongoCityEntity {
    @Name("city_id")
    private int id;
    private String name;
    private MongoCountryEntity country;
}
