package com.jobby.employee.infraestructure.persistence.mongo.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;
import java.util.Date;

@Getter
@Setter
public class MongoAddressEntity {
    @Name("address_id")
    private int id;
    private MongoCityEntity city;
    private String value;
    private String description;
    @Name("created_at")
    private Date createdAt;
    @Name("modified_at")
    private Date modifiedAt;
}
