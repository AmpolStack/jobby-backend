package com.jobby.authorization.infraestructure.dto.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequest {
    @NotNull(message = "token is required")
    @NotBlank(message = "token is invalid, because is blank")
    private String token;
    @NotNull(message = "refresh token is required")
    @NotBlank(message = "refresh token is invalid, because is blank")
    private String refreshToken;
    @NotNull(message = "id is required")
    @Min(value = 0, message = "The id must be greater than 0")
    private int id;
}
