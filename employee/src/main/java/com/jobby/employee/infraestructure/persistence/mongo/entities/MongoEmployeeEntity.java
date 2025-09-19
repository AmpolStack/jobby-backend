package com.jobby.employee.infraestructure.persistence.mongo.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "employee_general")
@Getter
@Setter
public class MongoEmployeeEntity {
    @Name("_id")
    @Id
    private int id;
    private MongoAddressEntity address;
    private MongoUserEntity user;
    private MongoSectionalEntity sectional;
    private MongoEmployeeStatus status;
    private String username;
    private String password;
    @Name("position_name")
    private String positionName;
    @Name("profile_image_url")
    private String profileImageUrl;
    @Name("created_at")
    private Instant createdAt;
    @Name("modified_at")
    private Instant modifiedAt;
}
