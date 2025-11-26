package com.jobby.employee.infraestructure.persistence.mongo.entities;

import com.jobby.infraestructure.security.SecuredPassword;
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
    private MongoEmployeeStatusEntity status;
    private String password;
    @Name("username")
    private String username;
    @Name("username_searchable")
    private byte[] usernameSearchable;
    @Name("position_name")
    private String positionName;
    @Name("position_name_searchable")
    private byte[] positionNameSearchable;

    @Name("profile_image_url")
    private String profileImageUrl;
    @Name("created_at")
    private Instant createdAt;
    @Name("modified_at")
    private Instant modifiedAt;
}
