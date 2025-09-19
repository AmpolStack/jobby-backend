package com.jobby.employee.infraestructure.persistence.mongo.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;

@Getter
@Setter
public class MongoUserEntity {
    @Name("user_id")
    private int id;
    @Name("first_name")
    private String firstName;
    @Name("last_name")
    private String lastName;
    private String email;
    private String phone;
}
