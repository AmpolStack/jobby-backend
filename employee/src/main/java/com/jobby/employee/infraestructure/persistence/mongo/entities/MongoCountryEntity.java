package com.jobby.employee.infraestructure.persistence.mongo.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;

@Setter
@Getter
public class MongoCountryEntity {
    @Name("country_id")
    private int id;
    private String name;
}
