package com.jobby.employee.infraestructure.persistence.mongo.entities;

import com.jobby.infraestructure.common.security.mac.MacGeneratedProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;

import java.util.Date;

@Getter
@Setter
public class MongoUserEntity {
    @Name("user_id")
    private int id;
    @Name("first_name")
    private String firstName;
    @Name("first_name_searchable")
    @MacGeneratedProperty(name = "firstName")
    private byte[] firstNameSearchable;
    @Name("last_name")
    private String lastName;
    @Name("last_name_searchable")
    @MacGeneratedProperty(name = "lastName")
    private byte[] lastNameSearchable;
    private String email;
    @Name("email_searchable")
    @MacGeneratedProperty(name = "email")
    private byte[] emailSearchable;
    private String phone;
    @Name("phone_searchable")
    @MacGeneratedProperty(name = "phone")
    private byte[] phoneSearchable;
    @Name("created_at")
    private Date createdAt;
    @Name("modified_at")
    private Date modifiedAt;
}
