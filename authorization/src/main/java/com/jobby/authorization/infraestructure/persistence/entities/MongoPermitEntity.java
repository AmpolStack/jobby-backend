package com.jobby.authorization.infraestructure.persistence.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;

import java.util.Date;

@Getter
@Setter
public class MongoPermitEntity {
    private String name;
    private String visible;
    private String description;
    @Name("modified_at")
    private Date modifiedAt;
    @Name("created_at")
    private Date createdAt;
}

