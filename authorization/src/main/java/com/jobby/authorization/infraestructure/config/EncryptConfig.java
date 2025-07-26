package com.jobby.authorization.infraestructure.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@Setter
public class EncryptConfig {
    @NotNull(message = "encrypt.iv.length is required")
    @Min(value = 8, message = "encrypt.iv.length must be at least 8")
    @Max(value = 16, message = "encrypt.iv.length must be at most 16")
    private Integer ivLength;

    @Valid
    @NotNull(message = "encrypt.secret-key is required")
    private SecretKey secretKey = new SecretKey();


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SecretKey {
        @NotNull(message = "encrypt.secret-key.value is required")
        @NotEmpty(message = "encrypt.secret-key.value must not be empty")
        private String value;

        @NotNull(message = "encrypt.secret-key.length is required")
        @Min(value = 128, message = "encrypt.secret-key.length must be at least 128")
        @Max(value = 256, message = "encrypt.secret-key.length must be at least 128")
        private Integer length;
    }
}
