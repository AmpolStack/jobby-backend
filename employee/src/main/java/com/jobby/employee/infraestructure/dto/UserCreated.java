package com.jobby.employee.infraestructure.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreated {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
