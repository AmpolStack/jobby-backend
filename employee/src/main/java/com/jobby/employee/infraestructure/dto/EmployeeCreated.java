package com.jobby.employee.infraestructure.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeCreated {
    private AddressCreated address;
    private UserCreated user;
    private int sectionalId;
    private int employeeStatusId;
    private String username;
    private String password;
    private String positionName;
    private String profileImageUrl;
}
