package com.jobby.employee.infraestructure.persistence.mongo.entities;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;

import java.util.Date;

@Getter
@Setter
public class MongoUserEntity {
    @Name("user_id")
    private int id;

    // first_name, first_name_searchable
    @Name("first_name")
    private String firstName;
    @Name("first_name_searchable")
    private byte[] firstNameSearchable;

    // last_name, last_name_searchable
    @Name("last_name")
    private String lastName;
    @Name("last_name_searchable")
    private byte[] lastNameSearchable;

    // email, email_searchable
    @Name("email")
    private String email;
    @Name("email_searchable")
    private byte[] emailSearchable;

    // phone, phone_searchable
    @Name("phone")
    private String phone;
    @Name("phone_searchable")
    private byte[] phoneSearchable;

    @Name("created_at")
    private Date createdAt;
    @Name("modified_at")
    private Date modifiedAt;
}
