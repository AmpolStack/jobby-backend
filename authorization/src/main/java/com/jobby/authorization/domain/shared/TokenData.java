package com.jobby.authorization.domain.shared;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenData {
    
    @NotNull(message = "ID cannot be null")
    @Min(value = 1, message = "ID must be greater than 0")
    private int id;
    
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be a valid email format")
    private String email;
    
    @NotBlank(message = "Audience cannot be blank")
    private String audience;
    
    @NotBlank(message = "Issuer cannot be blank")
    private String issuer;
    
    @NotNull(message = "Expiration time cannot be null")
    @Min(value = 1, message = "Expiration time must be greater than 0")
    private long msExpirationTime;
}
