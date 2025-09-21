package com.jobby.employee.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class User {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Date createdAt;
    private Date modifiedAt;
}
