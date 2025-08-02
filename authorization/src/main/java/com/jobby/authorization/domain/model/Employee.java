package com.jobby.authorization.domain.model;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class Employee {
    private int employeeId;
    private int userId;
    private int sectionalId;
    private int employeeStatusId;
    private String password;
    private String email;
    private String positionName;
    private String profileImageUrl;
    private Date createdAt;
    private Date updatedAt;
    private Set<PermitAssigment> permits;
}
