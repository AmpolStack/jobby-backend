package com.jobby.employee.infraestructure.adapters.in.rest.dto.created;

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
