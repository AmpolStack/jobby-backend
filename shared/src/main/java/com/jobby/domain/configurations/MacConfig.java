package com.jobby.domain.configurations;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MacConfig {
    @NotNull(message = "mac.secret-key.value is required")
    @NotEmpty(message = "mac.secret-key.value must not be empty")
    private String secretKey;
    
    @NotNull(message = "mac.algorithm is required")
    @NotEmpty(message = "mac.algorithm must not be empty")
    private String algorithm = "HmacSHA256";
}
