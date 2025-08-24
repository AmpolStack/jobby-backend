package com.jobby.employee.domain.model;

import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Getter
@Setter
public class Employee {
    private int id;
    private Address address;
    private User user;
    private Sectional sectional;
    private EmployeeStatus status;
    private String username;
    private String password;
    private String positionName;
    private String profileImageUrl;
    private Instant createdAt;
    private Instant modifiedAt;
}
