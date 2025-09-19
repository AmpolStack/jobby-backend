package com.jobby.employee.infraestructure.persistence.mongo.entities;

import com.jobby.employee.domain.model.Sectional;
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
    private Sectional sectional;
    private String status;
    private String username;
    private String password;
    @Name("position_name")
    private String positionName;
    @Name("profile_image_url")
    private String profileImageUrl;
    @Name("since")
    private Instant createdAt;
}
