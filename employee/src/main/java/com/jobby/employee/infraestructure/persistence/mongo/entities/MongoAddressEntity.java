package com.jobby.employee.infraestructure.persistence.mongo.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MongoAddressEntity {
    private String country;
    private String city;
    private String value;
    private String description;
}
