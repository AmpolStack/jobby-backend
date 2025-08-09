package com.jobby.authorization.infraestructure.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @Email(message = "is required an valid email")
    @NotNull(message = "email is required")
    @NotBlank(message = "email is invalid, because is blank")
    private String email;
    @NotNull(message = "password is required")
    @NotBlank(message = "password is invalid, because is blank")
    private String password;
}
