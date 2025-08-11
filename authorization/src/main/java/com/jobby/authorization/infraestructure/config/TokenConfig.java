package com.jobby.authorization.infraestructure.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Validated
public class TokenConfig {
    @NotNull(message = "token.expirationMs is required")
    @Min(value = 60000, message = "token.expirationMs must be at least 60000")
    private long expirationMs;
    @NotNull(message = "token.refresh.expiration.ms is required")
    @Min(value = 120000, message = "token.refresh.expiration.ms must be at least 120000")
    private long refreshExpirationMs;
    @NotNull(message = "token.secret-key.value is required")
    @NotBlank(message = "token.secret-key.value must not be blank")
    private String secretKey;
    @NotNull(message = "token.secret-key.value is required")
    @NotBlank(message = "token.secret-key.value must not be blank")
    private String employeeSub;
    @NotNull(message = "token.secret-key.value is required")
    @NotBlank(message = "token.secret-key.value must not be blank")
    private String defaultIss;
}
