package com.jobby.employee.infraestructure.adapters.in.rest.dto.created;

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
