package com.jobby.employee.infraestructure.dtos.objects.created;

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
